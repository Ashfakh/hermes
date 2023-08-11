package com.ashfakh.hermes.dto.WADTO;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class WATemplateDTO {
    String name;
    WALanguageDTO language;
    List<WAComponentDTO> components;
}
