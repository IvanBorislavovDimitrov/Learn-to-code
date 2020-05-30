package com.code.to.learn.core.rating;

import com.code.to.learn.persistence.domain.model.CourseServiceModel;

public class CourseRatingCalculator extends RatingCalculator {

    private final int stars;
    private final CourseServiceModel courseServiceModel;

    public CourseRatingCalculator(int stars, CourseServiceModel courseServiceModel) {
        this.stars = stars;
        this.courseServiceModel = courseServiceModel;
    }

    @Override
    protected int getNewStars() {
        return stars;
    }

    @Override
    protected double getCurrentRate() {
        return courseServiceModel.getRating();
    }

    @Override
    protected int getRateCount() {
        return courseServiceModel.getRatingCount();
    }
}
