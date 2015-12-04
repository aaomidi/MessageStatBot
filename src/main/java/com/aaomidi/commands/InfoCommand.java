package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramChat;
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
        TelegramChat telegramChat = getInstance().getDataManager().getChat(chat.getId());

        List<TelegramUser> telegramUsers = telegramChat.getUsers();

        StringBuilder s = new StringBuilder("Here is what I know so far!");
        s.append(String.format("\nThis chat has an ID of %s and I know %d users so far!", chat.getId(), telegramUsers.size()));
        s.append(String.format("\nThere have been a total of %d message sent by all the users!", telegramChat.getAllMessages().size()));
        s.append("\nThe list of admins for this chat are: \n");

        for (TelegramUser u : telegramChat.getAdmins()) {
            s.append(String.format("\t%s - %s %s\n", u.getName(), u.getUsername() == null ? "" : u.getUsername(), getInstance().getDataManager().isAdmin(u.getId()) ? "(Global Admin)" : ""));
        }

        s.append("You can enter /listusers to see all the users of this chat!");

        chat.sendMessage(s.toString(), getTelegramBot());

    }
}
