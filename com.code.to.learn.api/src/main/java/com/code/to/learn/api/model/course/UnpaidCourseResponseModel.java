package com.code.to.learn.api.model.course;

public class UnpaidCourseResponseModel {

    private String username;
    private String courseName;

    public UnpaidCourseResponseModel() {
    }

    public UnpaidCourseResponseModel(String username, String courseName) {
        this.username = username;
        this.courseName = courseName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
