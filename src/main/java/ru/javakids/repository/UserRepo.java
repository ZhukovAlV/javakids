package ru.javakids.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.javakids.model.User;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {
  User findByUsername(String username);
}
