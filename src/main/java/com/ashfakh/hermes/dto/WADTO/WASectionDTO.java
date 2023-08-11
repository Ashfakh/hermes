package com.ashfakh.hermes.dto.WADTO;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class WASectionDTO {
    String title;
    List<WARowDTO> rows;
}
