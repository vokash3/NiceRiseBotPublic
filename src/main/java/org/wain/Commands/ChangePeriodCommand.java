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

public class ChangePeriodCommand extends BotCommand {

    private static final Logger LOG = LogManager.getLogger();


    public ChangePeriodCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (arguments.length > 1)
            sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(), "Ошибка при изменении периода (много аргументов)!");
        boolean result = App.serviceDAO.changeUpdatePeriod(user.getId(), arguments[0]);
        sendAnswer(absSender, user.getId(), this.getCommandIdentifier(), user.getUserName(),
                result ? "Период обновления успешно обновлён" : "Ошибка при добавлении (нет такого периода обновления)!");
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
