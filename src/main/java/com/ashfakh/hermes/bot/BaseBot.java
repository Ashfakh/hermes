package com.ashfakh.hermes.bot;

import com.ashfakh.hermes.dto.BotResponseDTO;
import com.ashfakh.hermes.dto.MessageDTO;
import com.ashfakh.hermes.dto.PromptDTO;
import com.ashfakh.hermes.dto.SessionDTO;
import com.ashfakh.hermes.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public abstract class BaseBot {

    private OpenAIAPIService openAIService;

    private SummaryService summaryService;

    private SessionService sessionService;

    private RedisTemplate<String, List<String>> redisTemplate;

    private ExecutorService executorService;

    private PromptService promptService;

    @Autowired
    public BaseBot(OpenAIAPIService openAIService, SummaryService summaryService, SessionService sessionService, RedisTemplate<String, List<String>> redisTemplate, ExecutorService executorService, PromptService promptService, UserService userService) {
        this.openAIService = openAIService;
        this.summaryService = summaryService;
        this.sessionService = sessionService;
        this.redisTemplate = redisTemplate;
        this.executorService = executorService;
        this.promptService = promptService;
    }


    public List<String> getCommandResponse(String command, Long userId) {
        switch (command) {
            case "!help":
                return List.of(getBotHelp());
            case "!reset":
                ValueOperations<String, List<String>> valueOperations = redisTemplate.opsForValue();
                valueOperations.getAndDelete("chat-session:" + userId);
                return List.of("Your chat session is now reset");
            case "!summary":
                return List.of("The summary of this conversation is : \n" + summaryService.getSummary(1L, userId));
            default:
                return List.of("Oops! I couldn’t find this command. \uD83D\uDE25\n");
        }


    }

    public List<MessageDTO> getResponse(Long userId, String message, Boolean isAi) {
        if (isAi) {
            SessionDTO sessionDTO = sessionService.getAndUpdateSession(userId, 1L);
            if (sessionDTO.isChatInProgress() && (System.currentTimeMillis() - sessionDTO.getSessionTime()) < 120000)
                return List.of(MessageDTO.builder().msg(getBotName() + " is still processing the previous message.").isReply(true).build());
            List<MessageDTO> messages = new ArrayList<>();
            if (sessionDTO.getChatHistory().isEmpty())
                messages.add(MessageDTO.builder().msg("Starting a new chat").isReply(false).build());
            long startTime = System.currentTimeMillis();
            Future<BotResponseDTO> messageResp = executorService.submit(() -> getBotResponse(message, sessionDTO));
            while (!messageResp.isDone()) {
                if (System.currentTimeMillis() - startTime > 120000) {
                    messageResp.cancel(true);
                    String failureMsg = "Sorry, I am unable to process your request at the moment. Please try again later.";
                    messages.add(MessageDTO.builder().msg(failureMsg).isReply(true).build());
                    sessionService.updateSession(userId, 1L, message, failureMsg, sessionDTO);
                    return messages;
                }
            }
            String messageResponse = "";
            String useCase = "";
            Long tokens = 0L;
            try {
                messageResponse = messageResp.get().getResponse();
                useCase = messageResp.get().getUseCase();
                tokens = messageResp.get().getTokens();
            } catch (Exception e) {
                e.printStackTrace();
            }
            sessionDTO.setUseCase(useCase);
            sessionService.updateSession(userId, 1L, message, messageResponse, sessionDTO);
            messages.add(MessageDTO.builder().msg(messageResponse).tokens(tokens).useCase(useCase).isReply(true).build());
            return messages;
        } else {
            return getCommandResponse(message, userId).stream().map(k -> MessageDTO.builder().msg(k).isReply(false).build()).collect(Collectors.toList());
        }
    }


    private BotResponseDTO getBotResponse(String message, SessionDTO sessionDTO) {
        PromptDTO promptDTO = promptService.getPrompt(message, sessionDTO);
        BotResponseDTO botResponseDTO = openAIService.getOpenAIMessage(getPersona() + promptDTO.getPrompt(), message,
                sessionDTO.getChatHistory(), sessionDTO.getSummaryDTO().getSummary());
        botResponseDTO.setUseCase(promptDTO.getUseCase());
        return botResponseDTO;
    }

    public String getBotName() {
        return "BaseBot";
    }

    public abstract String getBotHelp();

    public abstract String getPersona();

}
