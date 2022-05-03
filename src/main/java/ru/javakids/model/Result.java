package ru.javakids.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result implements Serializable {
  private static final long serialVersionUID = 1;

  private String questionText;
  private String correctAnswer;
  private String selectedAnswer;
  private boolean correct;
}
