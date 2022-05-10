package ru.javakids.service;

import ru.javakids.model.Lecture;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface LectureService {

    List<Lecture> getLectures();

    Optional<Lecture> getLectureById(Long id);

    void saveLecture(Lecture lecture);

    void updateLecture(Lecture lecture, Long id);

    void deleteLecture(Long id);

}
