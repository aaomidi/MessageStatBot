package com.aaomidi.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.list.TreeList;

import java.util.List;

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
}
