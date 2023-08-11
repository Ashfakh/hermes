package com.ashfakh.hermes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ChatDTO {
    List<MessageDTO> messages;
    List<InteractiveDTO> interactiveDTOS;
    List<MediaDTO> medias;
    List<TemplateDTO> templates;
}
