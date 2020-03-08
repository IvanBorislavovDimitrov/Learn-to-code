package com.code.to.learn.persistence.domain.model;

public class HomeworkServiceModel extends GenericServiceModel {

    private String name;
    private CourseServiceModel course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CourseServiceModel getCourse() {
        return course;
    }

    public void setCourse(CourseServiceModel course) {
        this.course = course;
    }
}
