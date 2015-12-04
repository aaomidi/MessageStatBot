package com.aaomidi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.list.TreeList;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by amir on 2015-11-29.
 */
@RequiredArgsConstructor
public class TelegramChat {
    @Getter
    private final String chatID;
    @Getter
    private List<TelegramUser> users = new TreeList<>();

    public void addUser(TelegramUser x) {
        users.add(x);
    }

    public List<TelegramUser> getAdmins() {
        return users.stream().filter(user -> user.isAdmin()).collect(Collectors.toCollection(() -> new TreeList<>()));
    }

    public List<TelegramMessage> getAllMessages() {
        List<TelegramMessage> messages = new TreeList<>();
        users.stream().forEach(t -> messages.addAll(t.getMessages()));

        return messages;
    }

    public TelegramUser getUser(int userID) {
        return users.stream().filter(u -> u.getId() == userID).findFirst().get();
    }

    public List<TelegramUser> getTopUsersByMessageCount() {
        List<TelegramUser> list = new TreeList<>(users);

        Collections.sort(list, (o1, o2) -> {
            if (o1.getMessages().size() == o2.getMessages().size()) return 0;

            return o1.getMessages().size() > o2.getMessages().size() ? -1 : 1;
        });

        return list;
    }

    public List<TelegramUser> getTopUsersByRatio() {
        List<TelegramUser> list = new TreeList<>(users);

        Collections.sort(list, (o1, o2) -> {
            if (o1.getWordCount() / (double) o1.getMessages().size() == o2.getWordCount() / (double) o2.getMessages().size())
                return 0;

            return o1.getWordCount() / (double) o1.getMessages().size() > o2.getWordCount() / (double) o2.getMessages().size() ? -1 : 1;
        });

        return list;
    }

    public List<TelegramUser> getTopUsersByWordsCount() {
        List<TelegramUser> list = new TreeList<>(users);

        Collections.sort(list, (o1, o2) -> {
            if (o1.getWordCount() == o2.getWordCount()) return 0;

            return o1.getWordCount() > o2.getWordCount() ? -1 : 1;
        });

        return list;
    }
}
