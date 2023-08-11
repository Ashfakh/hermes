package com.ashfakh.hermes.dto.WADTO;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WAMessageDTO {
    String messaging_product = "whatsapp";
    String recipient_type = "individual";
    String to;
    String type="text";
    WATextDTO text;
    WAContextDTO context;
    WAMediaDTO image;
    WAMediaDTO video;
    WAInteractiveDTO interactive;
    WATemplateDTO template;
}


