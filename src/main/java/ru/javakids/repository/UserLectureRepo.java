package ru.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javakids.model.UserLecture;

import java.util.Set;

@Repository
public interface UserLectureRepo extends CrudRepository<UserLecture, UserLecture.Id> {
    Set<UserLecture> findByIdUserId(Long userId);
    Set<UserLecture> findByIdLectureId(Long lectureId);
}
