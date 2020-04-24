package com.code.to.learn.persistence.domain.model;

public class CourseCategoryWithCoursesNumber {

    private String name;
    private String description;
    private int coursesNumber;

    public CourseCategoryWithCoursesNumber() {
    }

    public CourseCategoryWithCoursesNumber(String name, String description, int coursesNumber) {
        this.name = name;
        this.coursesNumber = coursesNumber;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCoursesNumber() {
        return coursesNumber;
    }

    public void setCoursesNumber(int coursesNumber) {
        this.coursesNumber = coursesNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
