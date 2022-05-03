package ru.javakids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javakids.model.*;
import ru.javakids.service.LectureService;
import ru.javakids.service.ReportExcelExporter;
import ru.javakids.service.UserLectureServiceImpl;
import ru.javakids.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class LectureController {

    private final LectureService lectureService;
    private final UserLectureServiceImpl userLectureService;
    private final UserService userService;

    @Autowired
    public LectureController(LectureService lectureService, UserLectureServiceImpl userLectureService, UserService userService) {
        this.lectureService = lectureService;
        this.userLectureService = userLectureService;
        this.userService = userService;
    }

    @GetMapping("/lectures")
    public String getAllLectures(Principal principal, Model model) {
        Set<Lecture> lectures = lectureService.getLectures();
        List<Lecture> lecturesList = new ArrayList<>(lectures);
        lecturesList.sort(Comparator.comparingLong(Lecture::getId));
        model.addAttribute("lectures", lecturesList);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
       // if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/lecture/list";
    }

    @GetMapping(value = "/lecture/{id}")
    public String getLectureById(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
       // if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
            Optional<UserLecture> userLectureOp =
                    userLectureService.getUserLectureById(
                            new UserLecture.Id(userActive.getId(), lecture.getId()));
            if (userLectureOp.isPresent()) {
                UserLecture userLecture = userLectureOp.get();
                userLecture.setStatus(userLectureService.getCorrectStatus(userLecture));
                userLectureService.updateUserLecture(userLecture, userLecture.getId());
                userLectureService.saveUserLecture(userLecture);
                model.addAttribute("userlecture", userLecture);
            }

        } else {
            return "/error/page";
        }

        return "lecture/detail";
    }

    @PostMapping("/lecture/{id}")
    public String finishStatusLecture(@PathVariable("id") Long lectureId, Principal principal) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<UserLecture> userLectureOp =
                userLectureService.getUserLectureById(
                        new UserLecture.Id(userActive.getId(), lectureId));
        if (userLectureOp.isPresent()) {
            UserLecture userLecture = userLectureOp.get();
            userLecture.setStatus(Status.FINISHED);
            userLectureService.saveUserLecture(userLecture);
        } else {
            return "/error/page";
        }
        return "redirect:/user/lectures";
    }

    @GetMapping("/lecture/create")
    public String createLecture(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
      //  if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "/lecture/add";
    }

    @PostMapping("/lecture/create")
    public String createLecture(Lecture lecture, Model model) {
        lectureService.saveLecture(lecture);
        model.addAttribute("lecture", lecture);
        return "redirect:/lectures";
    }

    @GetMapping("/lecture/{id}/update")
    public String updateLecture(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
        } else {
            return "/error/page";
        }

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
       // if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        return "/lecture/update";
    }

    @PostMapping("/lecture/{id}/update")
    public String updateLecture(@PathVariable("id") Long lectureId, Lecture lecture) {
        lectureService.updateLecture(lecture, lectureId);

        return "redirect:/lecture/" + lectureId;
    }

    //    @DeleteMapping("/lecture/{id}/delete") // не работает
    @RequestMapping(value = "/lecture/{id}/delete", method = {RequestMethod.GET, RequestMethod.POST})
    public String deleteLecture(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("lecture", lecture);
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lectureId);
            for (UserLecture userLecture : userLectures) {
                userLectureService.deleteUserLecture(userLecture.getId());
            }
            lectureService.deleteLecture(lectureId);
        } else {
            return "/error/page";
        }
        model.addAttribute("principal", userActive);
       // if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        return "/lecture/delete";
    }

    @GetMapping("/lecture/{id}/users")
    public String getUserLectures(Principal principal, Model model, @PathVariable("id") Long lectureId) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Optional<Lecture> lectureOp = lectureService.getLectureById(lectureId);
        if (lectureOp.isPresent()) {
            Lecture lecture = lectureOp.get();
            model.addAttribute("currentlecture", lecture);
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lectureId);
            List<UserLecture> userLecturesList = new ArrayList<>(userLectures);
            userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getUser().getId()));
            model.addAttribute("userlectures", userLecturesList);
        } else {
            return "/error/page";
        }

        model.addAttribute("principal", userActive);
      //  if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        return "/lecture/users";
    }

    @GetMapping("/lectures/users")
    public String getUserLecturesList(Principal principal, Model model) {
        Set<Lecture> lectures = lectureService.getLectures();
        Set<UserLecture> allUserLectures = new HashSet<>();
        for (Lecture lecture : lectures) {
            Set<UserLecture> userLectures = userLectureService.getUserLecturesByLectureId(lecture.getId());
            allUserLectures.addAll(userLectures);
        }
        List<UserLecture> userLecturesList = new ArrayList<>(allUserLectures);
        userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        model.addAttribute("userlectures", userLecturesList);

        User userActive = (User) userService.loadUserByUsername(principal.getName());
        model.addAttribute("principal", userActive);
      //  if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        return "/lecture/users";
    }

    @GetMapping("/lectures/export/excel")
    public void exportToExcelLectures(HttpServletResponse response) throws IOException {
        configureResponse(response, "lectures");

        Set<Lecture> lectures = lectureService.getLectures();
        List<Lecture> lecturesList = new ArrayList<>(lectures);
        lecturesList.sort(Comparator.comparingLong(Lecture::getId));

        ReportExcelExporter excelExporter = new ReportExcelExporter(lecturesList, "Lectures");
        excelExporter.exportUsers(response, "lectures");
    }

    private void configureResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename +"_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }

    @GetMapping("/user/lectures")
    public String getMyLectures(Principal principal, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
        Set<UserLecture> myLectures = userLectureService.getUserLecturesByUserId(userActive);
        userActive.setUserLectures(myLectures);
        List<UserLecture> myLecturesList = new ArrayList<>(myLectures);
        myLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
        model.addAttribute("myLectures", myLecturesList);
        model.addAttribute("principal", userActive);
       // if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);

        return "user/lectures";
    }

    @GetMapping("/user/{id}/lectures")
    public String getUserLectures(Principal principal, @PathVariable Long id, Model model) {
        User userActive = (User) userService.loadUserByUsername(principal.getName());
      //  if (userActive.getRoles().contains(Role.ROLE_ADMIN)) model.addAttribute("master", Role.ROLE_ADMIN);
        model.addAttribute("principal", userActive);

        User user = userService.loadUserById(id);
        if (user == null) {
            return "/error/page";
        } else {
            model.addAttribute("user", user);

            Set<UserLecture> userLectures = userLectureService.getUserLecturesByUserId(user);
            user.setUserLectures(userLectures);
            List<UserLecture> userLecturesList = new ArrayList<>(userLectures);
            userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));
            model.addAttribute("userLectures", userLecturesList);

            return "user/userlectures";
        }
    }
}