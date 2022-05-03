package ru.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javakids.model.Lecture;

@Repository
public interface LectureRepo extends CrudRepository<Lecture, Long> {
}
