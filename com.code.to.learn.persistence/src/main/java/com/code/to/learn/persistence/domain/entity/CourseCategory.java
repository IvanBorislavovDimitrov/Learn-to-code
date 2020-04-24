package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.generic.NamedElement;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course_categories")
public class CourseCategory extends IdEntity<CourseCategory> implements NamedElement {

    @Column(nullable = false, unique = true)
    private String name;

    @Lob
    private String description;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Course> courses = new ArrayList<>();

    @Override
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
        setDescription(courseCategory.getDescription());
        return this;
    }
}
