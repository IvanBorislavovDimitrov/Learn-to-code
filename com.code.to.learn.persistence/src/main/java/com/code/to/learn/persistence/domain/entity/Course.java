package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.generic.NamedElement;
import com.code.to.learn.persistence.domain.entity.entity_enum.FormOfEducation;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course extends IdEntity<Course> implements NamedElement {

    private static final String START_DATE = "start_date";
    private static final String END_DATE = "end_date";
    private static final String DURATION_IN_WEEKS = "duration_in_weeks";
    private static final String CREDITS = "credits";
    private static final String FORM_OF_EDUCATION = "formOfEducation";
    private static final String PRICE = "price";
    private static final String DESCRIPTION = "description";
    private static final String VIDEO_NAME = "video_name";

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

    @Column(name = DESCRIPTION, nullable = false)
    @Lob
    private String description;

    @Column(name = VIDEO_NAME, nullable = false)
    private String videoName;

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory category;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attendant_id", referencedColumnName = "id"))
    private List<User> attendants = new ArrayList<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "future_attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "future_attendants_id", referencedColumnName = "id"))
    private List<User> futureAttendants = new ArrayList<>();

    @OneToMany(targetEntity = Homework.class, fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.ALL)
    private List<Homework> homework = new ArrayList<>();

    @Override
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

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    @Override
    public Course merge(Course course) {
        setName(course.getName());
        setStartDate(course.getStartDate());
        setEndDate(course.getEndDate());
        setDurationInWeeks(course.getDurationInWeeks());
        setCredits(course.getCredits());
        setFormOfEducation(course.getFormOfEducation());
        setPrice(course.getPrice());
        setDescription(course.getDescription());
        setCategory(course.getCategory());
        setAttendants(course.getAttendants());
        setTeacher(course.getTeacher());
        setFutureAttendants(course.getFutureAttendants());
        setHomework(course.getHomework());
        setVideoName(course.getVideoName());
        return this;
    }
}
