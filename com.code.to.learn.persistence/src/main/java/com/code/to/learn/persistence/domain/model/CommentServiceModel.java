package com.code.to.learn.persistence.domain.model;

import java.time.LocalDate;

public class CommentServiceModel extends IdServiceModel {

    private String content;

    private LocalDate date;

    private UserServiceModel author;

    private CourseServiceModel course;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public UserServiceModel getAuthor() {
        return author;
    }

    public void setAuthor(UserServiceModel author) {
        this.author = author;
    }

    public CourseServiceModel getCourse() {
        return course;
    }

    public void setCourse(CourseServiceModel course) {
        this.course = course;
    }
}
