package org.wain.Hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.wain.Models.TestConnection;

public class HibernateConfigurationTest {

    private static SessionFactory sessionFactory;
    private static Session session;

    @BeforeClass
    public static void getSession() {
        sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(TestConnection.class)
                .buildSessionFactory();
        session = sessionFactory.getCurrentSession();
    }

    @Test
    public void sessionShouldBeOpened() {
        session.beginTransaction();
        TestConnection testConnection = (TestConnection) session.createQuery("from TestConnection where status = :status")
                .setParameter("status", 1).uniqueResult();
        Assert.assertEquals(1, testConnection.getStatus());
    }

    @AfterClass
    public static void closeSession() {
        session.close();
        sessionFactory.close();
    }

}
