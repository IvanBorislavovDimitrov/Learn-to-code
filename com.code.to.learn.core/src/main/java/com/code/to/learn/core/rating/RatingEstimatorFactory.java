package com.code.to.learn.core.rating;

import com.code.to.learn.api.model.course.CourseRatingType;
import com.code.to.learn.persistence.exception.basic.NotFoundException;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class RatingEstimatorFactory {

    public RateEstimator createRateEstimator(CourseRatingType courseRatingType) {
        if (courseRatingType == CourseRatingType.INPUT) {
            return new InputRateEstimator();
        }
        if (courseRatingType == CourseRatingType.OUTPUT) {
            return new OutputRateEstimator();
        }
        throw new NotFoundException(MessageFormat.format("Rating type: \"{0}\" not found", courseRatingType));
    }
}
