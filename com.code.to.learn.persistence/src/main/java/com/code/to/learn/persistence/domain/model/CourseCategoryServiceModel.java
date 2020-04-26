package com.code.to.learn.persistence.domain.model;

import com.code.to.learn.persistence.domain.generic.NamedElement;

import java.util.List;

public class CourseCategoryServiceModel extends IdServiceModel implements NamedElement {

    private String name;
    private String description;
    private String thumbnailName;
    private List<CourseServiceModel> courses;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CourseServiceModel> getCourses() {
        return courses;
    }

    public void setCourses(List<CourseServiceModel> courses) {
        this.courses = courses;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }
}
