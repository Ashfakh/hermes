package com.ashfakh.hermes.dto.WADTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WAComponentDTO {
    String type;
    String sub_type;
    Integer index;
    List<WAParameterDTO> parameters;
}
