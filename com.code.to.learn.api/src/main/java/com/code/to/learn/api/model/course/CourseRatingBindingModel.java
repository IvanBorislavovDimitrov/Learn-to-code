package com.code.to.learn.api.model.course;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CourseRatingBindingModel {

    @NotNull
    private String courseName;
    @NotNull
    @Min(1)
    @Max(5)
    private int stars;
    @NotNull
    private CourseRatingType courseRatingType;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public CourseRatingType getCourseRatingType() {
        return courseRatingType;
    }

    public void setCourseRatingType(CourseRatingType courseRatingType) {
        this.courseRatingType = courseRatingType;
    }
}
