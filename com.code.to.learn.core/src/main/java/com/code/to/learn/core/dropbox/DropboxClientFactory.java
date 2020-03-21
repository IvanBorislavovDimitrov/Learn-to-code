package com.code.to.learn.core.dropbox;

import com.code.to.learn.core.environment.ApplicationConfiguration;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DropboxClientFactory {

    private final ApplicationConfiguration applicationConfiguration;

    @Autowired
    public DropboxClientFactory(ApplicationConfiguration applicationConfiguration) {
        this.applicationConfiguration = applicationConfiguration;
    }

    public DbxClientV2 createDropboxClient() {
        DbxRequestConfig dbxRequestConfig = DbxRequestConfig.newBuilder(applicationConfiguration.getDropboxClientIdentifier())
                .build();
        return new DbxClientV2(dbxRequestConfig, applicationConfiguration.getDropboxAccessToken());
    }
}
