package com.code.to.learn.core.rating;

public interface RateEstimator {

    double estimateRate(int newStars, double currentRate, int rateCount);
}
