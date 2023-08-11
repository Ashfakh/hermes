package com.ashfakh.hermes.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PromptDTO {
    String useCase;
    String prompt;
}
