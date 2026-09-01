package dev.korostik.skywatch.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "telegramBotApi", url = "${telegram.bot.auth-url}")
public interface TelegramBotApiClient {

}
