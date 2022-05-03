package ru.javakids.service;

import ru.javakids.model.Status;
import ru.javakids.model.User;
import ru.javakids.model.UserLecture;

import java.util.Optional;
import java.util.Set;

public interface UserLectureService {

    Set<UserLecture> getUserLectures();

    Set<UserLecture> getUserLecturesByUserId(User user);

    Set<UserLecture> getUserLecturesByLectureId(Long lectureId);

    Optional<UserLecture> getUserLectureById(UserLecture.Id id);

    void saveUserLecture(UserLecture userLecture);

    void updateUserLecture(UserLecture userLecture, UserLecture.Id id);

    void deleteUserLecture(UserLecture.Id id);

    Status getCorrectStatus(UserLecture userLecture);
}
