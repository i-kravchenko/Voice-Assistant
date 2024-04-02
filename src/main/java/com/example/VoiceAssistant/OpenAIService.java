package com.example.VoiceAssistant;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiAudioTranscriptionClient;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.ai.openai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class OpenAIService {
    private final OpenAiAudioTranscriptionClient audioClient;
    private final OpenAiChatClient chatClient;

    public OpenAIService(
            @Value("${spring.ai.openai.api-key}") String apiKey,
            @Value("${spring.ai.openai.api-model}") String apiModel
    ) {
        audioClient = new OpenAiAudioTranscriptionClient(new OpenAiAudioApi(apiKey));
        chatClient = new OpenAiChatClient(
                new OpenAiApi(apiKey),
                OpenAiChatOptions.builder()
                        .withModel(apiModel)
                        .withTemperature(0f)
                        .withMaxTokens(200)
                        .build());
    }

    public String transcript(Resource audioFile) {
        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .withTemperature(0f)
                .build();
        return audioClient
                .call(new AudioTranscriptionPrompt(audioFile, transcriptionOptions))
                .getResult()
                .getOutput();
    }

    public String chat(String request) {
        return chatClient
                .call(new Prompt(request))
                .getResult()
                .getOutput()
                .getContent();
    }
}
