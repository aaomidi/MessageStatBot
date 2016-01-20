package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-11-27.
 */
public class DeveloperCommand extends TelegramCommand {
    public DeveloperCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        event.getChat().sendMessage("Developed by @aaomidi", getTelegramBot());
    }
}
