package ru.javakids.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.javakids.model.User;
import ru.javakids.model.UserDto;

import java.security.GeneralSecurityException;
import java.util.List;

public interface UserService extends UserDetailsService {

  User loadUserById(Long id);

  User findByUsername(String username);
  UserDetails loadUserByUsernameWithDecryptionPassword(String username) throws UsernameNotFoundException;

  User saveUser(UserDto userDto) throws GeneralSecurityException;

  User updateUser(Long id, UserDto userDto) throws GeneralSecurityException;

  List<User> getUsersList();
}
