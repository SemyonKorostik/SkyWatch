package dev.korostik.skywatch.service.user;

import dev.korostik.skywatch.entities.user.User;
import dev.korostik.skywatch.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserServiceImpl {

  private final UserRepository userRepository;

  public boolean existsByChatId(Long chatId) {
    return userRepository.existsByChatId(chatId);
  }

  public User getByChatId(Long chatId) {
    return userRepository.getByChatId(chatId);
  }

  @Transactional
  public User save(User user) {
    return userRepository.save(user);
  }

  public User findWithLocationByChatId(Long chatId) {
    return userRepository.findWithLocationByChatId(chatId).orElseThrow();
  }
}
