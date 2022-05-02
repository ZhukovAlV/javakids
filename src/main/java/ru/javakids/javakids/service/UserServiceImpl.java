package ru.javakids.javakids.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.javakids.javakids.model.User;
import ru.javakids.javakids.model.UserDto;
import ru.javakids.javakids.repository.UserRepository;
import ru.javakids.javakids.util.EncryptionUtil;

import java.security.GeneralSecurityException;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepository userRepository;

  @Autowired
  EncryptionUtil encryptionUtil;

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User " + username + " was not found in the database");
    }
    return user;
  }

  @Override
  public User saveUser(UserDto userDto) throws GeneralSecurityException {
    User user = new User();
    user.setName(userDto.getName());
    user.setUsername(userDto.getUsername());
    user.setPassword(encryptionUtil.encrypt(userDto.getPassword()));
    user.setAdmin(false);
    user.setLocked(false);
    userRepository.saveAndFlush(user);
    return user;
  }
}
