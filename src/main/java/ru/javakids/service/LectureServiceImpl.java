package ru.javakids.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javakids.model.Lecture;
import ru.javakids.repository.LectureRepo;

import java.util.*;

@Service
public class LectureServiceImpl implements LectureService {

    private final LectureRepo lectureRepo;

    @Autowired
    public LectureServiceImpl(LectureRepo lectureRepo) {
        this.lectureRepo = lectureRepo;
    }

    @Override
    public List<Lecture> getLectures() {
        Set<Lecture> result = new HashSet<>();
        lectureRepo.findAll().forEach(result::add);

        // Сортируем лекции и добавляем в model
        List<Lecture> lecturesList = new ArrayList<>(result);
        lecturesList.sort(Comparator.comparingLong(Lecture::getId));

        return lecturesList;
    }

    @Override
    public Optional<Lecture> getLectureById(Long id) {
        return lectureRepo.findById(id);
    }

    @Transactional
    @Override
    public void saveLecture(Lecture lecture) {
        lectureRepo.save(lecture);
    }

    @Transactional
    @Override
    public void updateLecture(Lecture lecture, Long id) {
        Optional<Lecture> lectureOp = lectureRepo.findById(id);

        if (lectureOp.isPresent()) {
            Lecture lectureEntity = lectureOp.get();
            lectureEntity.setTopic(lecture.getTopic());
            lectureEntity.setContent(lecture.getContent());
        }
    }

    @Override
    public void deleteLecture(Long id) {
        lectureRepo.deleteById(id);
    }

}
