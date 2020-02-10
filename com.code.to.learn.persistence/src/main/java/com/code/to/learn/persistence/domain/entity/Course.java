package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.entity.entity_enum.FormOfEducation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course extends IdEntity {

    private static final String NAME = "name";
    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String DURATION_IN_WEEKS = "duration_in_weeks";
    private static final String CREDITS = "credits";
    private static final String FORM_OF_EDUCATION = "formOfEducation";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";

    @Column(name = NAME, nullable = false, unique = true)
    private String name;

    @Column(name = START_DATE, nullable = false)
    private LocalDate startDate;

    @Column(name = END_DATE, nullable = false)
    private LocalDate endDate;

    @Column(name = DURATION_IN_WEEKS, nullable = false)
    private int durationInWeeks;

    @Column(name = CREDITS, nullable = false)
    private int credits;

    @Enumerated(EnumType.STRING)
    @Column(name = FORM_OF_EDUCATION, nullable = false)
    private FormOfEducation formOfEducation;

    @Column(name = PRICE, nullable = false)
    private BigDecimal price;

    @Column(name = DESCRIPTION)
    @Lob
    private String description;

    @ManyToOne(targetEntity = CourseCategory.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory category;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attendant_id", referencedColumnName = "id"))
    private List<User> attendants;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToMany(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "future_attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "future_attendants_id", referencedColumnName = "id"))
    private List<User> futureAttendants;

    @OneToMany(targetEntity = Homework.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "course")
    private List<Homework> homework;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getDurationInWeeks() {
        return durationInWeeks;
    }

    public void setDurationInWeeks(int durationInWeeks) {
        this.durationInWeeks = durationInWeeks;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CourseCategory getCategory() {
        return category;
    }

    public void setCategory(CourseCategory category) {
        this.category = category;
    }

    public List<User> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<User> attendants) {
        this.attendants = attendants;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public List<User> getFutureAttendants() {
        return futureAttendants;
    }

    public void setFutureAttendants(List<User> futureAttendants) {
        this.futureAttendants = futureAttendants;
    }

    public List<Homework> getHomework() {
        return homework;
    }

    public void setHomework(List<Homework> homework) {
        this.homework = homework;
    }
}
