package com.ashfakh.hermes.service;

import com.ashfakh.hermes.bot.EducationBot;
import com.ashfakh.hermes.dto.ChatDTO;
import com.ashfakh.hermes.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatFlowService {

    private final UserService userService;

    private final EducationBot educationBot;

    @Autowired
    public ChatFlowService(UserService userService, EducationBot educationBot) {
        this.userService = userService;
        this.educationBot = educationBot;
    }

    public ChatDTO passThroughFlow(String message, String channel, String Id, String firstName) {
        UserDTO user = userService.getOrCreateUser(channel, Id
                , firstName);
        ChatDTO reply = ChatDTO.builder().build();
        reply.setMessages(educationBot.getResponse(user.getId(), message, message.charAt(0) != '!'));
        return reply;
    }
}
