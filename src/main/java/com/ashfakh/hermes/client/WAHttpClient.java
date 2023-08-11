package com.ashfakh.hermes.client;

import com.ashfakh.hermes.dto.InteractiveDTO;
import com.ashfakh.hermes.dto.WADTO.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class WAHttpClient extends HttpLocalClient {

    private final String whatsappToken;

    private final String whatsappPhoneNumberId;

    @Autowired
    public WAHttpClient(@Value("${whatsapp.token}") String whatsappToken,
                        @Value("${whatsapp.phonenumber.id}") String whatsappPhoneNumberId) {
        super();
        this.whatsappPhoneNumberId = whatsappPhoneNumberId;
        this.whatsappToken = whatsappToken;
    }

    public void sendMessage(String to, String message, String msgId) {
        WATextDTO text = WATextDTO.builder().preview_url(false).body(message).build();
        WAMessageDTO messageDTO = WAMessageDTO.builder().messaging_product("whatsapp")
                .recipient_type("individual")
                .type("text").to(to)
                .text(text).build();
        if (msgId != null && !msgId.equals("")) {
            messageDTO.setContext(WAContextDTO.builder().message_id(msgId).build());
        }
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(messageDTO);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        sendJsonOverHTTPWIthBearerToken("https://graph.facebook.com/v13.0/" + whatsappPhoneNumberId + "/messages",
                whatsappToken,
                jsonString,
                new HashMap<>());

    }

    public void sendMedia(String to, String mediaId, String caption, String mediaType) {
        WAMessageDTO.WAMessageDTOBuilder messageDTOBuilder = WAMessageDTO.builder().messaging_product("whatsapp")
                .recipient_type("individual")
                .type(mediaType).to(to);
        if (mediaType.contains("image")) {
            messageDTOBuilder.image(WAMediaDTO.builder().id(mediaId).caption(caption).build())
                    .type("image");
        } else if (mediaType.contains("video")) {
            messageDTOBuilder.video(WAMediaDTO.builder().id(mediaId).caption(caption).build())
                    .type("video");
        }
        WAMessageDTO messageDTO = messageDTOBuilder.build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(messageDTO);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        sendJsonOverHTTPWIthBearerToken("https://graph.facebook.com/v13.0/" + whatsappPhoneNumberId + "/messages",
                whatsappToken,
                jsonString,
                new HashMap<>());

    }

    public void sendTemplate(String to, String templateName, List<String> params, String buttonURL, String languageCode) {
        WAMessageDTO.WAMessageDTOBuilder messageDTOBuilder = WAMessageDTO.builder().messaging_product("whatsapp")
                .recipient_type("individual")
                .type("template").to(to);
        List<WAComponentDTO> componentDTOS = new ArrayList<>();
        if (params != null) {
            WAComponentDTO componentDTO = new WAComponentDTO();
            componentDTO.setType("body");
            List<WAParameterDTO> parameterDTOS = new ArrayList<>();
            params.forEach(p -> {
                WAParameterDTO parameterDTO = new WAParameterDTO();
                parameterDTO.setType("text");
                parameterDTO.setText(p);
                parameterDTOS.add(parameterDTO);
            });
            componentDTO.setParameters(parameterDTOS);
            componentDTOS.add(componentDTO);
        }
        if (buttonURL != "") {
            WAComponentDTO buttonComponentDTO = new WAComponentDTO();
            buttonComponentDTO.setType("button");
            buttonComponentDTO.setSub_type("url");
            buttonComponentDTO.setIndex(0);
            WAParameterDTO parameterDTO = new WAParameterDTO();
            parameterDTO.setType("text");
            parameterDTO.setText(buttonURL);
            buttonComponentDTO.setParameters(List.of(parameterDTO));
            componentDTOS.add(buttonComponentDTO);
        }
        messageDTOBuilder.template(WATemplateDTO.builder().language(WALanguageDTO.builder().code(languageCode)
                        .policy("deterministic").build())
                .name(templateName).components(componentDTOS).build());
        WAMessageDTO messageDTO = messageDTOBuilder.build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(messageDTO);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        sendJsonOverHTTPWIthBearerToken("https://graph.facebook.com/v13.0/" + whatsappPhoneNumberId + "/messages",
                whatsappToken,
                jsonString,
                new HashMap<>());

    }

    public void sendInteractiveList(String to, InteractiveDTO interactiveDTO) {
        WAMessageDTO.WAMessageDTOBuilder messageDTOBuilder = WAMessageDTO.builder().messaging_product("whatsapp")
                .recipient_type("individual")
                .type("interactive").to(to);
        WAHeaderDTO headerDTO = WAHeaderDTO.builder().type("text")
                .text(interactiveDTO.getHeader()).build();
        List<WASectionDTO> sectionDTOS = new ArrayList<>();
        interactiveDTO.getSectionDTOS().forEach(sectionDTO -> {
            List<WARowDTO> rowDTOS = new ArrayList<>();
            sectionDTO.getCategories().forEach(category -> {
                rowDTOS.add(WARowDTO.builder()
                        .id(UUID.randomUUID().toString())
                        .title(category.getCategoryName())
                        .description(category.getCatergoryDescription())
                        .build());
            });
            sectionDTOS.add(WASectionDTO.builder().title(sectionDTO.getSectionName()).rows(rowDTOS).build());
        });

        WAActionDTO actionDTO = WAActionDTO.builder().button(interactiveDTO.getListName())
                .sections(sectionDTOS).build();
        WAInteractiveDTO waInteractiveDTO = WAInteractiveDTO.builder().type(interactiveDTO.getType())
                .header(headerDTO).body(WABodyDTO.builder().text(interactiveDTO.getBody()).build())
                .action(actionDTO).build();
        messageDTOBuilder.interactive(waInteractiveDTO);
        WAMessageDTO messageDTO = messageDTOBuilder.build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = "";
        try {
            jsonString = mapper.writeValueAsString(messageDTO);
        } catch (Exception e) {
            System.out.println("Exception");
        }
        sendJsonOverHTTPWIthBearerToken("https://graph.facebook.com/v13.0/" + whatsappPhoneNumberId + "/messages",
                whatsappToken,
                jsonString,
                new HashMap<>());

    }

    public String uploadMedia(String localPath, String mediaType) {

        File file = new File(localPath);
        ContentBody fileBody = new FileBody(file, ContentType.create(mediaType));
        Map<String, String> params = new HashMap<>();
        params.put("type", mediaType);
        params.put("messaging_product", "whatsapp");

        String response = sendFileOverHTTPWIthBearerToken("https://graph.facebook.com/v16.0/" + whatsappPhoneNumberId + "/media",
                whatsappToken,
                fileBody,
                params);
        ObjectMapper mapper = new ObjectMapper();
        try {
            WAUploadResponseDTO responseDTO = mapper.readValue(response, WAUploadResponseDTO.class);
            return responseDTO.getId();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }


}
