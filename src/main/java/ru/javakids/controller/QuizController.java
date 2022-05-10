package ru.javakids.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javakids.model.Answer;
import ru.javakids.model.AnswerDto;
import ru.javakids.model.Lecture;
import ru.javakids.model.Result;
import ru.javakids.service.AnswerService;
import ru.javakids.service.LectureService;
import ru.javakids.service.QuestionService;
import ru.javakids.util.AnswerUtility;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class QuizController {

  @Autowired
  QuestionService questionService;

  @Autowired
  LectureService lectureService;

  @Autowired
  AnswerService answerService;

/*  @GetMapping("/quizquestions")
  public List<Question> getAllQuestionRest(
      @RequestParam(value = "category", required = false) Long categoryId) {
    return questionService.findAll();
  }*/

/*  @PostMapping(
      value = "/quizquestions",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Question> saveAllQuestions(@RequestBody List<Question> questions) {
    return questionService.saveAll(questions);
  }*/

/*  @PostMapping(
          value = "/quizquestions/category/{categoryId}/answer",
          consumes = MediaType.APPLICATION_JSON_VALUE,
          produces = MediaType.APPLICATION_JSON_VALUE)
  public void checkAnswer(
          @RequestBody Answer answers, @PathVariable(value = "categoryId") Long categoryId) {
    log.info("Answer List : " + answers);
  }*/

  /**
   *
   * @param model Модель для списка тестов
   * @param id ID лекции
   * @return Страницу с тестом
   */
  @GetMapping("/quiz/{id}")
  public String playQuiz(Model model, @PathVariable Long id) {
    Optional<Lecture> lecture = lectureService.getLectureById(id);
    model.addAttribute("lecture", lecture);
    if (lecture.isPresent()) {
      model.addAttribute("module", "play");
      List<Answer> answers =
              AnswerUtility.createAnswerList(questionService.findAll(lecture.get().getId()));
      AnswerDto answerDto = new AnswerDto(answers, lecture.get().getId());
      model.addAttribute("answerDto", answerDto);
      return "/quiz/play";
    } else {
      model.addAttribute("module", "play");
      return "home";
    }
  }

  @PostMapping("/play")
  public String checkQuiz(Model model, @ModelAttribute("answerDto") AnswerDto answerDto) {
    answerDto.getAnswers().forEach(answer -> log.info(answer.toString()));
    List<Result> results = answerService.checkAnswer(answerDto);
    model.addAttribute("results", results);
    model.addAttribute("score", results.stream().filter(Result::isCorrect).count());
    return "/quiz/result";
  }
}
