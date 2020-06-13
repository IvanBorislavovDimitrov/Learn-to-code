package com.code.to.learn.persistence.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "github_access_tokens")
public class GithubAccessToken extends IdEntity<GithubAccessToken> {

    @Column(nullable = false, unique = true)
    private String accessToken;

    @Basic
    private String scope;

    @Basic
    private String tokenType;

    @OneToOne(mappedBy = "githubAccessToken", cascade = {CascadeType.ALL})
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
