package com.ashfakh.hermes.service;

import com.ashfakh.hermes.entity.Summary;
import com.ashfakh.hermes.repository.SummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SummaryService {

    private final SummaryRepository summaryRepository;

    private final OpenAIAPIService openAIService;

    @Autowired
    public SummaryService(SummaryRepository summaryRepository, OpenAIAPIService openAIService) {
        this.summaryRepository = summaryRepository;
        this.openAIService = openAIService;

    }

    private Summary getOrCreateSummary(Long botId, Long userId) {
        Summary summary = summaryRepository.findSummaryByBotIdAndUserId(botId, userId);
        if (summary == null) {
            summary = new Summary();
            summary.setBotId(botId);
            summary.setUserId(userId);
            summary.setSummary("");
            summary = summaryRepository.save(summary);
        }
        return summary;
    }

    public String getSummary(Long botId, Long userId) {
        Summary summary = getOrCreateSummary(botId, userId);
        return summary.getSummary();
    }

    public String updateSummary(String history, Long userId, Long botId) {
        Summary summaryModel = getOrCreateSummary(botId, userId);
        String newSummary = summaryModel.getSummary();
        log.info("Summary before update: " + summaryModel.getSummary());
        try {
            newSummary = openAIService.getOpenAISummary(summaryModel.getSummary(), history);
        } catch (Exception e) {
            log.error("Error while updating summary: ");
            e.printStackTrace();
            return newSummary;
        }
        summaryModel.setSummary(newSummary);
        summaryRepository.save(summaryModel);
        return newSummary;
    }
}
