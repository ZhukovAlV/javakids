package ru.javakids.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Controller
@Slf4j
public class MainErrorController implements ErrorController {

  @GetMapping("/error")
  public String handleError(Model model, HttpServletRequest httpRequest){
    Object status = httpRequest.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    String message = "Что-то пошло не так. Пожалуйста попробуйте в другое время.";
    if(status != null){
      int errorCode = Integer.parseInt(status.toString());
      log.info("Errorcode "+errorCode );
      switch (errorCode){
        case 403 : message = "Доступ к ресурсу закрыт"; break;
        case 404 : message = "Ресурс не найден"; break;
      }
    }
    model.addAttribute("msg", message);
    return "/error";
  }

  public String getErrorPath() {
    return "/error";
  }
}
