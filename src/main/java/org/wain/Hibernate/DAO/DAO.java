package org.wain.Hibernate.DAO;

import org.wain.Models.fromDB.CryptoModel;
import org.wain.Models.fromDB.User;

import java.util.List;

public interface DAO {

    List<User> userList(); //Список всех пользователей из user_list

    User getUser(long userId);

    boolean save(User user);

    boolean addOrUpdateCoinpair(long userId, List<CryptoModel> cryptoProxyList);

    boolean removeCoinpairFromUser(long userId, List<CryptoModel> cryptoProxyList);
}
