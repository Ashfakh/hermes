package com.ashfakh.hermes.channel;

import com.ashfakh.hermes.config.ExtendedExecutor;
import com.ashfakh.hermes.dto.ChatDTO;
import com.ashfakh.hermes.service.ChatFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TestTelegram extends Telegram {

    private String username;

    private String token;

    private ChatFlowService chatFlowService;

    @Autowired
    public TestTelegram(@Value("${telegram.bot.username}") String username,
                        @Value("${telegram.bot.token}") String token, ChatFlowService chatFlowService, ExtendedExecutor executorService) {
        super(executorService);
        this.username = username;
        this.token = token;
        this.chatFlowService = chatFlowService;
    }

    public TestTelegram(ExtendedExecutor executorService) {
        super(executorService);
    }

    @Override
    public ChatDTO getResponse(String message, String Id, String firstName) {
        return chatFlowService.passThroughFlow(message, channel, Id, firstName);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
