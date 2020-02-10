package com.code.to.learn.api.model.user;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CourseBindingModel {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private int durationInWeeks;
    private int credits;
    private String formOfEducation;
    private BigDecimal price;
    private String description;

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

    public String getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(String formOfEducation) {
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
}
