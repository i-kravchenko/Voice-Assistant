package com.example.VoiceAssistant.bot;

import com.example.VoiceAssistant.OpenAIService;
import com.example.VoiceAssistant.tools.OggConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class VoiceAssistant extends TelegramLongPollingBot {
    @Value("${telegram.bot.username}")
    private String username;
    @Value("${telegram.bot.token}")
    private String token;
    private final OggConverter converter;
    private final OpenAIService openAIService;

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (message.hasVoice()) {
            try {
                File file = downloadFile(
                        execute(GetFile
                                .builder()
                                .fileId(message.getVoice().getFileId())
                                .build()),
                        File.createTempFile("tmp", ".ogg")
                );
                file = converter.convertOggToMp3(file);
                String transcriptions = openAIService
                        .transcript(new FileSystemResource(file.getAbsolutePath()));
                String chatResponse = openAIService.chat(transcriptions);
                execute(SendMessage
                        .builder()
                        .text(chatResponse)
                        .build());
            } catch (IOException | TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
