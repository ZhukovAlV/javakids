package ru.javakids.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javakids.model.Lecture;
import ru.javakids.model.Status;
import ru.javakids.model.User;
import ru.javakids.model.UserLecture;
import ru.javakids.repository.LectureRepo;
import ru.javakids.repository.UserLectureRepo;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserLectureServiceImpl implements UserLectureService{

    private final UserLectureRepo userLectureRepo;
    private final LectureRepo lectureRepo;

    @Autowired
    public UserLectureServiceImpl(UserLectureRepo userLectureRepo, LectureRepo lectureRepo) {
        this.userLectureRepo = userLectureRepo;
        this.lectureRepo = lectureRepo;
    }

    @Override
    public Set<UserLecture> getUserLectures() {
        Set<UserLecture> result = new HashSet<>();
        userLectureRepo.findAll().forEach(result::add);
        return result;
    }

    @Override
    public Set<UserLecture> getUserLecturesByUserId(User user) {
        Set<UserLecture> result = new HashSet<>(userLectureRepo.findByIdUserId(user.getId()));

        Set<Lecture> lectures = new HashSet<>();
        lectureRepo.findAll().forEach(lectures::add);

        for (Lecture lecture : lectures) {
            Optional<UserLecture> userLectureOp = getUserLectureById(new UserLecture.Id(user.getId(), lecture.getId()));
            if (userLectureOp.isPresent()) {
                UserLecture userLecture = userLectureOp.get();
                userLecture.setStatus(userLecture.getStatus());
                saveUserLecture(userLecture);
            } else {
                UserLecture userLecture = new UserLecture(user, lecture, Status.NOT_STARTED);
                result.add(userLecture);
                userLectureRepo.save(userLecture);
            }
        }

        return result;
    }

    @Override
    public Set<UserLecture> getUserLecturesByLectureId(Long lectureId) {
        return new HashSet<>(userLectureRepo.findByIdLectureId(lectureId));
    }

    @Override
    public Optional<UserLecture> getUserLectureById(UserLecture.Id id) {
        return userLectureRepo.findById(id);
    }

    @Override
    @Transactional
    public void saveUserLecture(UserLecture userLecture) {
        userLectureRepo.save(userLecture);
    }

    @Override
    @Transactional
    public void updateUserLecture(UserLecture userLecture, UserLecture.Id id) {
        Optional<UserLecture> userLectureOp = userLectureRepo.findById(id);

        if (userLectureOp.isPresent()) {
            UserLecture userLectureEntity = userLectureOp.get();
            userLectureEntity.setStatus(userLecture.getStatus());
        }
    }

    @Override
    @Transactional
    public void deleteUserLecture(UserLecture.Id id) {
        userLectureRepo.deleteById(id);
    }

    @Override
    public Status getCorrectStatus(UserLecture userLecture) {
        Status currentStatus = userLecture.getStatus();
        if (currentStatus == null) {
            return Status.NOT_STARTED;
        } else if (currentStatus == Status.FINISHED) {
            return Status.FINISHED;
        }
        return Status.IN_PROCESS;
    }
}
