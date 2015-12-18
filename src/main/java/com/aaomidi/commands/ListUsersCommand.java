package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramChat;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by amir on 2015-12-05.
 */
public class ListUsersCommand extends TelegramCommand {
    public ListUsersCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        TelegramChat telegramChat = getInstance().getDataManager().getChat(chat.getId());
        StringBuilder sb = new StringBuilder("The list of users I know so far: \n");

        int i = 0;
        List<TelegramUser> users = new ArrayList<>(telegramChat.getUsers().values());
        Collections.sort(users, (o1, o2) -> {
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() > o2.getId() ? 1 : -1;
        });

        for (TelegramUser tu : users) {
            sb
                    .append("\t")
                    .append(++i)
                    .append(". ")
                    .append(tu.getId())
                    .append(" - ")
                    .append(tu.getName());
            if (tu.getUsername() != null)
                sb.append(" - ").append(tu.getUsername());
            sb.append("\n");
        }
        SendableTextMessage textMessage = SendableTextMessage.builder()
                .message(sb.toString())
                .replyTo(event.getMessage())
                .build();

        chat.sendMessage(textMessage, getTelegramBot());
    }
}