package ru.javakids.javakids.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.javakids.javakids.model.User;
import ru.javakids.javakids.model.UserDto;

import java.security.GeneralSecurityException;

public interface UserService extends UserDetailsService {
  User findByUsername(String username);
  User saveUser(UserDto userDto) throws GeneralSecurityException;
}
