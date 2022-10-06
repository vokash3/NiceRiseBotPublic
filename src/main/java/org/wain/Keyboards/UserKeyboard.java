package org.wain.Keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

public class UserKeyboard extends ReplyKeyboardMarkup {
    public UserKeyboard() {
        setSelective(true);
        setResizeKeyboard(true);
        setOneTimeKeyboard(true);

        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add("Текущие значения");

        KeyboardRow keyboardSecondRow = new KeyboardRow();
        keyboardSecondRow.add("Мои настройки");
        keyboardSecondRow.add("Мой список пар");

        //Третья строчка клавиатуры
//        KeyboardRow keyboardThirdRow = new KeyboardRow();
//        keyboardThirdRow.add("Илон говори!");
//        keyboardThirdRow.add("Илон молчи!");


        KeyboardRow keyboardFourthRow = new KeyboardRow();
        keyboardFourthRow.add("Помощь по командам");

        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardFourthRow);
        setKeyboard(keyboard);
    }
}
