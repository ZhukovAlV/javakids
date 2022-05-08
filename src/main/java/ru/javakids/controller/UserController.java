package ru.javakids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import ru.javakids.model.User;
import ru.javakids.model.UserDto;
import ru.javakids.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

  @Autowired
  UserService userService;

  @ModelAttribute("/user")
  public UserDto userDto(){
    return new UserDto();
  }

  /**
   * URL страницы создания нового пользователя
   * @return страница создания нового пользователя
   */
  @GetMapping("/user/add")
  public String getNewUser(){
    return "/user/add";
  }

  /**
   *
   * @param userDto Пользователь
   * @param result Результат аутентификации
   * @param request Запрос Http
   * @return Окно создания нового пользователя или главная страница
   */
  @PostMapping("/user/add")
  public String saveUser(@ModelAttribute("user") @Valid UserDto userDto, BindingResult result, HttpServletRequest request){
    if(!userDto.getPassword().equals(userDto.getConfirmpassword())){
      result.rejectValue("username", Errors.NESTED_PATH_SEPARATOR,"Пароли не совпадают" );
    }

    User existingUser = userService.findByUsername(userDto.getUsername());
    if(existingUser != null){
      result.rejectValue("username", Errors.NESTED_PATH_SEPARATOR,"Пользователь уже существует" );
    }
    if(result.hasErrors()){
      return "/user/add";
    }
    try {
      User user = userService.saveUser(userDto);
      request.login(userDto.getUsername(), userDto.getPassword());
      return "redirect:/";
    } catch (GeneralSecurityException e) {
      result.rejectValue("password", Errors.NESTED_PATH_SEPARATOR,"Не верный пароль" );
      return "/user/add";
    } catch (ServletException e) {
      result.addError(
          new ObjectError("loginError", "Ошибка в имени пользователя"));
      return "/user/add";
    }
  }

  /**
   *
   * @param principal Пользователь
   * @param model Информация по пользователю для отображения
   * @return URL user/detail
   */
  @GetMapping("/user/detail")
  public String getUserDetail(Principal principal, Model model) {
    User userActive = (User) userService.loadUserByUsername(principal.getName());
    model.addAttribute("user", userActive);
    return "/user/detail";
  }

  /**
   *
   * @param principal Пользователь
   * @param model Информация по пользователю для отображения
   * @return URL user/update
   */
  @GetMapping("/user/update")
  public String updateUser(Principal principal, Model model) {
    User userActive = (User) userService.loadUserByUsernameWithDecryptionPassword(principal.getName());
    model.addAttribute("user", userActive);
    return "/user/update";
  }

  /**
   *
   * @param userDto Пользователь
   * @param id ID пользователя
   * @return Страница с обновленным пользователем
   */
  @PostMapping("/user/{id}/update")
  public String updateUser(@ModelAttribute("user") @Valid UserDto userDto, @PathVariable Long id) throws GeneralSecurityException {
    userService.updateUser(id, userDto);
    return "redirect:/user/detail";
  }

  /**
   * Возвращает всех пользователей
   * @param model Модель для списка пользователей
   * @return URL user/list
   */
  @GetMapping("/users")
  public String getUsersList(Model model){
    List<User> users = userService.getUsersList();
    model.addAttribute("users", users);
    return "/user/list";
  }
}
