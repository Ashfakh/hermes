package com.ashfakh.hermes.dto.WADTO;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class WALanguageDTO {
    String code="en";
    String policy = "deterministic";
}
