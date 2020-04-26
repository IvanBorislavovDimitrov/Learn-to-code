package com.code.to.learn.persistence.domain.model;

public class CourseCategoryWithCoursesNumber {

    private String name;
    private String description;
    private int coursesNumber;
    private String thumbnailName;

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

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public static class Builder {
        private final CourseCategoryWithCoursesNumber courseCategoryWithCoursesNumber = new CourseCategoryWithCoursesNumber();

        public Builder name(String name) {
            courseCategoryWithCoursesNumber.setName(name);
            return this;
        }

        public Builder description(String description) {
            courseCategoryWithCoursesNumber.setDescription(description);
            return this;
        }

        public Builder coursesNumber(int coursesNumber) {
            courseCategoryWithCoursesNumber.setCoursesNumber(coursesNumber);
            return this;
        }

        public Builder thumbnailName(String thumbnailName) {
            courseCategoryWithCoursesNumber.setThumbnailName(thumbnailName);
            return this;
        }

        public CourseCategoryWithCoursesNumber build() {
            return courseCategoryWithCoursesNumber;
        }
    }
}
