package org.wain.Hibernate.DAO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.wain.Hibernate.HibernateSession;
import org.wain.Models.fromDB.Crypto;
import org.wain.Models.fromDB.CryptoModel;
import org.wain.Models.fromDB.User;
import org.wain.Utils.CoinpairChecker;

import javax.persistence.Query;
import java.util.List;

public class DAOImpl implements DAO {

    private static final Logger LOG = LogManager.getLogger(DAOImpl.class);

    public List<User> userList() {
        LOG.info("userList()");
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            List<User> userList = session.createQuery("from User").getResultList();
            session.getTransaction().commit();
            return userList;
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            return null;
        }
    }

    @Override
    public User getUser(long userId) {

        LOG.info("getUser(id = {})", userId);
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            Query query = session.createQuery("from User where userId = :userId");
            query.setParameter("userId", userId);
            User user = (User) query.getSingleResult();
            tx.commit();
            return user;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            return null;
        }
    }

    @Override
    public boolean save(User user) {

        LOG.info("save(User {})", user.getUserId());
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            session.saveOrUpdate(user);
            tx.commit();
            return true;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            LOG.error(e.getMessage());
            return false;
        }
    }

    public List<User> getUsersByPeriodId(int periodId) {

        LOG.info("getUsersByPeriodId({})", periodId);
        Transaction tx = null;
        try (Session session = getSession()) {
            tx = session.beginTransaction();
            Query query = session.createQuery("from User where period.id = :periodId");
            query.setParameter("periodId", periodId);
            List<User> users = (List<User>) query.getResultList();
            tx.commit();
            return users;
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            if (tx != null) tx.rollback();
            return null;
        }
    }

    @Override
    public boolean addOrUpdateCoinpair(long userId, List<CryptoModel> cryptoListProxy) {

        LOG.info("addOrUpdateCoinpair({}, {}) ", userId, cryptoListProxy.toString());
        Transaction tx = null;
        Crypto crypto = null;
        Session session = null;
        try {
            session = getSession();
            tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            for (CryptoModel pair : cryptoListProxy) {
                if (CoinpairChecker.checkUserPair(pair.getCryptoName(), user.getCryptoList()))
                    continue;
                String cryptoName = pair.getCryptoName();
                crypto = (Crypto) session.createQuery("from Crypto where cryptoName = :cryptoName") //проверка на уже сущеcтсвующую пару в БД
                        .setParameter("cryptoName", cryptoName).uniqueResult();
                if (crypto == null)
                    crypto = new Crypto(cryptoName);
//                crypto.addUserToUserslist(user);
                session.saveOrUpdate(crypto); // Сохраняет и присваивает ID сущности автоматически
                user.addCryptoToUser(crypto);
                session.saveOrUpdate(user);
            }
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            if (tx != null) tx.rollback();
            return false;
        }
    }

    @Override
    public boolean removeCoinpairFromUser(long userId, List<CryptoModel> cryptoListProxy) {

        LOG.info("removeCoinpairFromUser({}, {}) ", userId, cryptoListProxy.toString());
        Transaction tx = null;
        Crypto crypto = null;
        Session session = null;
        try {
            session = getSession();
            tx = session.beginTransaction();
            User user = session.get(User.class, userId);
            for (CryptoModel pair : cryptoListProxy) {
                String cryptoName = pair.getCryptoName();
                crypto = (Crypto) session.createQuery("from Crypto where cryptoName = :cryptoName") //проверка на уже сущетсвующую пару в БД
                        .setParameter("cryptoName", cryptoName).uniqueResult();
                if (crypto == null) {
                    LOG.error("No such crypto pair ({}) in user's {} crypto list!", cryptoName, user.getUserId()); //FIXME Возможно, уже лишняя проверка, так как в CoinppairChecker уже создавал верификацию
                    continue;
                }
                user.removeCryptoFromUser(crypto);
                session.update(user);
                crypto.removeUserFromUserList(user);
                session.update(crypto);
            }
            session.getTransaction().commit();
            return true;
        } catch (HibernateException e) {
            LOG.error(e.getMessage());
            if (tx != null) tx.rollback();
            return false;
        }
    }

    protected Session getSession() {
        return HibernateSession.getSession();
    }
}
