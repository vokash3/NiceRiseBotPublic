package org.wain.Commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Main.App;
import org.wain.Utils.UserRegistrator;

import java.util.Arrays;

public class AnnouncementCommand extends BotCommand {

    private static final Logger LOG = LogManager.getLogger();

    public AnnouncementCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        if (!UserRegistrator.isAdmin(user)) return; //Выхоим
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        StringBuffer answer = new StringBuffer();
        Arrays.stream(arguments).forEach(s -> answer.append(s).append(" "));
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                answer.toString());
    }

    public void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.enableHtml(true);
        message.setText("<b>Оповещение!</b>\n\n" + "<i>" + text + "</i>");
        App.serviceDAO.userList().forEach(user -> {
            try {
                message.setChatId(String.valueOf(user.getUserId()));
                absSender.execute(message);
            } catch (TelegramApiException e) {
                LOG.error("user {}: {}", userName, e.getMessage());
            }
        });
    }
}
