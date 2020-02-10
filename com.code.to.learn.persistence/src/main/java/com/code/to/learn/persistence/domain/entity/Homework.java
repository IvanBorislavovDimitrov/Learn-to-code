package com.code.to.learn.persistence.domain.entity;

import javax.persistence.*;

@Entity
@Table(name = "homework")
public class Homework extends IdEntity {

    private static final String NAME = "name";

    @Column(name = NAME, nullable = false)
    private String name;

    @ManyToOne(targetEntity = Course.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
