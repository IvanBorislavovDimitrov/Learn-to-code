package com.code.to.learn.web.constants;

public final class Constants {

    public static final String USER_RESOURCE = "/user";
    public static final String REPOSITORIES_RESOURCE = "/user/repos";
    public static final String REPOSITORY_RESOURCE = "/repos";

    public static final String PROFILE_PICTURE_EXTENSION = "_profile_picture";
    public static final String COURSE_VIDEO_EXTENSION = "_course_video";
    public static final String THUMBNAIL_FILE_EXTENSION = "_thumbnail";
    public static final String DELIMITER = "_";
    public static final String COURSE_CATEGORY_THUMBNAIL = "_course_category" + THUMBNAIL_FILE_EXTENSION;
    public static final String DEFAULT_PICTURE_NAME = "default_picture_name.jpg";

    public static final long ASYNC_TIMEOUT_IN_MILISECONDS = 600 * 60 * 1000;
    public static final int SESSION_TIMEOUT_IN_SECONDS = 24 * 60 * 60;

    public static final String MAIL_SMTP_HOST = "mail.smtp.host";
    public static final String MAIL_SMTP_PORT = "mail.smtp.port";
    public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
    public static final String MAIL_SMPT_START_TLS_ENABLED = "mail.smtp.starttls.enable";

    public static final String THREE_AM_EVERY_DAY = "0 0 3 * * ?";
    public static final int MAX_USERS_BY_PAGE = 10;

    private Constants() {

    }
}
