package com.code.to.learn.core.environment;

import org.springframework.stereotype.Component;

@Component
public class ApplicationConfiguration {

    // Variable names
    private static final String GITHUB_URL_NAME = "GITHUB_URL";
    private static final String PARSER_NAME = "PARSER_NAME";
    private static final String GITHUB_ACCESS_TOKEN_NAME = "GITHUB_ACCESS_TOKEN";
    private static final String CLIENT_ID_NAME = "CLIENT_ID";
    private static final String CLIENT_SECRET_NAME = "CLIENT_SECRET_NAME";
    private static final String HIBERNATE_DIALECT_NAME = "HIBERNATE_DIALECT";
    private static final String HIBERNATE_POSTGRESQL_DRIVER_NAME = "HIBERNATE_POSTGRESQL_DRIVER";
    private static final String HIBERNATE_POSTGRESQL_CONNECTION_POOL_SIZE_NAME = "HIBERNATE_CONNECTION_POOL_SIZE";
    private static final String HIBERNATE_DDL_STRATEGY_NAME = "HIBERNATE_DDL_STRATEGY";
    private static final String HIBERNATE_ARCHIVE_AUTODETECTION_NAME = "HIBERNATE_ARCHIVE_AUTODETECTION";
    private static final String HIBERNATE_SHOW_SQL_NAME = "HIBERNATE_SHOW_SQL";
    private static final String HIBERNATE_DBCP_INITIAL_SIZE_NAME = "HIBERNATE_DBCP_INITIAL_SIZE";
    private static final String HIBERNATE_DBCP_MAX_TOTAL_NAME = "HIBERNATE_DBCP_MAX_TOTAL";
    private static final String HIBERNATE_DBCP_MAX_IDLE_NAME = "HIBERNATE_DBCP_MAX_IDLE";
    private static final String HIBERNATE_DBCP_MIN_IDLE_NAME = "HIBERNATE_DBCP_MIN_IDLE";
    private static final String HIBERNATE_DBCP_MAX_WAIT_MILLIS_NAME = "HIBERNATE_DBCP_MAX_WAIT_MILLIS";
    private static final String HIBERNATE_SESSION_CONTEXT_CLASS_NAME = "HIBERNATE_SESSION_CONTEXT_CLASS";
    private static final String DROPBOX_CLIENT_IDENTIFIER_NAME = "DROPBOX_CLIENT_IDENTIFIER_NAME";
    private static final String DROPBOX_ACCESS_TOKEN_NAME = "DROPBOX_ACCESS_TOKEN";

    // Variable default values
    private static final String CLIENT_ID_VALUE = "5b2f3c2f8bb2f09aa59d";
    private static final String CLIENT_SECRET_VALUE = "6b118a1b5bf8983c134be95a6b9fc1e437b84560";
    private static final String DEFAULT_GITHUB_URL_VALUE = "https://api.github.com";
    private static final String PARSER_VALUE = "JSON";
    private static final String GITHUB_ACCESS_TOKEN_URL_VALUE = "https://github.com/login/oauth/access_token";
    private static final String HIBERNATE_DIALECT_POSTGRE_SQL_9_DIALECT_VALUE = "org.hibernate.dialect.PostgreSQL9Dialect";
    private static final String HIBERNATE_POSTGRESQL_DRIVER_VALUE = "org.postgresql.Driver";
    private static final String HIBERNATE_CONNECTION_POOL_SIZE_VALUE = "3";
    private static final String HIBERNATE_DDL_STRATEGY_VALUE = "create";
    private static final String HIBERNATE_ARCHIVE_AUTO_DETECTION_VALUE = "class";
    private static final String HIBERNATE_SHOW_SQL_VALUE = "true";
    private static final String HIBERNATE_DBCP_INITIAL_SIZE_VALUE = "2";
    private static final String HIBERNATE_DBCP_MAX_TOTAL_VALUE = "3";
    private static final String HIBERNATE_DBCP_MAX_IDLE_VALUE = "3";
    private static final String HIBERNATE_DBCP_MIN_IDLE_VALUE = "0";
    private static final String HIBERNATE_DBCP_MAX_WAIT_MILLIS_VALUE = "-1";
    private static final String HIBERNATE_SESSION_CONTEXT_CLASS_VALUE = "org.hibernate.context.internal.ThreadLocalSessionContext";
    private static final String DROPBOX_CLIENT_IDENTIFIER_VALUE = "dropbox/learn-to-code";
    private static final String DROPBOX_ACCESS_TOKEN_VALUE = "Hvk-7gwjBJAAAAAAAAAAGCBG9tH2zJkc2iUIf8pYdekaLvYk2mTY-LWBoEsP6Q5z";

    public String getGithubApiUrl() {
        return getOrDefault(GITHUB_URL_NAME, DEFAULT_GITHUB_URL_VALUE);
    }

    public String getGithubAccessTokenUrl() {
        return getOrDefault(GITHUB_ACCESS_TOKEN_NAME, GITHUB_ACCESS_TOKEN_URL_VALUE);
    }

    public String getClientIdValue() {
        return getOrDefault(CLIENT_ID_NAME, CLIENT_ID_VALUE);
    }

    public String getClientSecretValue() {
        return getOrDefault(CLIENT_SECRET_NAME, CLIENT_SECRET_VALUE);
    }

    public String getParserType() {
        return getOrDefault(PARSER_NAME, PARSER_VALUE);
    }

    public String getHibernateDialect() {
        return getOrDefault(HIBERNATE_DIALECT_NAME, HIBERNATE_DIALECT_POSTGRE_SQL_9_DIALECT_VALUE);
    }

    public String getHibernatePostgresqlDriver() {
        return getOrDefault(HIBERNATE_POSTGRESQL_DRIVER_NAME, HIBERNATE_POSTGRESQL_DRIVER_VALUE);
    }

    public String getHibernateConnectionPool() {
        return getOrDefault(HIBERNATE_POSTGRESQL_CONNECTION_POOL_SIZE_NAME, HIBERNATE_CONNECTION_POOL_SIZE_VALUE);
    }

    public String getHibernateDDLStrategy() {
        return getOrDefault(HIBERNATE_DDL_STRATEGY_NAME, HIBERNATE_DDL_STRATEGY_VALUE);
    }

    public String getHibernateArchiveAutodetection() {
        return getOrDefault(HIBERNATE_ARCHIVE_AUTODETECTION_NAME, HIBERNATE_ARCHIVE_AUTO_DETECTION_VALUE);
    }

    public String hibernateShowSql() {
        return getOrDefault(HIBERNATE_SHOW_SQL_NAME, HIBERNATE_SHOW_SQL_VALUE);
    }

    public String getHibernateDBCPInitialSize() {
        return getOrDefault(HIBERNATE_DBCP_INITIAL_SIZE_NAME, HIBERNATE_DBCP_INITIAL_SIZE_VALUE);
    }

    public String getHibernateDBCPMaxTotal() {
        return getOrDefault(HIBERNATE_DBCP_MAX_TOTAL_NAME, HIBERNATE_DBCP_MAX_TOTAL_VALUE);
    }

    public String getHibernateDBCPMaxIdle() {
        return getOrDefault(HIBERNATE_DBCP_MAX_IDLE_NAME, HIBERNATE_DBCP_MAX_IDLE_VALUE);
    }

    public String getHibernateDBCPMinIdle() {
        return getOrDefault(HIBERNATE_DBCP_MIN_IDLE_NAME, HIBERNATE_DBCP_MIN_IDLE_VALUE);
    }

    public String getHibernateDBCPMaxWait() {
        return getOrDefault(HIBERNATE_DBCP_MAX_WAIT_MILLIS_NAME, HIBERNATE_DBCP_MAX_WAIT_MILLIS_VALUE);
    }

    public String getHibernateSessionContext() {
        return getOrDefault(HIBERNATE_SESSION_CONTEXT_CLASS_NAME, HIBERNATE_SESSION_CONTEXT_CLASS_VALUE);
    }

    public String getDropboxAccessToken() {
        return getOrDefault(DROPBOX_ACCESS_TOKEN_NAME, DROPBOX_ACCESS_TOKEN_VALUE);
    }

    public String getDropboxClientIdentifier() {
        return getOrDefault(DROPBOX_CLIENT_IDENTIFIER_NAME, DROPBOX_CLIENT_IDENTIFIER_VALUE);
    }

    @SuppressWarnings("unchecked")
    private <T> T getOrDefault(String name, T defaultValue) {
        String value = EnvironmentGetter.getVariable(name);
        if (value == null) {
            return defaultValue;
        }
        if (defaultValue instanceof Boolean) {
            return (T) Boolean.valueOf(value);
        } else if (defaultValue instanceof String) {
            return (T) value;
        } else if (defaultValue instanceof Integer) {
            return (T) Integer.valueOf(value);
        } else if (defaultValue instanceof Double) {
            return (T) Double.valueOf(value);
        } else if (defaultValue instanceof Long) {
            return (T) Long.valueOf(value);
        }
        return (T) value;
    }
}
