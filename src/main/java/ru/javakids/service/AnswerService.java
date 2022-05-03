package ru.javakids.service;

import ru.javakids.model.AnswerDto;
import ru.javakids.model.Result;

import java.util.List;

public interface AnswerService {

  List<Result> checkAnswer(AnswerDto answerDto);
}
