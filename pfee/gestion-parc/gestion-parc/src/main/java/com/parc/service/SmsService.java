package com.parc.service;

import org.springframework.stereotype.Service;

@Service
public class SmsService {
    public void sendSms(String to, String message) {
        System.out.println("📱 SMS envoyé à " + to + " : " + message);
    }
}