package com.code.to.learn.persistence.service.impl;

import com.code.to.learn.persistence.dao.api.GithubDao;
import com.code.to.learn.persistence.domain.entity.GithubAccessToken;
import com.code.to.learn.persistence.domain.model.GithubAccessTokenServiceModel;
import com.code.to.learn.persistence.service.api.GithubService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GithubServiceImpl extends GenericServiceImpl<GithubAccessToken, GithubAccessTokenServiceModel> implements GithubService {

    @Autowired
    public GithubServiceImpl(GithubDao githubDao, ModelMapper modelMapper) {
        super(githubDao, modelMapper);
    }

    @Override
    public GithubAccessTokenServiceModel findByAccessToken(String accessToken) {
        return toOutput(getGenericDao().findByField("accessToken", accessToken).get());
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
