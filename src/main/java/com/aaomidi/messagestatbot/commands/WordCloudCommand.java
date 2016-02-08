package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramChat;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramMessage;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.InputFile;
import pro.zackpollard.telegrambot.api.chat.message.send.SendablePhotoMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.internal.managers.FileManager;
import wordcloud.CollisionMode;
import wordcloud.WordCloud;
import wordcloud.WordFrequency;
import wordcloud.bg.CircleBackground;
import wordcloud.font.scale.SqrtFontScalar;
import wordcloud.nlp.FrequencyAnalyzer;
import wordcloud.palette.ColorPalette;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by amir on 2016-02-08.
 */
public class WordCloudCommand extends TelegramCommand {
    public WordCloudCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        TelegramChat telegramChat = getInstance().getDataManager().getChat(chat.getId());

        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequencesToReturn(300);
        frequencyAnalyzer.setMinWordLength(3);
        List<WordFrequency> wordFrequencyList = frequencyAnalyzer.load(telegramChat.getAllMessages().stream().map(TelegramMessage::getMessage).collect(Collectors.toList()));
        WordCloud wordCloud = new WordCloud(600, 600, CollisionMode.PIXEL_PERFECT);
        wordCloud.setPadding(2);
        wordCloud.setBackground(new CircleBackground(300));
        wordCloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        wordCloud.setFontScalar(new SqrtFontScalar(10, 40));
        wordCloud.build(wordFrequencyList);

        File file = null;
        try {
            file = File.createTempFile("wordcloud" + System.currentTimeMillis(), ".png", FileManager.getTemporaryFolder());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        if (!file.exists()) {
            return;
        }
        wordCloud.writeToFile(file.getPath());
        SendablePhotoMessage message = SendablePhotoMessage.builder()
                .photo(new InputFile(file))
                .replyTo(event.getMessage())
                .build();
        getTelegramBot().sendMessage(chat, message);
    }
}
