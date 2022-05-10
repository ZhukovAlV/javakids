package ru.javakids.service;

import ru.javakids.model.Question;
import ru.javakids.repository.QuestionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QuestionServiceImpl implements ru.javakids.service.QuestionService {

  @Autowired
  QuestionRepo questionRepository;

  @Override
  public void checkForErrorInNewQuestion(Question question, BindingResult bindingResult) {
    long correctCount =
        question.getOptions().entrySet().stream()
            .filter(
                integerOptionEntry -> {
                  return integerOptionEntry.getValue().isCorrect();
                })
            .count();
    if (correctCount > 1) {
      bindingResult.addError(new ObjectError("correctCount", "Only one option can be correct"));
    } else if (correctCount < 1) {
      bindingResult.addError(
          new ObjectError("correctCount", "Atleast one option should be correct"));
    }
    checkQuestionExistsForLecture(question, bindingResult);
  }

  @Override
  public void checkQuestionExistsForLecture(Question question, BindingResult bindingResult) {
    List<Question> questionList =
        questionRepository.findAllByLectureId(question.getLecture().getId());
    boolean questionExists =
        questionList.stream()
            .anyMatch(
                existingQuestion ->
                    existingQuestion.getText().equalsIgnoreCase(question.getText()));
    if (questionExists) {
      bindingResult.addError(
          new ObjectError(
              "questionerror",
              "Question already present in category " + question.getLecture().getTopic()));
    }
  }

  @Override
  public List<Question> saveAll(List<Question> questions) {
    return questionRepository.saveAll(questions);
  }

  @Override
  public Page<Question> findAll(int pageNumber, int size) {
    Pageable page = PageRequest.of(pageNumber, size);
    return questionRepository.findAll(page);
  }

  @Override
  public List<Question> findAll(Long categoryId) {
    return questionRepository.findAllByLectureId(categoryId);
  }

  @Override
  public Page<Question> findAll(Long categoryId,int pageNumber, int size) {
    Pageable page = PageRequest.of(pageNumber, size);
    return questionRepository.findAllByLectureId(categoryId,page);
  }

  @Override
  public List<Question> findAll() {
    return questionRepository.findAll();
  }

  @Override
  public Optional<Question> findById(Long questionId) {
    return questionRepository.findById(questionId);
  }

  @Override
  public Question save(Question question) {
    return questionRepository.saveAndFlush(question);
  }

  @Override
  public Question update(Question question) {
    Question existingQuestion =
        questionRepository
            .findById(question.getId())
            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
    existingQuestion.setText(question.getText());
    existingQuestion.setOptions(question.getOptions());
    existingQuestion.setLecture(question.getLecture());
    return questionRepository.save(existingQuestion);
  }

  @Override
  public void delete(Long questionId) {
    questionRepository.deleteById(questionId);
  }
}
