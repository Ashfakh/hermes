package com.ashfakh.hermes.dto;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InteractiveDTO {
    String type;
    String header;
    String body;
    String footer;
    String listName;
    List<SectionDTO> sectionDTOS;
}
