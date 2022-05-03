package ru.javakids.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Lecture {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String topic;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @OneToMany(mappedBy = "lecture")
    protected Set<UserLecture> userLectures  = new HashSet<>();
}
