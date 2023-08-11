package com.ashfakh.hermes.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class SectionDTO {
    String sectionName;
    List<CategoryDTO> categories;

}
