package com.ashfakh.hermes.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MediaDTO {
    String mediaId;
    String caption;
    String mediaType;
}
