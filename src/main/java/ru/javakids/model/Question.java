package ru.javakids.model;

import ru.javakids.util.OptionAttributeConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Entity
@NoArgsConstructor
public class Question implements Serializable {
  private static final long serialVersionUID = 1;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @NotNull
  @NotEmpty
  private String text;

  @Convert(converter = OptionAttributeConverter.class)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private ConcurrentHashMap<Long, Option> options = new ConcurrentHashMap<>(4);

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "lecture_id", referencedColumnName = "id")
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private Lecture lecture;
}
