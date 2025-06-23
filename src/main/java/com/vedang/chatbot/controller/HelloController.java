package com.vedang.chatbot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String hello() {
        return """
    ✅ The bot is deployed successfully! It's live on WhatsApp test number: +1 (555) 082-1525
    \n

    ⚠ Note: This number only works with WhatsApp accounts that are added as test users.
    \n

    👉 If you'd like to test the bot, please share your WhatsApp number with the developer 
    so it can be added to the test list.
    I have sent the demo video too.
""";
    }
}
