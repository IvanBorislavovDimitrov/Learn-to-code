package com.code.to.learn.api.util;

import com.code.to.learn.api.exception.InvalidRangeHeaderException;

import java.util.Objects;

public class RangeHeaderGetter {

    private static final String BYTES = "bytes";

    private final String range;

    private RangeHeaderGetter(String range) {
        this.range = range;
    }

    public static RangeHeaderGetter createRangeHeaderGetter(String range) {
        validateRangeHeader(range);
        return new RangeHeaderGetter(range);
    }

    private static void validateRangeHeader(String range) {
        if (range == null) {
            throw new InvalidRangeHeaderException(null);
        }
        String[] rangeParts = range.split("=");
        if (range.length() < 2) {
            throw new InvalidRangeHeaderException("Invalid parts length");
        }
        if (!Objects.equals(rangeParts[0], BYTES)) {
            throw new InvalidRangeHeaderException("No bytes name present");
        }
    }

    public long getOffset() {
        String[] rangeParts = range.split("=");
        try {
            String[] numbersParts = rangeParts[1].split("-");
            return Long.parseLong(numbersParts[0]);
        } catch (NumberFormatException e) {
            throw new InvalidRangeHeaderException(e.getMessage());
        }
    }

}
