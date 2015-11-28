package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.List;

/**
 * Created by amir on 2015-11-29.
 */
public class InfoCommand extends TelegramCommand {
    public InfoCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        List<TelegramUser> telegramUsers = getInstance().getDataManager().getUsers(chat.getId());

        StringBuilder s = new StringBuilder("Here is what I know so far!");
        s.append(String.format("\nThis chat has an ID of %s and I know %d users so far!", chat.getId(), telegramUsers.size()));
       // s.append(String.format("\nThere have been a total of %d message sent by all the users!"))
        chat.sendMessage(s.toString(), getTelegramBot());

    }
}
