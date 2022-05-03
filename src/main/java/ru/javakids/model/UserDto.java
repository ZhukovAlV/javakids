package ru.javakids.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDto {
  @NotEmpty
  private String username;
  @NotEmpty
  private String name;
  @NotEmpty
  private String password;
  @NotEmpty
  private String confirmpassword;
}
