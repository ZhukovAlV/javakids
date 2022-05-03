package ru.javakids.model;

import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
  List<Category> categories;
}
