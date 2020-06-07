package com.code.to.learn.persistence.domain.model;

import com.code.to.learn.persistence.domain.generic.NamedElement;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class CourseServiceModel extends IdServiceModel implements NamedElement {

    private String name;
    private LocalDate startDate;
    private BigDecimal price;
    private String description;
    private List<UserServiceModel> attendants;
    private UserServiceModel teacher;
    private List<UserServiceModel> futureAttendants;
    private List<CourseVideoServiceModel> videosNames;
    private CourseCategoryServiceModel courseCategory;
    private String thumbnailName;
    private double rating;
    private int ratingCount;
    private List<UserServiceModel> usersWhoHaveNotPaid;

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

    public List<CourseVideoServiceModel> getVideosNames() {
        return videosNames;
    }

    public void setVideosNames(List<CourseVideoServiceModel> videosNames) {
        this.videosNames = videosNames;
    }

    public CourseCategoryServiceModel getCourseCategory() {
        return courseCategory;
    }

    public void setCourseCategory(CourseCategoryServiceModel courseCategory) {
        this.courseCategory = courseCategory;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public List<UserServiceModel> getUsersWhoHaveNotPaid() {
        return usersWhoHaveNotPaid;
    }

    public void setUsersWhoHaveNotPaid(List<UserServiceModel> usersWhoHaveNotPaid) {
        this.usersWhoHaveNotPaid = usersWhoHaveNotPaid;
    }

    public static class CourseVideoServiceModel {
        private String videoTitle;
        private String videoFullName;
        private long videoFileSize;

        public CourseVideoServiceModel() {
        }

        public CourseVideoServiceModel(String videoTitle, String videoFullName, long videoFileSize) {
            this.videoTitle = videoTitle;
            this.videoFullName = videoFullName;
            this.videoFileSize = videoFileSize;
        }

        public String getVideoTitle() {
            return videoTitle;
        }

        public void setVideoTitle(String videoTitle) {
            this.videoTitle = videoTitle;
        }

        public String getVideoFullName() {
            return videoFullName;
        }

        public void setVideoFullName(String videoFullName) {
            this.videoFullName = videoFullName;
        }

        public long getVideoFileSize() {
            return videoFileSize;
        }

        public void setVideoFileSize(long videoFileSize) {
            this.videoFileSize = videoFileSize;
        }
    }
}
