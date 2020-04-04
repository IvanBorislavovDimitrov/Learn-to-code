package com.code.to.learn.api.model.course;

public class CoursePagesResponseModel {

    private long count;

    public CoursePagesResponseModel() {
    }

    public CoursePagesResponseModel(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
