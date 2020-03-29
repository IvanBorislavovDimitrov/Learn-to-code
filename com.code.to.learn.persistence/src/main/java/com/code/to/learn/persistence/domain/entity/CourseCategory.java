package com.code.to.learn.persistence.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_categories")
public class CourseCategory extends GenericEntity<CourseCategory> {

    public static final String NAME = "name";

    @Column(name = NAME, nullable = false)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Course> courses = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public CourseCategory merge(CourseCategory courseCategory) {
        setName(courseCategory.getName());
        setCourses(courseCategory.getCourses());
        return this;
    }
}
