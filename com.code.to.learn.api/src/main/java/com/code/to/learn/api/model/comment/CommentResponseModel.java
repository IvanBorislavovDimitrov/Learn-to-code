package com.code.to.learn.api.model.comment;

import com.code.to.learn.api.model.course.CourseNameResponseModel;
import com.code.to.learn.api.model.user.UserResponseModel;

import java.time.LocalDate;

public class CommentResponseModel {

    private String id;
    private String content;
    private UserResponseModel author;
    private LocalDate date;

    private CourseNameResponseModel course;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public UserResponseModel getAuthor() {
        return author;
    }

    public void setAuthor(UserResponseModel author) {
        this.author = author;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CourseNameResponseModel getCourse() {
        return course;
    }

    public void setCourse(CourseNameResponseModel course) {
        this.course = course;
    }
}
