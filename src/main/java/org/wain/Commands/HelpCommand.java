package org.wain.Commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.wain.Utils.PropertyTaker;

public class HelpCommand extends ServiceCommand{

    private final String TEXT = "<b><i>"+ PropertyTaker.getCustomProperty("bot.version") +
            "</i></b>\n\n" + "<i>" +
            "Команда /report ТЕКСТ_ОБРАЩЕНИЯ позволяет оставить отзыв или жалобу. " + "</i>\n\n" +
            "<i>/add НАЗВАНИЯ_КРИПТОПАР_ЧЕРЕЗ ПРОБЕЛ (например, /add USDTRUB BNBUSDT BTCRUB) - добавляет криптопару(-ы) в список отслеживания;</i>\n\n" +
            "<i>/delete НАЗВАНИЯ_КРИПТОПАР_ЧЕРЕЗ_ПРОБЕЛ (например, /delete USDTRUB BNBUSDT BTCRUB) - удаляет криптопару(-ы) из списка отслеживания;</i>\n\n" +
            "<i>/period PERIOD (например, /period 1H) - изменения периода отслеживания. Доступные таймфреймы: 1h - 1 час, 30m - 30 минут, 15m - 15 минут, 1d - 1 день;</i>\n";

    public HelpCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, TEXT); //@TODO
    }
}
