package ru.javakids.controller;

import ru.javakids.model.Answer;
import ru.javakids.model.Category;
import ru.javakids.model.Question;
import ru.javakids.service.CategoryService;
import ru.javakids.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Utility controller to add new questions quickly :P */
@RestController
@Slf4j
public class QuizRestController {

  @Autowired QuestionService questionService;

  @Autowired CategoryService categoryService;

  @GetMapping("/quizquestions")
  public List<Question> getAllQuestionRest(
      @RequestParam(value = "category", required = false) Long categoryId) {
    return questionService.findAll();
  }

  @PostMapping(
      value = "/quizquestions",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Question> saveAllQuestions(@RequestBody List<Question> questions) {
    return questionService.saveAll(questions);
  }

  // Should be included in its own file if more methods are included! SOLID
  @PostMapping(
      value = "/quizquestions/category",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public Category saveCategory(@RequestBody Category category) {
    return categoryService.save(category);
  }

  @PostMapping(
      value = "/quizquestions/category/{categoryId}/answer",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void checkAnswer(
      @RequestBody Answer answers, @PathVariable(value = "categoryId") Long categoryId) {
    log.info("Answer List : " + answers);
  }
}
