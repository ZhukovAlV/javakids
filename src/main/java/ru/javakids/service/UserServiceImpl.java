package ru.javakids.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.javakids.exception.UsernameNotUpdatedException;
import ru.javakids.model.User;
import ru.javakids.model.UserDto;
import ru.javakids.repository.UserRepo;
import ru.javakids.util.EncryptionUtil;

import java.security.GeneralSecurityException;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  UserRepo userRepository;

  @Autowired
  EncryptionUtil encryptionUtil;

/*  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;*/

  @Override
  public User findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Пользователь " + username + " не найден в базе данных");
    }
    return user;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsernameWithDecryptionPassword(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("Пользователь " + username + " не найден в базе данных");
    }
    user.setPassword(encryptionUtil.decrypt(user.getPassword()));
    return user;
  }

  @Override
  public User loadUserById(Long id) {
    Optional<User> userOp = userRepository.findById(id);

    if (userOp.isEmpty()) {
      throw new UsernameNotFoundException("Пользователь не найден");
    }
    return userOp.get();
  }

  @Override
  public User saveUser(UserDto userDto) throws GeneralSecurityException {
    User user = new User();
    user.setEmail(userDto.getEmail());
    user.setUsername(userDto.getUsername());
    user.setPassword(encryptionUtil.encrypt(userDto.getPassword()));
    user.setAdmin(false);
    user.setLocked(false);
    userRepository.save(user);
    return user;
  }

/*  @Transactional
  public void saveUser(User user) {
    // По умолчанию пользователь с ролью USER создается
    Set<Role> roles = new HashSet<>();
    roles.add(Role.ROLE_USER);
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    user.setActive(true);
    user.setRoles(roles);

    userRepo.save(user);
  }
  */

  @Transactional
  public User updateUser(Long id, UserDto userDto) throws GeneralSecurityException {
    Optional<User> userOp = userRepository.findById(id);

    if (userOp.isPresent()) {
      User user = userOp.get();
      user.setId(id);
      user.setUsername(userDto.getUsername());
      user.setEmail(userDto.getEmail());
      user.setPassword(encryptionUtil.encrypt(userDto.getPassword()));
      user.setAdmin(user.isAdmin());
      user.setLocked(user.isLocked());
      userRepository.save(user);
      return user;
    } else {
      throw new UsernameNotUpdatedException("Пользователь не обновлен");
    }
  }

  @Override
  public List<User> getUsersList() {
    List<User> result = new ArrayList<>();
    userRepository.findAll().forEach(result::add);
    Collections.sort(result, Comparator.comparingLong(User::getId));
    return result;
  }
}
