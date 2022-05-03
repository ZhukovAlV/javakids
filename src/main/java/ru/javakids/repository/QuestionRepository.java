package ru.javakids.repository;

import ru.javakids.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
  List<Question> findAllByCategoryId(Long categoryId);

  Page<Question> findAllByCategoryId(Long category, Pageable page);
}
