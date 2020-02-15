package com.code.to.learn.persistence.dao.impl;

import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.dao.api.GithubDao;
import org.springframework.stereotype.Repository;

@Repository
public class GithubDaoImpl extends GenericDaoImpl<GithubAccessToken> implements GithubDao {

    @Override
    protected Class<GithubAccessToken> getDomainClassType() {
        return GithubAccessToken.class;
    }
}
