package com.aaomidi.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.list.TreeList;
import pro.zackpollard.telegrambot.api.user.User;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by amir on 2015-11-27.
 */
public class TelegramUser {
    @Getter
    private final int id;
    @Getter
    private String name;
    private String username;
    @Getter
    private List<TelegramMessage> messages = new TreeList<>();
    @Getter
    private boolean isAdmin = false;
    @Getter
    @Setter
    private transient boolean changesMade = false;
    public TelegramUser(User user) {
        this.id = user.getId();
        updateInformation(user);
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        setChangesMade(true);
    }

    public String getUsername() {
        if (username == null) return null;

        if (username.equals("")) return null;
        return username;
    }

    public void updateInformation(User user) {
        this.name = user.getFullName();
        this.username = user.getUsername();
        changesMade = true;
    }

    public int getWordCount() {
        int count = 0;
        for (TelegramMessage m : messages) {
            count += m.getWordCount();
        }
        return count;
    }

    public TelegramMessage getRandomMessage() {
        return getRandomMessage(-1);
    }

    public TelegramMessage getRandomMessage(int minCharacters) {
        // Micro optimization. Also this should never happen.
        if (messages.size() == 0) return null;

        int count = 0;
        TelegramMessage tg;

        do {
            int r = ThreadLocalRandom.current().nextInt(messages.size());
            tg = messages.get(r);
        }
        while (tg.getType() != TelegramMessage.Type.TEXT_MESSAGE || tg.getMessage().length() < minCharacters && (count++ < 10000 || count < messages.size()));

        if (tg.getMessage().length() < minCharacters) return null;

        return tg;
    }

    public List<TelegramMessage> getTextMessages() {
        List<TelegramMessage> list = new TreeList<>();

        this.getMessages().stream().filter(t -> t.getType() == TelegramMessage.Type.TEXT_MESSAGE).forEach(list::add);

        return list;
    }

    public void say(TelegramMessage msg) {
        messages.add(msg);
        changesMade = true;
    }

}
