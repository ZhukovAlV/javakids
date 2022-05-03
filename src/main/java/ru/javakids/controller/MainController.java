package ru.javakids.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Slf4j
@PreAuthorize("isAuthenticated()")
public class MainController {

/*
  @Autowired QuestionService questionService;

  @Autowired CategoryService categoryService;

  @Autowired AnswerService answerService;
*/

  @PreAuthorize("permitAll()")
  @RequestMapping("/login")
  public String getLogin() {
    return "login";
  }

  @PreAuthorize("permitAll()")
  @GetMapping({"/", "/play"})
  public String getPlayQuiz(Model model, @RequestParam("category") Optional<Long> category) {
/*    model.addAttribute("categories", categoryService.findAll());
    if (category.isPresent()) {
      model.addAttribute("module", "play");
      List<Answer> answers =
          AnswerUtility.createAnswerList(questionService.findAll(category.get()));
      AnswerDto answerDto = new AnswerDto(answers, category.get());
      model.addAttribute("answerDto", answerDto);
      return "play";
    } else {
      model.addAttribute("module", "play");
      return "home";
    }*/
    return "home";
  }

/*  @PostMapping("/play")
  public String checkQuiz(Model model, @ModelAttribute("answerDto") AnswerDto answerDto) {
    answerDto.getAnswers().forEach(answer -> log.info(answer.toString()));
    List<Result> results = answerService.checkAnswer(answerDto);
    model.addAttribute("results", results);
    model.addAttribute("score", results.stream().filter(Result::isCorrect).count());
    return "result";
  }*/

}
