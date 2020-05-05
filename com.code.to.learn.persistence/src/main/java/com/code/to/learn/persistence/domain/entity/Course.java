package com.code.to.learn.persistence.domain.entity;

import com.code.to.learn.persistence.domain.entity.entity_enum.FormOfEducation;
import com.code.to.learn.persistence.domain.generic.NamedElement;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course extends IdEntity<Course> implements NamedElement {

    public static final String START_DATE = "startDate";
    public static final String TEACHER = "teacher";
    public static final String ATTENDANTS = "attendants";
    public static final String COMMENTS = "comments";

    @Column(name = NAME, nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int durationInWeeks;

    @Column(nullable = false)
    private int credits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormOfEducation formOfEducation;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    @Lob
    private String description;

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = ID))
    private List<CourseVideo> videosNames;

    @Column(nullable = false)
    private String thumbnailName;

    @ManyToOne(targetEntity = CourseCategory.class, fetch = FetchType.EAGER, optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "category_id", nullable = false)
    private CourseCategory category;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attendant_id", referencedColumnName = "id"))
    private List<User> attendants = new ArrayList<>();

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "teacher_id", nullable = false)
    private User teacher;

    @ManyToMany(targetEntity = User.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(name = "future_attendants_courses", joinColumns = @JoinColumn(name = "course_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "future_attendants_id", referencedColumnName = "id"))
    private List<User> futureAttendants = new ArrayList<>();

    @OneToMany(targetEntity = Homework.class, fetch = FetchType.LAZY, mappedBy = "course", cascade = CascadeType.MERGE)
    private List<Homework> homework = new ArrayList<>();

    @OneToMany(mappedBy = "course", targetEntity = Comment.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Comment> comments = new ArrayList<>();

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

    public List<CourseVideo> getVideosNames() {
        return videosNames;
    }

    public void setVideosNames(List<CourseVideo> videosNames) {
        this.videosNames = videosNames;
    }

    public String getThumbnailName() {
        return thumbnailName;
    }

    public void setThumbnailName(String thumbnailName) {
        this.thumbnailName = thumbnailName;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
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
        setVideosNames(course.getVideosNames());
        setThumbnailName(course.getThumbnailName());
        setComments(course.getComments());
        return this;
    }

    @Embeddable
    public static class CourseVideo {

        @Column(nullable = false)
        private String videoTitle;
        @Column(nullable = false)
        private String videoFullName;
        @Column
        private Long videoFileSize;

        public CourseVideo() {
        }

        public CourseVideo(String videoTitle, String videoFullName, long videoFileSize) {
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

        public Long getVideoFileSize() {
            return videoFileSize;
        }

        public void setVideoFileSize(Long videoFileSize) {
            this.videoFileSize = videoFileSize;
        }
    }
}
