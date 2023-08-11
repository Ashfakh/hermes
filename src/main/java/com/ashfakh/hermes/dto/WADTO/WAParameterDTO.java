package com.ashfakh.hermes.dto.WADTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WAParameterDTO {
    String type;
    String text;
}
