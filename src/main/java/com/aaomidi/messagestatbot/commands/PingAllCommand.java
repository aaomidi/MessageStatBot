package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramUser;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Collection;

/**
 * Created by amir on 2015-11-27.
 */
public class PingAllCommand extends TelegramCommand {


    public PingAllCommand(MessageStatBot instance, String name, String description, boolean localAdminCommand, String... aliases) {
        super(instance, name, description, localAdminCommand, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        Collection<TelegramUser> users = getInstance().getDataManager().getUsers(chat.getId());


        StringBuilder sb = new StringBuilder();
        for (TelegramUser u : users) {
            sb.append("@").append(u.getUsername()).append(" ");
        }

        chat.sendMessage(sb.toString(), getTelegramBot());
    }
}
