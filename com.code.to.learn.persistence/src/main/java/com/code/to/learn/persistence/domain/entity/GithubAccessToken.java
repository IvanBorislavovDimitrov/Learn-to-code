package com.code.to.learn.persistence.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "github_access_tokens")
public class GithubAccessToken extends GenericEntity<GithubAccessToken> {

    private static final String ACCESS_TOKEN = "access_token";
    private static final String SCOPE = "scope";
    private static final String TOKEN_TYPE = "token_type";

    @Column(name = ACCESS_TOKEN, nullable = false)
    private String accessToken;

    @Column(name = SCOPE)
    private String scope;

    @Column(name = TOKEN_TYPE)
    private String tokenType;

    @OneToOne(mappedBy = "githubAccessToken", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User accessTokenOwner;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public User getAccessTokenOwner() {
        return accessTokenOwner;
    }

    public void setAccessTokenOwner(User accessTokenOwner) {
        this.accessTokenOwner = accessTokenOwner;
    }

    @Override
    public GithubAccessToken merge(GithubAccessToken githubAccessToken) {
        setAccessToken(githubAccessToken.getAccessToken());
        setScope(githubAccessToken.getScope());
        setTokenType(githubAccessToken.getTokenType());
        return this;
    }
}
