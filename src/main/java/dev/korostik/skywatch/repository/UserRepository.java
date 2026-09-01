package dev.korostik.skywatch.repository;

import dev.korostik.skywatch.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    boolean existsUserByChatId(Long chatId);
}
