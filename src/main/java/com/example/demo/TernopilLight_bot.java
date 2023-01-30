package com.example.demo;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TernopilLight_bot extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TernopilLight_bot";
    }

    @Override
    public String getBotToken() {
        return "5746549424:AAETB2pbC7FAirJiXypaGVNu4kAyQooheog";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
            var msg = update.getMessage();
            var chatId = msg.getChatId();
            try {
                var reply = Main.resp();
                sendNotification(String.valueOf(chatId), reply);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendNotification(String chatId, String msg) throws TelegramApiException {
        var response = new SendMessage(chatId, msg);
        execute(response);
    }
}