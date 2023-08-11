package com.ashfakh.hermes.dto.WADTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WARowDTO {
    String id;
    String title;
    String description;
}
