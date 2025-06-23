package com.vedang.chatbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class MessageService {

    private WhatsAppService whatsAppService;

    @Autowired
    public void setWhatsAppService(WhatsAppService whatsAppService) {
        this.whatsAppService = whatsAppService;
    }

    // this method extracts the mobile number and the text from the incoming msg
    public void processIncoming(Map<String, Object> payload) {
        try {
            Map entry = (Map) ((List) payload.get("entry")).get(0);
            Map change = (Map) ((List) entry.get("changes")).get(0);
            Map value = (Map) change.get("value");

            List<Map<String, Object>> messages = (List<Map<String, Object>>) value.get("messages");

            if (messages != null && !messages.isEmpty()) {
                Map<String, Object> message = messages.get(0);

                String senderPhone = (String) message.get("from");
                Map<String, String> textObj = (Map<String, String>) message.get("text");
                String userMessage = textObj.get("body");

                System.out.println("📨 From: " + senderPhone);
                System.out.println("💬 Message: " + userMessage);

                // whatsapp service handles the text message and gives proper response
                whatsAppService.handleTextMessage(userMessage, senderPhone);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
