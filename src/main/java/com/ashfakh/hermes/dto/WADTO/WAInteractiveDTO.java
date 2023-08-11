package com.ashfakh.hermes.dto.WADTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WAInteractiveDTO {
    String type;
    WAHeaderDTO header;
    WABodyDTO body;
    WABodyDTO footer;
    WAActionDTO action;
}
