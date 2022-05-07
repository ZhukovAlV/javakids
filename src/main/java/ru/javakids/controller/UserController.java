package ru.javakids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javakids.model.User;
import ru.javakids.model.UserDto;
import ru.javakids.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.GeneralSecurityException;

@Controller
@RequestMapping("/user")
public class UserController {

  @Autowired
  UserService userService;

  @ModelAttribute("user")
  public UserDto userDto(){
    return new UserDto();
  }

  @GetMapping
  public String getNewUser(Model model){
    return "user/add";
  }

  @PostMapping
  public String saveUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, HttpServletRequest request){
    if(!userDto.getPassword().equals(userDto.getConfirmpassword())){
      result.rejectValue("username", Errors.NESTED_PATH_SEPARATOR,"Пароли не совпадают" );
    }

    User existingUser = userService.findByUsername(userDto.getUsername());
    if(existingUser != null){
      result.rejectValue("username", Errors.NESTED_PATH_SEPARATOR,"Пользователь уже существует" );
    }
    if(result.hasErrors()){
      return "user/add";
    }
    try {
      User user = userService.saveUser(userDto);
      request.login(userDto.getUsername(), userDto.getPassword());
      return "redirect:/";
    } catch (GeneralSecurityException e) {
      result.rejectValue("password", Errors.NESTED_PATH_SEPARATOR,"Не верный пароль" );
      return "user/add";
    } catch (ServletException e) {
      result.addError(
          new ObjectError("loginError", "Ошибка в имени пользователя"));
      return "user/add";
    }
  }

}
