package org.wain.Utils;

import org.telegram.telegrambots.meta.api.objects.User;
import org.wain.Main.App;

import java.util.Objects;

public class UserRegistrator {

    public static void addUser(User user, Long chatId){
        if(!isExist(chatId)) {
            App.serviceDAO.saveUser(new org.wain.Models.fromDB.User(user)); // Добавление в userlist
            App.serviceDAO.updateLastGotMessage(user.getId()); // создание записи в lastupdated
        }
    }

    public static boolean isExist(long chatId){
        return App.serviceDAO.userList().stream().anyMatch(user -> user.getUserId() == chatId);
    }

    public static boolean isAdmin(User user){ //TODO возможно, следует перевести на БД из property
        return user.getId().equals(Long.valueOf(Objects.requireNonNull(PropertyTaker.getCustomProperty("report.id")))) ||
                user.getId().equals(Long.valueOf(Objects.requireNonNull(PropertyTaker.getCustomProperty("adm.id"))));
    }
}
