package com.ashfakh.hermes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO implements Serializable {
    private String summary;
    private Integer counter;
}
