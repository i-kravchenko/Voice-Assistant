package com.example.VoiceAssistant.tools;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import java.io.*;

@Component
public class OggConverter
{
    @Value("${ffmpeg.path}")
    private String ffmpegPath;

    public File convertOggToMp3(File source) throws IOException {
        File target = File.createTempFile("tmp", ".mp3");
        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(source.getAbsolutePath())
                .overrideOutputFiles(true)
                .addOutput(target.getAbsolutePath())
                .setAudioCodec("libmp3lame")
                .setAudioBitRate(32768)
                .done();
        FFmpegExecutor executor = new FFmpegExecutor(new FFmpeg(ffmpegPath));
        executor.createJob(builder).run();
        try {
            executor.createTwoPassJob(builder).run();
        } catch (IllegalArgumentException ignored) {}
        return target;
    }
}
