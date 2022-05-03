package ru.javakids.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer implements Serializable {
  private static final long serialVersionUID = 1;

  private Question question;
  private Long selectedOption;
}
