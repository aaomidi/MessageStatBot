package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramChat;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.util.pagination.PaginatedList;
import com.aaomidi.messagestatbot.util.pagination.PaginatedMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

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

        List<String> userList = new ArrayList<>();
        userList.add("The list of users I know so far: \n");

        int i = 0;
        List<TelegramUser> users = new ArrayList<>(telegramChat.getUsers().values());
        Collections.sort(users, (o1, o2) -> {
            if (o1.getId() == o2.getId()) return 0;
            return o1.getId() > o2.getId() ? 1 : -1;
        });


        for (TelegramUser tu : users) {
            StringBuilder userLine = new StringBuilder()

                    .append("\t")
                    .append(++i)
                    .append(". ")
                    .append(tu.getId())
                    .append(" - ")
                    .append(tu.getName());
            if (tu.getUsername() != null)
                userLine.append(" - ").append(tu.getUsername());

            userList.add(userLine.toString());
        }

        PaginatedMessage paginatedMessage = new PaginatedMessage(
                new PaginatedList(userList, 10)
        );

        paginatedMessage.setMessage(chat.sendMessage(
                SendableTextMessage.builder()
                        .message(paginatedMessage.getPaginatedList().getCurrentPageContent())
                        .replyMarkup(paginatedMessage.getButtons())
                        .parseMode(ParseMode.NONE)
                        .disableWebPagePreview(true)
                        .build()));
    }
}