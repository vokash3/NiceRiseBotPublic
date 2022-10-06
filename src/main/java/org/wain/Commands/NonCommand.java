package org.wain.Commands;

public class NonCommand {

    public String nonCommandExecute(Long chatId, String userName, String text) {
        return "Неизвестная команда!";
    }
}
