package com.code.to.learn.persistence.domain.model;

import com.code.to.learn.persistence.domain.generic.NamedElement;

public class HomeworkServiceModel extends IdServiceModel implements NamedElement {

    private String name;
    private CourseServiceModel course;

    @Override
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
