package org.wain.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.wain.Models.fromDB.Crypto;
import org.wain.Models.fromDB.Period;
import org.wain.Models.fromDB.User;
import org.wain.Models.fromDB.UserLastUpdated;

public class HibernateSession {

    private static final SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Period.class)
                .addAnnotatedClass(Crypto.class)
                .addAnnotatedClass(UserLastUpdated.class)
                .buildSessionFactory();
    }

    public static Session getSession() {
        return sessionFactory.getCurrentSession();
    }
}
