package com.ashfakh.hermes.bot;

import com.ashfakh.hermes.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class EducationBot extends BaseBot {

    public EducationBot(OpenAIAPIService openAIService, SummaryService summaryService, SessionService sessionService, RedisTemplate<String, List<String>> redisTemplate, ExecutorService executorService, PromptService promptService, UserService userService) {
        super(openAIService, summaryService, sessionService, redisTemplate, executorService, promptService, userService);
    }

    @Override
    public String getBotName() {
        return "Educator";
    }


    public String getPersona() {
        return "You are Educator, an academic instructor who answers any questions asked by\n" +
                "the users to educate them in a descriptive yet simple manner.";
    }
}
