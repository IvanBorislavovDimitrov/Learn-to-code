package com.code.to.learn.persistence.domain.model;

import com.code.to.learn.persistence.domain.entity.entity_enum.FormOfEducation;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CourseServiceModel extends GenericServiceModel {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int durationInWeeks;
    private int credits;
    private FormOfEducation formOfEducation;
    private BigDecimal price;
    private String description;
    private List<UserServiceModel> attendants;
    private UserServiceModel teacher;
    private List<UserServiceModel> futureAttendants;
    private List<HomeworkServiceModel> homework;
    private String videoName;
    private CourseCategoryServiceModel courseCategory;

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

    public List<UserServiceModel> getAttendants() {
        return attendants;
    }

    public void setAttendants(List<UserServiceModel> attendants) {
        this.attendants = attendants;
    }

    public UserServiceModel getTeacher() {
        return teacher;
    }

    public void setTeacher(UserServiceModel teacher) {
        this.teacher = teacher;
    }

    public List<UserServiceModel> getFutureAttendants() {
        return futureAttendants;
    }

    public void setFutureAttendants(List<UserServiceModel> futureAttendants) {
        this.futureAttendants = futureAttendants;
    }

    public List<HomeworkServiceModel> getHomework() {
        return homework;
    }

    public void setHomework(List<HomeworkServiceModel> homework) {
        this.homework = homework;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public CourseCategoryServiceModel getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategoryServiceModel courseCategory) {
        this.courseCategory = courseCategory;
    }
}
