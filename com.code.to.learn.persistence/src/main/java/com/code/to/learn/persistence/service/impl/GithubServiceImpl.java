package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.repository.api.GithubRepository;
import com.code.to.learn.persistence.service.api.GithubService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GithubServiceImpl extends GenericService<GithubAccessToken, GithubAccessTokenServiceModel> implements GithubService {

    protected GithubServiceImpl(GithubRepository githubRepository, ModelMapper modelMapper) {
        super(githubRepository, modelMapper);
    }

    @Override
    protected Class<GithubAccessTokenServiceModel> getModelClass() {
        return GithubAccessTokenServiceModel.class;
    }

    @Override
    protected Class<GithubAccessToken> getEntityClass() {
        return GithubAccessToken.class;
    }
}
