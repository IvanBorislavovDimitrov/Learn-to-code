package com.code.to.learn.core.rating;

public class OutputRateEstimator implements RateEstimator {

    @Override
    public double estimateRate(int newStars, double currentRate, int rateCount) {
        return rateCount == 0 ? 0 : currentRate / rateCount;
    }
}
