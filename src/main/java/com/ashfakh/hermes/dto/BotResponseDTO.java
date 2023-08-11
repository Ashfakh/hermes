package com.ashfakh.hermes.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BotResponseDTO {
    String response;
    String useCase;
    Long tokens;
}
