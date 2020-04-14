package com.code.to.learn.api.model.comment;

import javax.validation.constraints.NotNull;

public class CommentBindingModel {

    @NotNull
    private String courseName;

    @NotNull
    private String content;

    private String author;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
