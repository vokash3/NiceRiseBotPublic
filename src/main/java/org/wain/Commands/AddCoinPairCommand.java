package org.wain.Commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Keyboards.UserKeyboard;
import org.wain.Main.App;
import org.wain.Models.fromDB.CryptoModel;
import org.wain.Utils.CoinpairChecker;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AddCoinPairCommand extends BotCommand {

    private static final Logger LOG = LogManager.getLogger();

    public AddCoinPairCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length == 0)
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка при добавлении!");
        Set<CryptoModel> cryptoModelSet = new HashSet<>();
        for (String s : arguments)
            cryptoModelSet.add(new CryptoModel(s.toUpperCase().trim()));
        List<CryptoModel> coinPairs = CoinpairChecker.getCheckedListOfPairs(cryptoModelSet);
        boolean result;
        if (coinPairs.size() == 0) {
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка при добавлении!");
            return;
        } else
            result = App.serviceDAO.addUserCryptoPair(user.getId(), coinPairs);
        if (result)
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Успешно!");
        else
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка при добавлении!");
    }

    public void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());
        message.enableHtml(true);
        message.setText(text);
        message.setReplyMarkup(new UserKeyboard());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            LOG.error("user {}: {}", userName, e.getMessage());
        }
    }
}
