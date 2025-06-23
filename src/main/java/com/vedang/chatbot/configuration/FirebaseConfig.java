package com.vedang.chatbot.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                initializeFirebase();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    private void initializeFirebase() throws IOException {
        // Use ClassPathResource for better resource handling
        ClassPathResource resource = new ClassPathResource("firebase-config.json");

        try (InputStream serviceAccountStream = resource.getInputStream()) {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            FirebaseApp.initializeApp(options);
            System.out.println("Firebase initialized successfully");
        }
    }
}