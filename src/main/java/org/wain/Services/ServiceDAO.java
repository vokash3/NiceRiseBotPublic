package org.wain.Services;

import org.wain.Models.fromDB.Crypto;
import org.wain.Models.fromDB.CryptoModel;
import org.wain.Models.fromDB.User;
import org.wain.Models.fromDB.UserSettings;

import java.util.List;

public interface ServiceDAO {

    List<User> userList(); //Список всех пользователей из user_list

    List<User> getUsersByPeriodId(int periodId);

    void updateLastGotMessage(long userID);

    String getUserPeriodName(long userId);

    List<Crypto> userCryptoList(long userID); //Список пар пользователя. Обращение должно происходить от юзера с определённым id telegram

    UserSettings getUserSettings(long userId); //Получения пользователя

    boolean addUserCryptoPair(long userID, List<CryptoModel> cryptoModelList); // Добавление криптопары пользователя

    boolean deleteUserCrypto(long userId, List<CryptoModel> cryptoModelList); // Удаление криптопары пользователя

    boolean changeUpdatePeriod(long userId, String period); // изменение периода

    boolean saveUser(User user); // Сохранить пользователя в БД


}
