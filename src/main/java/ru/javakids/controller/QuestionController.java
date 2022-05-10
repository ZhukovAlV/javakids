package ru.javakids.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.javakids.model.Option;
import ru.javakids.model.Question;
import ru.javakids.service.LectureService;
import ru.javakids.service.QuestionService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class QuestionController {

  @Autowired
  QuestionService questionService;

  @Autowired
  LectureService lectureService;

  @GetMapping({"/questions"})
  public String getHome(
      Model model,
      @RequestParam("page") Optional<Integer> page,
      @RequestParam("size") Optional<Integer> size,
      @RequestParam("lecture") Optional<Long> lecture) {
    int currentPage = page.orElse(1);
    int pageSize = size.orElse(8);
    if (lecture.isPresent()) {
      model.addAttribute(
          "questions", questionService.findAll(lecture.get(), currentPage - 1, pageSize));
    } else {
      model.addAttribute("questions", questionService.findAll(currentPage - 1, pageSize));
    }
    model.addAttribute("module", "allquestions");
    model.addAttribute("lectures", lectureService.getLectures());
    return "/quiz/question_list";
  }

  @GetMapping({"/question"})
  public String getAddNewQuiz(
      @ModelAttribute("question") Question question, Model model, Principal principal) {
    question.getOptions().put(1L, new Option());
    question.getOptions().put(2L, new Option());
    question.getOptions().put(3L, new Option());
    question.getOptions().put(4L, new Option());
    model.addAttribute("question", question);
    model.addAttribute("module", "newquestion");
    model.addAttribute("lectures", lectureService.getLectures());
    return "/quiz/question_new";
  }

  @PostMapping("/question")
  public String addNewQuiz(
      @Valid @ModelAttribute("question") Question question,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {
    questionService.checkQuestionExistsForLecture(question, bindingResult);
    if (bindingResult.hasErrors()) {
      model.addAttribute("lectures", lectureService.getLectures());
      return "/quiz/question_new";
    }
    questionService.save(question);
    redirectAttributes.addFlashAttribute("added", true);
    return "redirect:/question";
  }

  @PostMapping("/question/delete")
  public String deleteQuestion(
      @ModelAttribute(value = "quesToDelete") Question quesToDelete,
      RedirectAttributes redirectAttributes,
      @RequestParam("page") Optional<Integer> page) {
    int currentPage = page.orElse(1);
    questionService.delete(quesToDelete.getId());
    redirectAttributes.addFlashAttribute("questionDeleted", true);
    return "redirect:/questions?page=" + currentPage;
  }
}
