package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import com.aaomidi.util.IntegerConverter;
import com.aaomidi.util.LogHandler;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.List;

/**
 * Created by amir on 2015-11-29.
 */
public class RandomMessageCommand extends TelegramCommand {
    public RandomMessageCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        if (event.getArgs().length == 0) return;

        Chat chat = event.getChat();
        String name = event.getArgs()[0];

        for (int i = 1; i < event.getArgs().length - 1; i++) {
            name = String.format("%s %s", name, event.getArgs()[i]);
        }
        String s = event.getArgs()[event.getArgs().length - 1];

        Integer minChar = IntegerConverter.fromString(s);

        if (minChar == null && event.getArgs().length > 1) {
            name = String.format("%s %s", name, event.getArgs()[event.getArgs().length - 1]);
        }

        LogHandler.logn(name);
        List<TelegramUser> telegramUsers = getInstance().getDataManager().getUsers(chat.getId());

        TelegramUser telegramUser = null;

        for (TelegramUser t : telegramUsers) {
            if (t.getUsername().equalsIgnoreCase(name) || t.getName().equalsIgnoreCase(name)) {
                telegramUser = t;
                break;
            }
        }

        if (telegramUser == null) return;

        String randomMessage;

        if (minChar != null) {
            randomMessage = telegramUser.getRandomMessage(minChar).getMessage();
        } else {
            randomMessage = telegramUser.getRandomMessage().getMessage();
        }

        if (randomMessage == null) return;

        chat.sendMessage(randomMessage, getTelegramBot());
    }
}
