package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

import static com.code.to.learn.persistence.domain.entity.Course.START_DATE;

@Repository
public class CourseDaoImpl extends GenericDaoImpl<Course> implements CourseDao {

    @Autowired
    public CourseDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public Optional<Course> findByName(String courseName) {
        return findByField(Course.NAME, courseName);
    }

    @Override
    public List<Course> findLatestCourses(int count) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<Course> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(START_DATE)));
        criteriaQuery.select(criteriaQuery.from(getDomainClassType()));
        Query<Course> latestCoursesQuery = session.createQuery(criteriaQuery);
        latestCoursesQuery.setMaxResults(count);
        return latestCoursesQuery.getResultList();
    }

    @Override
    protected Class<Course> getDomainClassType() {
        return Course.class;
    }
}
