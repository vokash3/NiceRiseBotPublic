package org.wain.Commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Keyboards.UserKeyboard;

abstract public class ServiceCommand extends BotCommand {
    /**
     * Суперкласс для сервисных команд
     */
    protected static final Logger LOG = LogManager.getLogger();

    public ServiceCommand(String identifier, String description) {
        super(identifier, description);
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
