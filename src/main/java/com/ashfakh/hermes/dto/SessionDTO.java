package com.ashfakh.hermes.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionDTO implements Serializable {
    private SummaryDTO summaryDTO;
    private List<String> chatHistory;
    private String useCase;
    private boolean chatInProgress;
    private Long sessionTime;
}
