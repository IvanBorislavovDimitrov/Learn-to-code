package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.UserDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    @Autowired
    public UserDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findByField(User.USERNAME, username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findByField(User.EMAIL, email);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return findByField(User.PHONE_NUMBER, phoneNumber);
    }

    @Override
    public List<User> findUsersByUsernameContaining(String username) {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<User> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root).where(criteriaBuilder.like(criteriaBuilder.lower(root.get(User.USERNAME)), buildContainsExpression(username)));
        Query<User> query = session.createQuery(criteriaQuery);
        return query.getResultList();
    }


    @Override
    public List<User> findTeachers() {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
        Root<Course> courseRoot = criteriaQuery.from(Course.class);
        courseRoot.join(Course.TEACHER, JoinType.INNER);
        criteriaQuery.select(courseRoot);
        Query<Course> teachersQuery = session.createQuery(criteriaQuery);
        return getDistinctTeachers(teachersQuery);
    }

    @Override
    public List<User> findAllUsersWithUnpaidCourses() {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> userRoot = userCriteriaQuery.from(User.class);
        userRoot.join("unpaidCourses", JoinType.INNER);
        userCriteriaQuery.select(userRoot);
        Query<User> userQuery = session.createQuery(userCriteriaQuery);
        return userQuery.getResultList();
    }

    private List<User> getDistinctTeachers(Query<Course> teachersQuery) {
        return teachersQuery.getResultList()
                .stream()
                .map(Course::getTeacher)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<User> forceFindUsersByPage(int page, int maxResults) {
        return executeInNewTransaction((session, transaction) -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> userCriteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<User> userRoot = userCriteriaQuery.from(getDomainClassType());
            userCriteriaQuery.select(userRoot);
            Query<User> userQuery = session.createQuery(userCriteriaQuery);
            userQuery.setFirstResult(page * maxResults);
            userQuery.setMaxResults(maxResults);
            return userQuery.getResultList();
        });
    }

    @Override
    public Optional<User> findByResetPasswordToken(String resetPasswordToken) {
        Session session = DatabaseSessionUtil.getCurrentSession(sessionFactory);
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<User> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root).where(criteriaBuilder.equal(criteriaBuilder.lower(root.get(User.RESET_PASSWORD_TOKEN)), resetPasswordToken));
        return getOrEmpty(session, criteriaQuery);
    }

    @Override
    public Optional<User> forceFindByUsername(String username) {
        return executeInNewTransaction((session, transaction) -> {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
            Root<User> root = criteriaQuery.from(getDomainClassType());
            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(User.USERNAME), username));
            Optional<User> user = getOrEmpty(session, criteriaQuery);
            if (user.isPresent()) {
                Hibernate.initialize(user.get().getLoginRecords());
                Hibernate.initialize(user.get().getAuthorities());
            }
            return user;
        });
    }

    @Override
    public Optional<User> forceUpdate(User user) {
        return executeInNewTransaction((session, transaction) -> {
            session.update(user);
            return Optional.of(user);
        });
    }

    @Override
    protected Class<User> getDomainClassType() {
        return User.class;
    }

}
