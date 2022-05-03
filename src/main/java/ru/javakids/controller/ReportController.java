package ru.javakids.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.javakids.model.User;
import ru.javakids.model.UserLecture;
import ru.javakids.service.LectureService;
import ru.javakids.service.ReportExcelExporter;
import ru.javakids.service.UserLectureServiceImpl;
import ru.javakids.service.UserService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ReportController {

    private final LectureService lectureService;
    private final UserLectureServiceImpl userLectureService;
    private final UserService userService;

    @Autowired
    public ReportController(LectureService lectureService, UserLectureServiceImpl userLectureService, UserService userService) {
        this.lectureService = lectureService;
        this.userLectureService = userLectureService;
        this.userService = userService;
    }

    @GetMapping("/users/export/excel")
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> users = userService.getUsersList();

        ReportExcelExporter excelExporter = new ReportExcelExporter(users, "Users");

        excelExporter.exportUsers(response, "users");
    }

    @GetMapping("/lectures/users/export/excel")
    public void exportToExcelUserLectures(HttpServletResponse response) throws IOException {
        configureResponse(response, "userlectures");

        Set<UserLecture> allUserLectures = userLectureService.getUserLectures();
        List<UserLecture> userLecturesList = new ArrayList<>(allUserLectures);
        userLecturesList.sort(Comparator.comparingLong(userLecture -> userLecture.getLecture().getId()));

        ReportExcelExporter excelExporter = new ReportExcelExporter(userLecturesList, "UserLectures");
        excelExporter.exportUsers(response, "userLectures");
    }

    private void configureResponse(HttpServletResponse response, String filename) {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + filename +"_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);
    }
}
