package com.ashfakh.hermes.repository;

import com.ashfakh.hermes.entity.Summary;
import org.springframework.data.repository.CrudRepository;

public interface SummaryRepository extends CrudRepository<Summary, Long> {
    Summary findSummaryByBotIdAndUserId(Long botId, Long userId);

}
