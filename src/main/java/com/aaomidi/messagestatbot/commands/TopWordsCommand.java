package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramMessage;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.model.WordData;
import com.aaomidi.messagestatbot.util.Number;
import com.aaomidi.messagestatbot.util.pagination.PaginatedList;
import com.aaomidi.messagestatbot.util.pagination.PaginatedMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-11-27.
 */
public class TopWordsCommand extends TelegramCommand {
    private TreeMap<String, WordData> words = new TreeMap<>();

    public TopWordsCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        Collection<TelegramUser> users = getInstance().getDataManager().getUsers(chat.getId());
        Integer minAmount = -1;
        if (event.getArgs().length >= 1) {
            minAmount = Number.fromString(event.getArgs()[0]);
            if (minAmount == null) {
                minAmount = -1;
            }
        }
        words = new TreeMap<>();

        for (TelegramUser user : users) {
            for (TelegramMessage msg : user.getTextMessages()) {
                for (String word : msg.words()) {
                    if (word.length() < minAmount) continue;
                    if (word.equals("")) continue;
                    updateWord(word);
                }
            }
        }

        ArrayList<WordData> wordsByFrequency = new ArrayList<>(words.values());

        Collections.sort(wordsByFrequency, (o1, o2) -> o2.getCount() - o1.getCount());
        List<String> wordList = new ArrayList<>();
        wordList.add(String.format("Top words with a minimum length of %d are: \n", minAmount == -1 ? 0 : minAmount));

        int i = 0;
        for (WordData wordData : wordsByFrequency) {
            if (i++ == 10) break;

            wordList.add(String.format("%s - %d\n", wordData.getWord(), wordData.getCount()));
        }

        PaginatedMessage paginatedMessage = new PaginatedMessage(
                new PaginatedList(wordList, 10)
        );

        paginatedMessage.setMessage(chat.sendMessage(
                SendableTextMessage.builder()
                        .message(paginatedMessage.getPaginatedList().getCurrentPageContent())
                        .replyMarkup(paginatedMessage.getButtons())
                        .parseMode(ParseMode.NONE)
                        .disableWebPagePreview(true)
                        .build()));
    }

    private void updateWord(String w) {
        WordData res = words.get(w);

        if (res == null) res = new WordData(w);

        res.setCount(res.getCount() + 1);
        words.put(w, res);

    }
}
