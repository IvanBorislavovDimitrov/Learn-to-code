package com.code.to.learn.api.model.course;

public class RatedCourseResponseModel {

    private String courseName;
    private boolean isRated;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }
}
