package com.ashfakh.hermes;

import com.ashfakh.hermes.channel.TestTelegram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@EnableCaching
@EnableScheduling
@SpringBootApplication
public class HermesApplication implements CommandLineRunner {
    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(HermesApplication.class, args);

    }

    @Override
    public void run(String[] args) {

        TestTelegram testTelegram = context.getBean(TestTelegram.class);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(testTelegram);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
