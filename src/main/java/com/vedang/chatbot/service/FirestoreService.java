package com.vedang.chatbot.service;

import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirestoreService {

    private final Firestore db = FirestoreClient.getFirestore();

    public void saveUserMessage(String from, String message) {
        Map<String, Object> data = new HashMap<>();
        data.put("from", from);
        data.put("message", message);
        data.put("timestamp", FieldValue.serverTimestamp());

        db.collection("messages").add(data);
    }
}
