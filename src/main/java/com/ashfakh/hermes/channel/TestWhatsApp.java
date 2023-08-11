package com.ashfakh.hermes.channel;

import com.ashfakh.hermes.client.WAHttpClient;
import com.ashfakh.hermes.config.ExtendedExecutor;
import com.ashfakh.hermes.dto.ChatDTO;
import com.ashfakh.hermes.service.ChatFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestWhatsApp extends WhatsApp{

    @Autowired
    ChatFlowService chatFlowService;

    public TestWhatsApp(ExtendedExecutor executorService, WAHttpClient waHttpClient) {
        super(executorService, waHttpClient);
    }

    @Override
    public ChatDTO getResponse(String message, String Id, String firstName) {
        return chatFlowService.passThroughFlow(message, channel, Id, firstName);
    }
}
