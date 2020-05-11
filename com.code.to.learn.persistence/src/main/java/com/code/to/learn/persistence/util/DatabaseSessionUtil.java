package com.code.to.learn.persistence.util;

import com.code.to.learn.persistence.exception.basic.LCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseSessionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSessionUtil.class);

    private DatabaseSessionUtil() {

    }

    public static Session getCurrentOrOpen(SessionFactory sessionFactory) {
        try {
            Session session = sessionFactory.getCurrentSession();
            beginTransactionIfNotActive(session.getTransaction());
            return session;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return openNewSession(sessionFactory);
        }
    }

    private static void beginTransactionIfNotActive(Transaction transaction) {
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    private static Session openNewSession(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session;
    }

    public static void closeWithRollback(SessionFactory sessionFactory) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        rollbackTransaction(session.getTransaction());
        close(session);
    }

    private static void close(Session session) {
        try {
            session.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    private static void rollbackTransaction(Transaction transaction) {
        try {
            transaction.rollback();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void closeWithCommit(SessionFactory sessionFactory) {
        Session session;
        try {
            session = sessionFactory.getCurrentSession();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return;
        }
        try {
            commitTransaction(session.getTransaction());
        } finally {
            close(session);
        }
    }

    private static void commitTransaction(Transaction transaction) {
        try {
            transaction.commit();
        } catch (Exception e) {
            throw new LCException(e.getMessage(), e);
        }
    }

}
