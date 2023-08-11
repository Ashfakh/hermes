package com.ashfakh.hermes.dto;


import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Data
@Builder
public class MessageDTO {
    String msg;
    Map<String, String> messageParams;
    Boolean isReply;
    Long tokens;
    String useCase;
    final Long timestamp = System.currentTimeMillis();


    public String getMessage() {
        AtomicReference<String> message = new AtomicReference<>(msg);
        if (messageParams != null)
            messageParams.forEach((k, v) -> message.set(message.get().replace("{" + k + "}", v)));
        return message.get();
    }
}
