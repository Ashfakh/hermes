package com.ashfakh.hermes.dto.WADTO;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class WAActionDTO {
    String button;
    List<WASectionDTO> sections;
}
