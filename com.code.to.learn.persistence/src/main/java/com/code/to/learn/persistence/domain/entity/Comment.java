package com.code.to.learn.persistence.domain.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "comments")
public class Comment extends IdEntity<Comment> {

    public static final String COURSE = "course";

    @Lob
    private String content;

    @NotNull
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public Comment merge(Comment comment) {
        setContent(comment.getContent());
        setDate(comment.getDate());
        setAuthor(comment.getAuthor());
        setCourse(comment.getCourse());
        return this;
    }
}
