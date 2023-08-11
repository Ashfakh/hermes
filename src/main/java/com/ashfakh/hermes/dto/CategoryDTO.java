package com.ashfakh.hermes.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CategoryDTO {
    String categoryName;
    String catergoryDescription;
}
