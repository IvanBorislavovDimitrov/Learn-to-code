package com.code.to.learn.api.model.course_category;

import javax.validation.constraints.NotNull;

public class CourseCategoryBindingModel {

    @NotNull
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
