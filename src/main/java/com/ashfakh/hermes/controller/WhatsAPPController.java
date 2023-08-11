package com.ashfakh.hermes.controller;


import com.ashfakh.hermes.channel.TestWhatsApp;
import com.ashfakh.hermes.config.ExtendedExecutor;
import com.ashfakh.hermes.dto.ChatDTO;
import com.ashfakh.hermes.dto.WADTO.WhatsAppRequestDTO;
import com.ashfakh.hermes.dto.MessageDTO;
import com.ashfakh.hermes.service.ChatFlowService;
import com.ashfakh.hermes.service.MediaManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/whatsapp")
public class WhatsAPPController {

    private TestWhatsApp testWhatsApp;

    @Autowired
    public WhatsAPPController(TestWhatsApp testWhatsApp) {
        this.testWhatsApp = testWhatsApp;

    }

    @PostMapping(value = "/test-bot")
    public void processMessage(@RequestBody WhatsAppRequestDTO wtdo) {
        log.info("Whatsapp Notification received");
        if (!wtdo.getTo().equals(""))
            testWhatsApp.processWhatsAppRequest(wtdo);

    }


    @GetMapping(value = "/test-bot")
    @ResponseBody
    public String getDateAndTime(@RequestParam("hub.mode") String mode,
                                 @RequestParam("hub.verify_token") String token,
                                 @RequestParam("hub.challenge") int challenge) {
        if (token.equals("abcde"))
            return String.valueOf(challenge);
        else return "0";
    }
}
