package com.code.to.learn.persistence.domain.model;

import com.code.to.learn.persistence.domain.generic.NamedElement;

import java.util.List;

public class CourseCategoryServiceModel extends IdServiceModel implements NamedElement {

    private String name;
    private List<CourseServiceModel> courses;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<CourseServiceModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseServiceModel> courses) {
        this.courses = courses;
    }
}
