package ru.javakids.controller;

import ru.javakids.model.Answer;
import ru.javakids.model.Question;
import ru.javakids.service.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Utility controller to add new questions quickly :P */
@RestController
@Slf4j
public class QuizController {

  @Autowired QuestionService questionService;

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

  @PostMapping(
      value = "/quizquestions/category/{categoryId}/answer",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public void checkAnswer(
      @RequestBody Answer answers, @PathVariable(value = "categoryId") Long categoryId) {
    log.info("Answer List : " + answers);
  }
}
