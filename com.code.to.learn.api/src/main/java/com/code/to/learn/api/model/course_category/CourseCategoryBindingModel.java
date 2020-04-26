package com.code.to.learn.api.model.course_category;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class CourseCategoryBindingModel {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private MultipartFile thumbnail;

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

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }
}
