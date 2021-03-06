package com.code.to.learn.core.interceptor;

import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class HibernateSessionManagementInterceptor implements HandlerInterceptor {

    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateSessionManagementInterceptor(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getRequestURI() != null && request.getRequestURI().startsWith("/resource")) {
            return true;
        }
        Session session = DatabaseSessionUtil.openNewSession(sessionFactory);
        DatabaseSessionUtil.bindSessionToContext(session);
        DatabaseSessionUtil.beginTransaction(session);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if (request.getRequestURI() != null && request.getRequestURI().startsWith("/resource")) {
            return;
        }
        try {
            if (response.getStatus() >= 200 && response.getStatus() <= 299) {
                DatabaseSessionUtil.closeWithCommit(sessionFactory);
                return;
            }
            DatabaseSessionUtil.closeWithRollback(sessionFactory);
        } finally {
            DatabaseSessionUtil.unbindSessionFactory(sessionFactory);
        }

    }
}
