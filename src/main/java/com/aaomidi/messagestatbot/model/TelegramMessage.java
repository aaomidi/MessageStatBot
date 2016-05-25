package com.aaomidi.messagestatbot.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by amir on 2015-11-27.
 */
@RequiredArgsConstructor
public class TelegramMessage {
    @Getter
    private final long timestamp;
    @Getter
    private final long id;
    @Getter
    private final String message;
    @Getter
    private final Type type;


    /**
     * Gets the number of words in the message.
     *
     * @return Number of words in the message.
     */
    public int getWordCount() {
        int count = 1; // The smallest message has to have at least one word.

        for (char c : message.toCharArray()) {
            if (c != ' ') continue;
            count++;
        }

        return count;
    }

    public String[] words(){
        // Thanks to @Earth2Me
        return message.split("[^\\pL\\pN\\p{Pc}]+");
    }

    public enum Type {
        TEXT_MESSAGE,
        COMMAND;
    }
}
