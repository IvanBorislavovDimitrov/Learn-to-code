package com.code.to.learn.api.model.course;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseBindingModel {

    @NotNull
    private String name;

    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

    @NotNull
    private int durationInWeeks;

    @NotNull
    private int credits;

    @NotNull
    private String formOfEducation;

    @NotNull
    @Min(0)
    private BigDecimal price;

    @NotNull
    private String description;

    @NotNull
    private String teacherName;

    private String categoryName;

    private List<String> videosNames;

    private List<MultipartFile> videos;

    private MultipartFile thumbnail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
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

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<MultipartFile> getVideos() {
        return videos;
    }

    public void setVideos(List<MultipartFile> videos) {
        this.videos = videos;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public MultipartFile getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(MultipartFile thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<String> getVideosNames() {
        return videosNames;
    }

    public void setVideosNames(List<String> videosNames) {
        this.videosNames = videosNames;
    }

    public Map<String, MultipartFile> getVideosToUpload() {
        if (videos.size() != videosNames.size()) {
            throw new IllegalArgumentException("Videos and videos' names do no match!");
        }
        Map<String, MultipartFile> videosToUpload = new HashMap<>();
        for (int i = 0; i < videosNames.size(); i++) {
            videosToUpload.put(videosNames.get(i), videos.get(i));
        }
        return videosToUpload;
    }
}
