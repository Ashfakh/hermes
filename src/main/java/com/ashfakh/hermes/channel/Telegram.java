package com.ashfakh.hermes.channel;

import com.ashfakh.hermes.config.ExtendedExecutor;
import com.ashfakh.hermes.dto.ChatDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public abstract class Telegram extends TelegramLongPollingBot {

    public String channel = "telegram";

    private final ExtendedExecutor executorService;

    @Autowired
    public Telegram(ExtendedExecutor executorService) {
        this.executorService = executorService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        executorService.submit(
                () -> {
                    ChatDTO chatDTO = getResponse(update.getMessage().getText(), update.getMessage().getFrom().getId().toString()
                            , update.getMessage().getFrom().getFirstName());
                    chatDTO.getMessages().forEach(message -> {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(update.getMessage().getChatId().toString());
                        sendMessage.setText(message.getMessage());
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    });
                });
    }

    public abstract ChatDTO getResponse(String message, String Id, String firstName);

    @Override
    public String getBotUsername() {
        return "";
    }

    @Override
    public String getBotToken() {
        return "";
    }

}
