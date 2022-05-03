package ru.javakids.model;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "userlectures_table")
public class UserLecture {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected Id id = new Id();

    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    protected Status status;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            insertable = false, updatable = false)
    protected User user;

    @ManyToOne
    @JoinColumn(
            name = "lecture_id",
            insertable = false, updatable = false)
    protected Lecture lecture;

    @ManyToOne
    @JoinColumn(name= "grade_id")
    protected Grade grade;

    public UserLecture(User user, Lecture lecture, Status status) {
        this.user = user;
        this.lecture = lecture;
        this.status = status;

        this.id.userId = user.getId();
        this.id.lectureId = lecture.getId();
        user.getUserLectures().add(this);
        lecture.getUserLectures().add(this);
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    public static class Id implements Serializable {

        @Column(name = "user_id")
        protected Long userId;

        @Column(name = "lecture_id")
        protected Long lectureId;

        public Id(Long userId, Long lectureId) {
            this.userId = userId;
            this.lectureId = lectureId;
        }
    }
}
