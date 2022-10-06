package org.wain.Services;

import org.wain.Hibernate.DAO.DAOImpl;
import org.wain.Models.PeriodFactory;
import org.wain.Models.fromDB.*;

import java.sql.Timestamp;
import java.util.List;

public class ServiceDAOImpl implements ServiceDAO {

    DAOImpl dao = new DAOImpl();

    @Override
    public List<User> userList() {
        return dao.userList();
    }

    @Override
    public List<Crypto> userCryptoList(long userID) {
        return dao.getUser(userID).getCryptoList();
    }

    @Override
    public String getUserPeriodName(long userId) {
        return dao.getUser(userId).getPeriod().getPeriodName();
    }

    @Override
    public void updateLastGotMessage(long userID) {

        User userFromDB = dao.getUser(userID);
        if (userFromDB.getUserLastUpdated() == null) { // Для новых юзеров
            UserLastUpdated lastUpdated = new UserLastUpdated();
            lastUpdated.setUserId(userID);
            userFromDB.setUserLastUpdated(lastUpdated);
        }
        userFromDB.getUserLastUpdated().setLastUpdated(new Timestamp(System.currentTimeMillis()));
        dao.save(userFromDB);
    }

    @Override
    public boolean addUserCryptoPair(long userID, List<CryptoModel> cryptoModelList) {
        return dao.addOrUpdateCoinpair(userID, cryptoModelList);
    }

    @Override
    public boolean deleteUserCrypto(long userId, List<CryptoModel> cryptoModelList) {
        return dao.removeCoinpairFromUser(userId, cryptoModelList);
    }

    @Override
    public boolean changeUpdatePeriod(long userId, String period) {
        String p = period.toUpperCase();
        User userFromDB = dao.getUser(userId);
        userFromDB.setPeriod(new Period());
        switch (p) {
            case "1H":
                userFromDB.setPeriod(PeriodFactory.getPeriod(PeriodFactory.Periods.HOURLY));
                return dao.save(userFromDB);
            case "30M":
                userFromDB.setPeriod(PeriodFactory.getPeriod(PeriodFactory.Periods.HALF_HOURLY));
                return dao.save(userFromDB);
            case "15M":
                userFromDB.setPeriod(PeriodFactory.getPeriod(PeriodFactory.Periods.FIFTEEN_MINUTES));
                return dao.save(userFromDB);
            case "1D":
                userFromDB.setPeriod(PeriodFactory.getPeriod(PeriodFactory.Periods.DAILY));
                return dao.save(userFromDB);
        }
        return false;
    }

    @Override
    public UserSettings getUserSettings(long userId) {
        User user = dao.getUser(userId);
        UserSettings userSettings = new UserSettings();
        userSettings.setUserId(userId);
        userSettings.setMuteElon(user.isMuteElon());
        userSettings.setUpdatePeriodName(user.getPeriod().getPeriodName());
        userSettings.setLastUpdated(user.getUserLastUpdated().getLastUpdated());

        return userSettings;
    }

    @Override
    public List<User> getUsersByPeriodId(int periodId) {
        return dao.getUsersByPeriodId(periodId);
    }

    @Override
    public boolean saveUser(User user) {
        return dao.save(user);
    }
}
