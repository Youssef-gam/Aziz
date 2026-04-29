package com.parc.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class PushNotificationService {
    public void sendPush(String token, String title, String body) {
        if (token == null || token.isEmpty()) return;
        Message message = Message.builder()
                .setNotification(Notification.builder().setTitle(title).setBody(body).build())
                .setToken(token).build();
        try {
            FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Push envoyé");
        } catch (Exception e) {
            System.err.println("❌ Erreur push : " + e.getMessage());
        }
    }
}