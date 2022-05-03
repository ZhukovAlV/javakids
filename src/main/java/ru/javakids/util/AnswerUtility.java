package ru.javakids.util;

import ru.javakids.model.Answer;
import ru.javakids.model.Question;

import java.util.ArrayList;
import java.util.List;

public class AnswerUtility {

  public static List<Answer> createAnswerList(List<Question> questionList) {
    List<Answer> answerList = new ArrayList<>(questionList.size());
    questionList.forEach(
        question -> {
          answerList.add(new Answer(question, -1L));
        });
    return answerList;
  }
}
