package org.wain.Bot;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Binance.runnable.PriceWatcher;
import org.wain.Binance.ShowPrice;
import org.wain.Commands.*;
import org.wain.Keyboards.UserKeyboard;
import org.wain.Models.UserMessage;
import org.wain.Models.fromDB.Crypto;
import org.wain.Services.ServiceDAO;
import org.wain.Services.ServiceDAOImpl;
import org.wain.Utils.FilesOpener;
import org.wain.Utils.FilesUpdater;
import org.wain.Utils.Helper;
import org.wain.Utils.PropertyTaker;

import java.util.*;

public class Bot extends TelegramLongPollingCommandBot {

    private static final Logger LOG = LogManager.getLogger(Bot.class);

    private final String BOT_NAME = PropertyTaker.getCustomProperty("bot.name");
    private final String BOT_TOKEN = PropertyTaker.getCustomProperty("bot.token");
    private final NonCommand nonCommand;
    private final ReplyKeyboardMarkup replyKeyboardMarkup;
    private final ServiceDAO serviceDAO;
    private final int SPAM_TIMER = Integer.parseInt(Objects.requireNonNull(PropertyTaker.getCustomProperty("spam.cd"))) / 1000;
    private final String HELP_MESSAGE = "<i>Команда /report ТЕКСТ_ОБРАЩЕНИЯ позволяет оставить отзыв или жалобу. " + "</i>\n\n" +
            "<i>/add НАЗВАНИЯ_КРИПТОПАР_ЧЕРЕЗ ПРОБЕЛ (например, /add USDTRUB BNBUSDT BTCRUB) - добавляет криптопару(-ы) в список отслеживания;</i>\n\n" +
            "<i>/delete НАЗВАНИЯ_КРИПТОПАР_ЧЕРЕЗ_ПРОБЕЛ (например, /delete USDTRUB BNBUSDT BTCRUB) - удаляет криптопару(-ы) из списка отслеживания;</i>\n\n" +
            "<i>/period PERIOD (например, /period 1H) - изменения периода отслеживания. Доступные таймфреймы: 1h - 1 час, 30m - 30 минут, 15m - 15 минут, 1d - 1 день;</i>\n";


    public Bot() {
        super();
        LOG.info("Bot() constructor");
        this.nonCommand = new NonCommand();
        register(new StartCommand("start", "Старт"));
        register(new HelpCommand("help", "Помощь"));
        register(new AnnouncementCommand("shout", "Оповещение"));
        register(new ReportCommand("report", "Обращение"));
        register(new AddCoinPairCommand("add", "Добавить отслеживание криптопары"));
        register(new DeleteCoinPairCommand("delete", "Удалить криптопары"));
        register(new ChangePeriodCommand("period", "Изменить период обновлений"));
        replyKeyboardMarkup = new UserKeyboard();
        new Thread(new PriceWatcher()).start();
        serviceDAO = new ServiceDAOImpl();
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        updates.forEach(update -> {
            if (update.hasMessage() && update.getMessage().hasText()) {
                Message inMessage = update.getMessage();
                Long chatId = inMessage.getChatId();
                LOG.info("Got message:\"" + update.getMessage().getText() + "\" from user_id=" + update.getMessage().getFrom().getId());
                SendMessage outMessage = new SendMessage();
                outMessage.setChatId(inMessage.getChatId().toString());
                outMessage.enableHtml(true);

                if (isSpamer(update.getMessage().getFrom())) {
                    outMessage.setText("<b>Не спамь! Теперь жди " + SPAM_TIMER + " секунд :-( </b>");
                } else if (inMessage.getText().startsWith("/")) {
//                    if (inMessage.getText().equals("/start") || inMessage.getText().equals("/report")) {
//                        super.onUpdatesReceived(updates);
//                        return;
//                    }
                    super.onUpdatesReceived(updates);
                    return;
                } else {
                    if (inMessage.getText().equals("Мой список пар"))
                        outMessage.setText(listPairsAsString(serviceDAO.userCryptoList(chatId)));
                    else if (inMessage.getText().equals("Мои настройки"))
                        outMessage.setText(serviceDAO.getUserSettings(chatId).toString());
                    else if (inMessage.getText().equals("Текущие значения"))
                        outMessage.setText(new ShowPrice().getAllStats(chatId));
                    else if (inMessage.getText().equals("Помощь по командам"))
                        outMessage.setText(HELP_MESSAGE);
                    else {
                        super.onUpdatesReceived(updates);
                        return;
                    }
                }
                try {
                    execute(outMessage);
                } catch (TelegramApiException e) {
                    LOG.error("TelegramApiException: {1}", e);
                }
            } else if (update.hasCallbackQuery()) {
                try {
                    SendMessage sendMessage = new SendMessage();
                    if (update.getCallbackQuery().getData().equals("/help"))
                        sendMessage.setText(update.getCallbackQuery().getData());
                    else
                        sendMessage.setText(update.getCallbackQuery().getData());
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId().toString());
                    execute(sendMessage);
                } catch (TelegramApiException e) {
                    LOG.error("TelegramApiException {1}", e);
                }
            }
        });
    }

    @Override
    public void processNonCommandUpdate(Update update) {
        Message msg = update.getMessage();
        Long chatId = msg.getChatId();
        String userName = getUserName(msg);

        String answer = nonCommand.nonCommandExecute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.enableMarkdown(true);
        answer.setText(text);
        answer.setChatId(chatId.toString());
        answer.setReplyMarkup(replyKeyboardMarkup);
        // Образец inline клавиатуры:
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> lst2 = new ArrayList<>();
        List<InlineKeyboardButton> lst = new ArrayList<>();
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText("Помощь");
        button.setCallbackData("/help");
        lst.add(button);
        lst2.add(lst);
        inlineKeyboardMarkup.setKeyboard(lst2);
        answer.setReplyMarkup(inlineKeyboardMarkup);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            LOG.error("user {}: {}", userName, e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    private boolean isSpamer(User user) {
        if (FilesOpener.isEmpty(Helper.lastMessage)) {
            Map<Long, Long> messages = FilesOpener.getLastMessageStats();
            messages.put(user.getId(), new Date().getTime());
            FilesUpdater.updateLastUserMessage(mapToListLastMessages(messages));
            return false;
        }
        Map<Long, Long> messages = FilesOpener.getLastMessageStats();
        if (messages.isEmpty() || !messages.containsKey(user.getId())) {
            messages.put(user.getId(), new Date().getTime());
            FilesUpdater.updateLastUserMessage(mapToListLastMessages(messages));
            return false;
        } else {
            if ((new Date().getTime() - messages.get(user.getId())) <= SPAM_TIMER) {
                messages.put(user.getId(), new Date().getTime());
                FilesUpdater.updateLastUserMessage(mapToListLastMessages(messages));
                return true;
            } else {
                messages.put(user.getId(), new Date().getTime());
                FilesUpdater.updateLastUserMessage(mapToListLastMessages(messages));
                return false;
            }
        }
    }

    private List<UserMessage> mapToListLastMessages(Map<Long, Long> messages) {
        List<UserMessage> userMessages = new ArrayList<>();
        for (Map.Entry<Long, Long> entry : messages.entrySet())
            userMessages.add(new UserMessage(entry.getKey(), entry.getValue()));
        return userMessages;
    }

    private String listPairsAsString(List<Crypto> cryptoModelList) {
        if (cryptoModelList.isEmpty())
            return "Пустой список пар!";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cryptoModelList.size(); i++)
            sb.append(i + 1).append(") ").append(cryptoModelList.get(i).getCryptoName()).append("\n");
        return sb.toString();
    }
}
