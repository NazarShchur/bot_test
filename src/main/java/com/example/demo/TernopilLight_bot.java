package com.example.demo;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

@Component
@EnableScheduling
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
                var reply = calculateMessage();
                sendNotification(String.valueOf(chatId), reply);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void sendNotification(String chatId, String msg) throws TelegramApiException {
        var response = new SendMessage(chatId, msg, "HTML",
                null,
                null,
                null,
                null,
                null,
                null,
                null);
        execute(response);
    }

    @Scheduled(cron = "0 2 */3 * * *", zone = "Europe/Kiev")
    public void sendUpdateToChannel() {
        try {
            var message = calculateMessage();
            if (message.contains("ERROR")) {
                System.out.println("ERROR");
                Thread.sleep(60000);
                sendUpdateToChannel();
                return;
            }
            sendNotification("-1001866754700", message);
        } catch (TelegramApiException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String calculateMessage() {
        var now = LocalDateTime.now(ZoneId.of("Europe/Kiev"));
        var then = now.plusHours(3);
        return Main.resp(now) + Main.getUntil(now) + ". <a href=\"https://www.toe.com.ua/index.php/hrafik-pohodynnykh-vymknen-spozhyvachiv\">Далі</a> " + Main.resp(then);
    }
}