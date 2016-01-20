package com.aaomidi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.list.TreeList;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by amir on 2015-11-29.
 */
@RequiredArgsConstructor
public class TelegramChat {
    @Getter
    private final String chatID;
    @Getter
    private Map<String, TelegramUser> users = new HashMap<>();

    public void addUser(TelegramUser x) {
        users.put(x.getUsername() != null ? x.getUsername().toLowerCase() : String.valueOf(x.getId()), x);
    }

    public List<TelegramUser> getAdmins() {
        return users.values().stream().filter(TelegramUser::isAdmin).collect(Collectors.toCollection(ArrayList::new));
    }

    public boolean isAdmin(int userID) {
        try {
            TelegramUser tu = getAdmins().stream().filter(t -> t.getId() == userID).findFirst().get();
            return tu != null && tu.isAdmin();
        } catch (Exception ex) {
            return false;
        }
    }

    public List<TelegramMessage> getAllMessages() {
        List<TelegramMessage> messages = new TreeList<>();
        users.values().stream().forEach(t -> messages.addAll(t.getMessages()));

        return messages;
    }

    public TelegramUser getUser(int userID) {
        return users.values().stream().filter(u -> u.getId() == userID).findFirst().get();
    }

    public TelegramUser getUser(String username) {
        username = username.toLowerCase();
        if (username.charAt(0) == '@') username = username.substring(1);

        return users.get(username);
    }

    public TelegramUser getUserByName(String name) {
        for (TelegramUser telegramUser : users.values()) {
            if (telegramUser.getName().equalsIgnoreCase(name)) {
                return telegramUser;
            }
        }
        return null;
    }

    public List<TelegramUser> getTopUsersByMessageCount() {

        List<TelegramUser> list = new TreeList<>(users.values());

        Collections.sort(list, (o1, o2) -> {
            if (o1.getMessages().size() == o2.getMessages().size()) return 0;

            return o1.getMessages().size() > o2.getMessages().size() ? -1 : 1;
        });

        return list;
    }

    public List<TelegramUser> getTopUsersByRatio() {
        List<TelegramUser> list = new TreeList<>(users.values());

        Collections.sort(list, (o1, o2) -> {
            if (o1.getWordCount() / (double) o1.getMessages().size() == o2.getWordCount() / (double) o2.getMessages().size())
                return 0;

            return o1.getWordCount() / (double) o1.getMessages().size() > o2.getWordCount() / (double) o2.getMessages().size() ? -1 : 1;
        });

        return list;
    }

    public List<TelegramUser> getTopUsersByWordsCount() {
        List<TelegramUser> list = new TreeList<>(users.values());

        Collections.sort(list, (o1, o2) -> {
            if (o1.getWordCount() == o2.getWordCount()) return 0;

            return o1.getWordCount() > o2.getWordCount() ? -1 : 1;
        });

        return list;
    }

    public Chat toChat() {
        return TelegramBot.getChat(chatID);
    }
}
