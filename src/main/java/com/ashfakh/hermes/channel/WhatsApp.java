package com.ashfakh.hermes.channel;

import com.ashfakh.hermes.client.WAHttpClient;
import com.ashfakh.hermes.config.ExtendedExecutor;
import com.ashfakh.hermes.dto.ChatDTO;
import com.ashfakh.hermes.dto.WADTO.WhatsAppRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public abstract class WhatsApp {
    public String channel = "whatsapp";

    private WAHttpClient waHttpClient;

    private ExtendedExecutor executorService;

    @Autowired
    public WhatsApp(ExtendedExecutor executorService, WAHttpClient waHttpClient) {
        this.executorService = executorService;
        this.waHttpClient = waHttpClient;
    }

    public void processWhatsAppRequest(WhatsAppRequestDTO request) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info(jsonString);
        String waMessage = request.getMessage();
        if (!waMessage.equals("")) {
            executorService.submit(
                    () -> {
                        ChatDTO responseDTO = getResponse(waMessage, request.getTo(), request.getName());
                        sendResponseDTO(responseDTO, request);
                    });
        } else if (request.getType().contains("interactive")) {
            log.info("Interactive message flow");
            executorService.submit(
                    () -> {
                        ChatDTO responseDTO = getResponse(request.getInteractiveMessages(), request.getTo(), request.getName());
                        sendResponseDTO(responseDTO, request);
                    });
        } else {
            log.info("Message is empty");
        }

    }

    private void sendResponseDTO(ChatDTO responseDTO, WhatsAppRequestDTO request) {
        if (responseDTO.getMessages() != null)
            responseDTO.getMessages()
                    .forEach(message -> waHttpClient.sendMessage(request.getTo(), message.getMessage(), message.getIsReply() ? request.getId() : ""));
        if (responseDTO.getMedias() != null)
            responseDTO.getMedias()
                    .forEach(media -> waHttpClient.sendMedia(request.getTo(), media.getMediaId(), media.getCaption(), media.getMediaType()));
        if (responseDTO.getInteractiveDTOS() != null) {
            responseDTO.getInteractiveDTOS()
                    .forEach(interactiveDTO -> waHttpClient.sendInteractiveList(request.getTo(), interactiveDTO));
        }
    }

    public abstract ChatDTO getResponse(String message, String Id, String firstName);

}
