package org.wain.Commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.wain.Utils.PropertyTaker;

import java.util.Arrays;
import java.util.Objects;


public class ReportCommand extends BotCommand {

    private static final Logger LOG = LogManager.getLogger();

    public ReportCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        StringBuffer answer = new StringBuffer();
        answer.append("Name: @").append(user.getUserName()).append("\nFirst name: ").append(user.getFirstName()).append("\nLast name: ").append(user.getLastName()).append("\nLanguage Code: ").append(user.getLanguageCode()).append("\nID: ").append(user.getId()).append("\n\n");
        Arrays.stream(arguments).forEach(s -> answer.append(s).append(" "));

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                answer.toString());
    }

    public void sendAnswer(AbsSender absSender, Long chatId, String commandName, String userName, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdown(true);
        message.enableHtml(true);
        message.setText("<b>Report!</b>\n\n\n" + "<i>" + text + "</i>");
        try {
            message.setChatId(Objects.requireNonNull(PropertyTaker.getCustomProperty("report.id")));
            absSender.execute(message);
            message.setChatId(chatId.toString());
            message.setText("<i>Спасибо за обращение!</i>");
            absSender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            LOG.error("user {}: {}", userName, e.getMessage());
        }
    }
}
