package ru.javakids.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
@PreAuthorize("isAuthenticated()")
public class MainController {

  @PreAuthorize("permitAll()")
  @RequestMapping("/login")
  public String getLogin() {
    return "user/login";
  }

  @PreAuthorize("permitAll()")
  @GetMapping({"/","home"})
  public String getMainPage() {
    return "home";
  }
}
