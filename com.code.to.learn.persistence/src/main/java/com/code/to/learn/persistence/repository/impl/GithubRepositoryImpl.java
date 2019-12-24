package com.code.to.learn.persistence.repository.impl;

import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.repository.api.GithubRepository;
import org.springframework.stereotype.Repository;

@Repository
public class GithubRepositoryImpl extends GenericRepositoryImpl<GithubAccessToken> implements GithubRepository {

    @Override
    protected Class<GithubAccessToken> getDomainClassType() {
        return GithubAccessToken.class;
    }
}
