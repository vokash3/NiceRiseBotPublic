package org.wain.Commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.wain.Binance.ShowPrice;
import org.wain.Utils.PropertyTaker;
import org.wain.Utils.UserRegistrator;


public class StartCommand extends ServiceCommand {

    private final String START_MESSAGE = "<b><i>Welcome!</i></b>\n\n" + "<i>" +
            "Closed Club. \n Who knows, that knows ;-) \n\n" +
            "Команда /report ТЕКСТ_ОБРАЩЕНИЯ позволяет оставить отзыв или жалобу!"
            + "</i>\n\n\n" + PropertyTaker.getCustomProperty("bot.version");

    public StartCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        //формируем имя пользователя - поскольку userName может быть не заполнено, для этого случая используем имя и фамилию пользователя
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, START_MESSAGE);
        try {
            sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, new ShowPrice().getAllStats(user.getId()));
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
        UserRegistrator.addUser(user, chat.getId());
    }

}
