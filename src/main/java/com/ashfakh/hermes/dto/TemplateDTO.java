package com.ashfakh.hermes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateDTO {
    String templateName;
    List<String> params;
    String buttonUrl;
    String languageCode;
}
