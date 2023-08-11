package com.ashfakh.hermes.service;

import com.ashfakh.hermes.dto.BotResponseDTO;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
//TODO: add LLM service factory and replace OpenAI with LLM
public class OpenAIAPIService {

    private final OpenAiService service;

    public OpenAIAPIService(@Value("${openai.api.key}") String apiKey) {
        this.service = new OpenAiService(apiKey, Duration.ofSeconds(61));
    }

    public BotResponseDTO getOpenAIMessage(String persona, String query, List<String> history, String summary) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        chatMessages.addAll(generateSystemMessages(persona, summary));
        chatMessages.addAll(generateChatHistory(history));
        chatMessages.add(new ChatMessage(ChatMessageRole.USER.value(), query));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .model("gpt-3.5-turbo")
                .maxTokens(300)
                .build();
        BotResponseDTO botResponseDTO = new BotResponseDTO();
        ChatCompletionResult result = tryChatCompletionWithRetry(completionRequest);
        botResponseDTO.setResponse(result.getChoices().get(0).getMessage().getContent());
        botResponseDTO.setTokens(result.getUsage().getTotalTokens());
        return botResponseDTO;
    }

    private ChatCompletionResult tryChatCompletionWithRetry(ChatCompletionRequest chatCompletionRequest) {
        ChatCompletionResult result = null;
        int retryCount = 0;
        while (result == null && retryCount < 2) {
            try {
                result = service.createChatCompletion(chatCompletionRequest);
            } catch (Exception e) {
                log.error("Error while calling openai chat completion", e);
                retryCount++;
            }
        }
        return result;
    }

    public String getOpenAISummary(String summaryMessage, String newMessages) {
        String prompt = "This is details the user has given about himself so far : " + summaryMessage +
                "\nThe new messages are as follows :" + newMessages +
                "Extract the users details out of these new messages and combine with the old details to give a new user description in no less than 1000 words. If there are no new details, just repeat the old details.";
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage systemPromptMessage = new ChatMessage();
        systemPromptMessage.setRole(ChatMessageRole.SYSTEM.value());
        systemPromptMessage.setContent(prompt);
        chatMessages.add(systemPromptMessage);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .temperature(0.3)
                .model("gpt-3.5-turbo")
                .maxTokens(15)
                .build();
        return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
    }

    public String getUseCase(String query, String lastMessage, String prompt) {
        List<ChatMessage> chatMessages = new ArrayList<>();
        ChatMessage systemPromptMessage = new ChatMessage();
        systemPromptMessage.setRole(ChatMessageRole.SYSTEM.value());
        systemPromptMessage.setContent(prompt);
        chatMessages.add(systemPromptMessage);
        ChatMessage userQueryMessage = new ChatMessage();
        userQueryMessage.setRole(ChatMessageRole.SYSTEM.value());
        query = "previous query: " + lastMessage + " current_query: " + query;
        userQueryMessage.setContent(query);
        chatMessages.add(userQueryMessage);
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(chatMessages)
                .temperature(0.3)
                .model("gpt-3.5-turbo")
                .maxTokens(15)
                .build();
        return tryChatCompletionWithRetry(completionRequest).getChoices().get(0).getMessage().getContent();
    }

    List<ChatMessage> generateSystemMessages(String persona, String summary) {
        ChatMessage systemPersonaMessage = new ChatMessage();
        systemPersonaMessage.setRole(ChatMessageRole.SYSTEM.value());
        systemPersonaMessage.setContent(persona);
        ChatMessage systemSummaryMessage = new ChatMessage();
        systemSummaryMessage.setRole(ChatMessageRole.SYSTEM.value());
        systemSummaryMessage.setContent("Here is a summary of your conversation so far: " + summary + ". If you can derive any characteristics of the user from the provided summary, use it to respond better");
        return List.of(systemPersonaMessage, systemSummaryMessage);
    }

    List<ChatMessage> generateChatHistory(List<String> history) {
        //TODO: make this replacement better, replace history by object
        return history.stream().map(h -> {
            if (h.contains("user:")) return new ChatMessage(ChatMessageRole.USER.value(), h.replace("user:", ""));
            else return new ChatMessage(ChatMessageRole.ASSISTANT.value(), h.replace("AI:", ""));
        }).collect(Collectors.toList());
    }
}
