package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

public class StartCommand extends TelegramCommand {

    public PingAllCommand(MessageStatBot instance, String name, String description, boolean localAdminCommand, String... aliases) {
        super(instance, name, description, localAdminCommand, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();

        chat.sendMessage("I will generate statistics about your group chats. Simply add me to your group to get started!", getTelegramBot());
    }
}
