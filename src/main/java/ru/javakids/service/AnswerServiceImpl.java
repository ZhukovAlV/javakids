package ru.javakids.service;

import ru.javakids.model.AnswerDto;
import ru.javakids.model.Option;
import ru.javakids.model.Question;
import ru.javakids.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

  @Autowired QuestionService questionService;

  @Override
  public List<Result> checkAnswer(AnswerDto answerDto) {
    List<Result> results = new ArrayList<>();
    Long categoryId = answerDto.getLectureId();

    answerDto
        .getAnswers()
        .forEach(
            answer -> {
              log.info(answer.toString());
              Question question =
                  questionService.findById(answer.getQuestion().getId()).orElse(null);
              if (question != null) {
                log.info(question.toString());
                Option correctOption =
                    question.getOptions().values().stream()
                        .filter(Option::isCorrect)
                        .findFirst()
                        .orElse(new Option());
                Option selectedOption;
                if (answer.getSelectedOption() != -1) {
                   selectedOption = question.getOptions().get(answer.getSelectedOption());
                }else{
                  selectedOption = new Option();
                }
                Result result = new Result();
                result.setQuestionText(question.getText());
                result.setSelectedAnswer(selectedOption.getText());
                result.setCorrectAnswer(correctOption.getText());
                result.setCorrect(selectedOption.isCorrect());
                results.add(result);
              }
            });
    return results;
  }
}
