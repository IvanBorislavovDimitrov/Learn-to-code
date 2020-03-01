package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.dao.api.GithubDao;
import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class GithubDaoImpl extends GenericDaoImpl<GithubAccessToken> implements GithubDao {

    @Autowired
    public GithubDaoImpl(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    protected Class<GithubAccessToken> getDomainClassType() {
        return GithubAccessToken.class;
    }
}
