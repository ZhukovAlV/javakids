package ru.javakids.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.javakids.model.Lecture;
import ru.javakids.model.User;
import ru.javakids.model.UserLecture;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ReportExcelExporter {
    private final XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<User> users;
    private List<Lecture> lectures;
    private List<UserLecture> userLectures;
    private String description;

    public <T> ReportExcelExporter(List<T> list, String description) {
        workbook = new XSSFWorkbook();
        this.description = description;
        switch (description) {
            case "Users":
                this.users = (List<User>) list;
                break;
            case "Lectures":
                this.lectures = (List<Lecture>) list;
                break;
            case "UserLectures":
                this.userLectures = (List<UserLecture>) list;
                break;
        }
    }

//    public ReportExcelExporter(Set<UserLecture> userLectures) {
//        this.userLectures = userLectures;
//        workbook = new XSSFWorkbook();
//    }

//    public ReportExcelExporter(List<User> listUsers) {
//        this.users = listUsers;
//        workbook = new XSSFWorkbook();
//    }
//
//    public ReportExcelExporter(List<Lecture> lectures, String description) {
//        this.lectures = lectures;
//        this.description = description;
//        workbook = new XSSFWorkbook();
//    }

    private void writeHeaderLineUsers() {
        sheet = workbook.createSheet(description);
        Row row = sheet.createRow(0);
        CellStyle style = getCellStyleHeaderLine();
        createCell(row, 0, "Id пользователя", style);
        //createCell(row, 1, "Roles", style);
        createCell(row, 2, "username", style);
        createCell(row, 3, "E-mail", style);
    }

    private void writeHeaderLineLectures() {
        sheet = workbook.createSheet(description);
        Row row = sheet.createRow(0);
        CellStyle style = getCellStyleHeaderLine();
        createCell(row, 0, "Id лекции", style);
        createCell(row, 1, "Тема лекции", style);
    }

    private void writeHeaderLineUserLectures() {
        sheet = workbook.createSheet(description);
        Row row = sheet.createRow(0);
        CellStyle style = getCellStyleHeaderLine();
        createCell(row, 0, "Id лекции", style);
        createCell(row, 1, "Тема лекции", style);
        createCell(row, 2, "Id пользователя", style);
        createCell(row, 3, "Имя пользователя", style);
        createCell(row, 4, "Статус лекции", style);
    }

    private CellStyle getCellStyleHeaderLine() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLinesUsers() {
        int rowCount = 1;
        CellStyle style = getCellStyleDataLines();

        for (User user : users) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, user.getId(), style);
            //createCell(row, columnCount++, user.getRoles().toString(), style);
            createCell(row, columnCount++, user.getUsername(), style);
            createCell(row, columnCount, user.getEmail(), style);
        }
    }

    private void writeDataLinesLectures() {
        int rowCount = 1;
        CellStyle style = getCellStyleDataLines();

        for (Lecture lecture : lectures) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, lecture.getId(), style);
            createCell(row, columnCount, lecture.getTopic(), style);
        }
    }

    private void writeDataLinesUserLectures() {
        int rowCount = 1;
        CellStyle style = getCellStyleDataLines();

        for (UserLecture userLecture : userLectures) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, userLecture.getLecture().getId(), style);
            createCell(row, columnCount++, userLecture.getLecture().getTopic(), style);
            createCell(row, columnCount++, userLecture.getUser().getId(), style);
            createCell(row, columnCount++, userLecture.getUser().getUsername(), style);
            createCell(row, columnCount, userLecture.getStatus().toString(), style);
        }
    }

    private CellStyle getCellStyleDataLines() {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setIndention((short) 2);
        return style;
    }

    public void exportUsers(HttpServletResponse response, String report) throws IOException {
        switch (report) {
            case "users":
                writeHeaderLineUsers();
                writeDataLinesUsers();
                break;
            case "lectures":
                writeHeaderLineLectures();
                writeDataLinesLectures();
                break;
            case "userLectures":
                writeHeaderLineUserLectures();
                writeDataLinesUserLectures();
                break;
        }
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}

