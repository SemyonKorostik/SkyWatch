package dev.korostik.skywatch.service;

import dev.korostik.skywatch.entity.User;
import dev.korostik.skywatch.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public boolean existsByChatId(Long chatId) {
    return userRepository.existsByChatId(chatId);
  }

  public User getByChatId(Long chatId) {
    return userRepository.getByChatId(chatId);
  }

  public User save(User user) {
    return userRepository.save(user);
  }

}
