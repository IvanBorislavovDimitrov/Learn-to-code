package com.code.to.learn.api.model.course_category;

import javax.validation.constraints.NotNull;

public class CourseCategoryBindingModel {

    @NotNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
