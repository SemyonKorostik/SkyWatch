package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entities.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

  boolean existsByChatId(Long chatId);

  User getByChatId(Long chatId);

  @Query("select u from User u left join fetch u.location where u.chatId = :chatId")
  Optional<User> findWithLocationByChatId(Long chatId);
}
