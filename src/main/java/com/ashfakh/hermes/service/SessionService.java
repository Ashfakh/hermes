package com.ashfakh.hermes.service;

import com.ashfakh.hermes.dto.SessionDTO;
import com.ashfakh.hermes.dto.SummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {

    RedisTemplate<String, SessionDTO> redisTemplate;

    private final SummaryService summaryService;

    @Autowired
    public SessionService(RedisTemplate<String, SessionDTO> redisTemplate, SummaryService summaryService) {
        this.redisTemplate = redisTemplate;
        this.summaryService = summaryService;
    }

    public SessionDTO createSession(Long botId, Long userId) {
        String summary = summaryService.getSummary(botId, userId);
        SummaryDTO summaryDTO = new SummaryDTO();
        summaryDTO.setSummary(summary);
        summaryDTO.setCounter(5);
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setSummaryDTO(summaryDTO);
        sessionDTO.setChatInProgress(true);
        sessionDTO.setChatHistory(List.of());
        sessionDTO.setSessionTime(System.currentTimeMillis());
        return sessionDTO;
    }

    public SessionDTO getAndUpdateSession(Long userId, Long botId) {
        ValueOperations<String, SessionDTO> valueOperations = redisTemplate.opsForValue();
        SessionDTO sessionDTO = valueOperations.get("chat-session:" + userId);
        if (sessionDTO == null) {
            sessionDTO = createSession(userId, botId);
            valueOperations.set("chat-session:" + userId, sessionDTO, 2, TimeUnit.HOURS);
            sessionDTO.setChatInProgress(false);
        } else {
            if (sessionDTO.isChatInProgress()) {
                return sessionDTO;
            } else {
                sessionDTO.setChatInProgress(true);
                valueOperations.set("chat-session:" + userId, sessionDTO, 2, TimeUnit.HOURS);
                sessionDTO.setChatInProgress(false);
            }
        }
        return sessionDTO;
    }

    public void updateSession(Long userId, Long botId, String message, String response, SessionDTO sessionDTO) {
        sessionDTO.setChatHistory(updateChatHistory(message, response, sessionDTO.getChatHistory()));
        sessionDTO.setChatInProgress(false);
        if (sessionDTO.getSummaryDTO().getCounter() < 1) {
            String newSummary = summaryService.updateSummary
                    (String.join("\n", sessionDTO.getChatHistory()), userId, botId);
            sessionDTO.getSummaryDTO().setSummary(newSummary);
            sessionDTO.getSummaryDTO().setCounter(10);
        } else {
            sessionDTO.getSummaryDTO().setCounter(sessionDTO.getSummaryDTO().getCounter() - 1);
        }
        sessionDTO.setSessionTime(System.currentTimeMillis());
        ValueOperations<String, SessionDTO> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("chat-session:" + userId, sessionDTO, 2, TimeUnit.HOURS);
    }

    private List<String> updateChatHistory(String message, String response, List<String> history) {
        if (history.isEmpty()) {
            history = new ArrayList<>();
        } else {
            if (history.size() > 6) {
                history.remove(0);
                history.remove(1);
            }
        }
        history.add("user:" + message);
        history.add("AI:" + response);
        return history;
    }
}