package com.code.to.learn.core.rating;

public class InputRateEstimator implements RateEstimator {

    @Override
    public double estimateRate(int newStars, double currentRate, int rateCount) {
        return currentRate + newStars;
    }
}
