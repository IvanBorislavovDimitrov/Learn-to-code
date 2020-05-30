package com.code.to.learn.core.rating;

public abstract class RatingCalculator {

    public double calculateRating(RateEstimator rateEstimator) {
        return rateEstimator.estimateRate(getNewStars(), getCurrentRate(), getRateCount());
    }

    protected abstract int getNewStars();

    protected abstract double getCurrentRate();

    protected abstract int getRateCount();
}
