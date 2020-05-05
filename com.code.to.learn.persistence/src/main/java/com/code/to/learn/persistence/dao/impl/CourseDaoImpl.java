package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.CourseDao;
import com.code.to.learn.persistence.domain.entity.Comment;
import com.code.to.learn.persistence.domain.entity.Course;
import com.code.to.learn.persistence.domain.entity.CourseCategory;
import com.code.to.learn.persistence.domain.entity.User;
import com.code.to.learn.persistence.util.DatabaseSessionUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.code.to.learn.persistence.domain.entity.Course.*;

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
        Query<Course> latestCoursesQuery = getCoursesOrderedByStartDate();
        latestCoursesQuery.setMaxResults(count);
        return latestCoursesQuery.getResultList();
    }

    private Query<Course> getCoursesOrderedByStartDate() {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<Course> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root);
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(START_DATE)));
        return session.createQuery(criteriaQuery);
    }

    @Override
    public List<Course> findCourses(int page, int maxResults, String name, String category) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<Course> root = criteriaQuery.from(getDomainClassType());
        criteriaQuery.select(root);
        List<Predicate> predicates = new ArrayList<>();
        if (name != null) {
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(Course.NAME)), buildContainsExpression(name.toLowerCase())));
        }
        if (category != null) {
            Root<CourseCategory> courseCategoryRoot = criteriaQuery.from(CourseCategory.class);
            predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(courseCategoryRoot.get(CourseCategory.NAME)),
                    category));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        criteriaQuery.orderBy(criteriaBuilder.desc(root.get(START_DATE)));
        Query<Course> courseQuery = session.createQuery(criteriaQuery);
        courseQuery.setFirstResult(page * maxResults);
        courseQuery.setMaxResults(maxResults);
        return courseQuery.getResultList();
    }

    @Override
    public long countByNameLike(String name) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        Root<Course> root = criteriaQuery.from(getDomainClassType());
        Predicate like = criteriaBuilder.like(criteriaBuilder.lower(root.get(Course.NAME)), buildContainsExpression(name.toLowerCase()));
        criteriaQuery.where(like);
        criteriaQuery.select(criteriaBuilder.count(root));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public List<Course> findBestSellers(int limit) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> courseCriteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<Course> courseRoot = courseCriteriaQuery.from(getDomainClassType());
        courseRoot.join(ATTENDANTS, JoinType.LEFT);
        courseCriteriaQuery.groupBy(courseRoot.get(Course.ID));
        courseCriteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(courseCriteriaQuery.from(User.class))));
        courseCriteriaQuery.select(courseRoot);
        Query<Course> bestSellers = session.createQuery(courseCriteriaQuery);
        return bestSellers.setMaxResults(limit)
                .getResultList();
    }

    @Override
    public List<Course> findMostCommented(int limit) {
        Session session = DatabaseSessionUtil.getCurrentOrOpen(getSessionFactory());
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Course> courseCriteriaQuery = criteriaBuilder.createQuery(getDomainClassType());
        Root<Course> courseRoot = courseCriteriaQuery.from(getDomainClassType());
        courseRoot.join(COMMENTS, JoinType.LEFT);
        courseCriteriaQuery.groupBy(courseRoot.get(Course.ID));
        courseCriteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.count(courseCriteriaQuery.from(Comment.class))));
        courseCriteriaQuery.select(courseRoot);
        Query<Course> mostCommented = session.createQuery(courseCriteriaQuery);
        return mostCommented.setMaxResults(limit)
                .getResultList();
    }

    @Override
    protected Class<Course> getDomainClassType() {
        return Course.class;
    }
}
