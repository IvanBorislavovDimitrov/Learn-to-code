package com.code.to.learn.web.constants;

public final class Constants {

    public static final String USER_RESOURCE = "/user";

    public static final String PROFILE_PICTURE_EXTENSION = "_profile_picture";
    public static final String COURSE_VIDEO_EXTENSION = "_course_video";
    public static final String THUMBNAIL_FILE_EXTENSION = "_thumbnail";
    public static final String DELIMITER = "_";
    public static final String COURSE_CATEGORY_THUMBNAIL = "_course_category" + THUMBNAIL_FILE_EXTENSION;

    public static final long ASYNC_TIMEOUT_IN_MILISECONDS = 600 * 60 * 1000;
    public static final int SESSION_TIMEOUT_IN_SECONDS = 24 * 60 * 60;

    private Constants() {

    }
}
