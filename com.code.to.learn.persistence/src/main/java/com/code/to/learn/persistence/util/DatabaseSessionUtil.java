package com.code.to.learn.persistence.util;

import com.code.to.learn.persistence.exception.basic.LCException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.context.internal.ManagedSessionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseSessionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseSessionUtil.class);

    private DatabaseSessionUtil() {

    }

    public static Session getCurrentSession(SessionFactory sessionFactory) {
        Session session = sessionFactory.getCurrentSession();
        beginTransactionIfNotActive(session.getTransaction());
        ManagedSessionContext.bind(session);
        return session;
    }

    private static void beginTransactionIfNotActive(Transaction transaction) {
        if (!transaction.isActive()) {
            transaction.begin();
        }
    }

    public static Session openNewSession(SessionFactory sessionFactory) {
        Session session = sessionFactory.openSession();
        ManagedSessionContext.bind(session);
        return session;
    }

    public static Transaction beginTransaction(Session session) {
        return session.beginTransaction();
    }

    public static void closeWithRollback(SessionFactory sessionFactory) {
        try {
            Session session;
            try {
                session = sessionFactory.getCurrentSession();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return;
            }
            rollbackTransaction(session.getTransaction());
        } finally {
            close(sessionFactory);
        }
    }

    public static void rollbackTransaction(Transaction transaction) {
        try {
            if (transaction != null) {
                transaction.rollback();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static void closeWithCommit(SessionFactory sessionFactory) {
        try {
            Session session;
            try {
                session = sessionFactory.getCurrentSession();
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return;
            }
            commitTransaction(session.getTransaction());
        } finally {
            close(sessionFactory);
        }
    }

    public static void commitTransaction(Transaction transaction) {
        try {
            if (transaction != null && transaction.isActive()) {
                transaction.commit();
            }
        } catch (Exception e) {
            throw new LCException(e.getMessage(), e);
        }
    }

    public static void closeSession(SessionFactory sessionFactory, Session session) {
        if (session != null) {
            try {
                session.close();
                ManagedSessionContext.unbind(sessionFactory);
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }

    private static void close(SessionFactory sessionFactory) {
        try {
            Session currentSession = sessionFactory.getCurrentSession();
            ManagedSessionContext.unbind(sessionFactory);
            currentSession.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

}
