package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.repository.api.GithubRepository;
import com.code.to.learn.persistence.service.api.GithubGenericService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubGenericServiceImplImpl extends GenericServiceImpl<GithubAccessToken, GithubAccessTokenServiceModel> implements GithubGenericService {

    @Autowired
    public GithubGenericServiceImplImpl(GithubRepository githubRepository, ModelMapper modelMapper) {
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
