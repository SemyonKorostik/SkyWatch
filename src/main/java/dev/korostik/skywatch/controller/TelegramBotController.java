package dev.korostik.skywatch.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("${telegram.bot.auth-url}")
@RestController
public class TelegramBotController {

/*    @GetMapping("getUpdates")
    public UpdateDto getUpdates(Integer offset, Integer timeout) {
        
    }*/
}
