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

public class DeleteCoinPairCommand extends BotCommand {

    private static final Logger LOG = LogManager.getLogger();

    public DeleteCoinPairCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        boolean result = false;
        if (arguments.length == 0) {
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка! Не указаны криптопары!");
            return;
        }
        Set<CryptoModel> cryptoModelSet = new HashSet<>();
        for (String s : arguments)
            cryptoModelSet.add(new CryptoModel(s.toUpperCase().trim()));
        List<CryptoModel> coinPairs = CoinpairChecker.getCheckedListOfPairs(cryptoModelSet);
        if (coinPairs.size() == 0) {
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка! Проверьте указанные криптопары!");
            return;
        } else
            result = App.serviceDAO.deleteUserCrypto(user.getId(), coinPairs);
        if (result)
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Успешно!");
        else
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка при удалении!");
    }

    public void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        //включаем поддержку режима разметки, чтобы управлять отображением текста и добавлять эмодзи
        message.enableMarkdown(true);
        message.setChatId(chatId.toString());
        message.enableHtml(true);
        message.setText(text);
        message.setReplyMarkup(new UserKeyboard());
        try {
            absSender.execute(message);
        } catch (TelegramApiException e) {
            LOG.error(e);
        }
    }


}
