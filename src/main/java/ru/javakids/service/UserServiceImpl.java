package ru.javakids.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User " + username + " was not found in the database");
    }
    return user;
  }

  @Override
  public User loadUserById(Long id) {
    Optional<User> userOp = userRepository.findById(id);

    if (userOp.isEmpty()) {
      throw new UsernameNotFoundException("User not found");
    }
    return userOp.get();
  }

  @Override
  public User saveUser(UserDto userDto) throws GeneralSecurityException {
    User user = new User();
    user.setEmail(userDto.getName());
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

  @Transactional
  public Optional<User> updateUser(Long id, User user) {
    Optional<User> userOp = userRepo.findById(id);

    if (userOp.isPresent()) {
      user.setId(id);
      user.setActive(userOp.get().isActive());
      user.setRoles(userOp.get().getRoles());
      user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
      userRepo.save(user);
      return userRepo.findById(id);
    } else {
      return userOp;
    }
  }
  */
  @Override
  public List<User> getUsersList() {
    List<User> result = new ArrayList<>();
    userRepository.findAll().forEach(result::add);
    Collections.sort(result, Comparator.comparingLong(User::getId));
    return result;
  }
}
