package ru.javakids.service;

import ru.javakids.model.Question;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface QuestionService {

  /**
   * Find all by filter
   *
   * @param pageNumber pageNumber
   * @param size size
   * @return Page
   */
  Page<Question> findAll(int pageNumber, int size);

  /**
   * Find all
   *
   * @param categoryId category id
   * @return List
   */
  List<Question> findAll(Long categoryId);

  /**
   * Find all by category and page
   *
   * @param categoryId
   * @return
   */
  Page<Question> findAll(Long categoryId,int pageNumber, int size);

  /**
   * Find all
   *
   * @return List
   */
  List<Question> findAll();
  /**
   * Find questions by id
   *
   * @param questionId question id
   * @return Optional
   */
  Optional<Question> findById(Long questionId);

  /**
   * Save question
   *
   * @param question question
   * @return Question
   */
  Question save(Question question);

  /**
   * Save all questions
   *
   * @param questions questions
   * @return List
   */
  List<Question> saveAll(List<Question> questions);

  /**
   * Update question
   *
   * @param question question
   * @return Question
   */
  Question update(Question question);

  /**
   * Delete question
   *
   * @param questionId question id
   */
  void delete(Long questionId);

  /**
   * Check if the question exits for a category
   *
   * @param question question
   * @param bindingResult binding result
   */
  void checkQuestionExistsForLecture(Question question, BindingResult bindingResult);

  void checkForErrorInNewQuestion(Question question, BindingResult bindingResult);
}
