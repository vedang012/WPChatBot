package com.vedang.chatbot.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsAppService {

    @Value("${whatsapp.access.token}")
    private String ACCESS_TOKEN;

    @Value("${whatsapp.phone.number.id}")
    private String PHONE_NUMBER_ID;

    private String url;

    @PostConstruct
    public void init() {
        this.url = "https://graph.facebook.com/v20.0/" + PHONE_NUMBER_ID + "/messages";
    }


//    private static final String ACCESS_TOKEN = "";
//    private static final String PHONE_NUMBER_ID = "";
//    private static final String url = "https://graph.facebook.com/v20.0/" + PHONE_NUMBER_ID + "/messages";


    private FirestoreService firestoreService;

    @Autowired
    public void setFirestoreService(FirestoreService firestoreService) {
        this.firestoreService = firestoreService;
    }


    public void handleTextMessage(String message, String from) {

        // saves the user message to firebase firestore db
        firestoreService.saveUserMessage(from, message);

        String messageToSend = null;

        // handle commands
        switch (message.toLowerCase().trim()) {
            case "hi":
            case "hello":
            case "hey":
                messageToSend = """
                Hi 👋! I’m Jarurat Care Bot.
                                
                I’m here to support you through your cancer journey. You can reply with any of these:
                                
                👉 1. about
                👉 2. donate
                👉 3. financial help
                👉 4. emotional support
                👉 5. volunteer
                                
                Or just type the number directly.
                """;
                break;

            case "about":
            case "1":
                sendCtaTemplate(from, "about");
                break;

            case "donate":
            case "2":
                sendCtaTemplate(from, "donation");
                break;

            case "financial help":
            case "3":
                sendCtaTemplate(from, "financial_aid");
                break;

            case "emotional support":
            case "4":
                sendCtaTemplate(from, "emotional_support");
                break;

            case "volunteer":
            case "5":
                sendCtaTemplate(from, "volunteer");
                break;

            default:
                messageToSend = """
                Sorry, I didn't understand that. 🤔

                Please try one of these:
                👉 about
                👉 donate
                👉 financial help
                👉 emotional support
                👉 volunteer
                👉 hospitals
                """;
        }

        if (messageToSend != null) {
            sendMessage(from, messageToSend);
        }
    }


    // method to send a normal text message (no media, buttons)
    public void sendMessage(String to, String message) {

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> body = new HashMap<>();
        body.put("messaging_product", "whatsapp");
        body.put("to", to);
        body.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", message);
        body.put("text", text);

        sendRequest(body);

    }

    // sends message with buttons
    // the cta template is to be created on facebook dev console
    public void sendCtaTemplate(String to, String templateName) {

        Map<String, Object> template = new HashMap<>();
        template.put("messaging_product", "whatsapp");
        template.put("to", to);
        template.put("type", "template");

        Map<String, Object> templateObj = new HashMap<>();
        templateObj.put("name", templateName);
        templateObj.put("language", Map.of("code", "en_US"));

        template.put("template", templateObj);

        sendRequest(template);
    }

    // it sends a post req to meta api that sends the message to the user
    public void sendRequest(Map<String, Object> body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(ACCESS_TOKEN);
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("✅ Sent reply: " + response.getBody());
        } catch (Exception e) {
            System.out.println("❌ Error sending message: ");
            e.printStackTrace();
        }
    }
}