package com.ashfakh.hermes.dto.WADTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WATextDTO {
    Boolean preview_url= false;
    String body;
}
