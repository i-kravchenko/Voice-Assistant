package com.example.VoiceAssistant.configuration;

import com.example.VoiceAssistant.bot.VoiceAssistant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class TelegramConfiguration
{
    @Bean
    public TelegramBotsApi telegramBotsApi(VoiceAssistant voiceAssistant) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(voiceAssistant);
        return telegramBotsApi;
    }
}
