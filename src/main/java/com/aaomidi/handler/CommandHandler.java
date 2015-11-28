package com.aaomidi.handler;

import com.aaomidi.MessageStatBot;
import com.aaomidi.commands.*;
import com.aaomidi.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

import java.util.HashMap;

/**
 * Created by amir on 2015-11-26.
 */
public class CommandHandler {
    private final MessageStatBot instance;
    private HashMap<String, TelegramCommand> commands;

    private TelegramCommand statCounter;

    public CommandHandler(MessageStatBot instance) {
        this.instance = instance;
        commands = new HashMap<>();
    }

    public void registerCommands() {
        statCounter = new StatCounter(instance, "statcounter", "Does nothing");
        new GetMessagesCommand(instance, "getmessages", "");
        new TopUsersCommand(instance, "topusers", "");
        new TopWordsCommand(instance, "topwords", "");
        new DeveloperCommand(instance, "developer", "");
        new RandomMessageCommand(instance, "randommessage", "");
        new InfoCommand(instance, "info", "");
        // new PingAllCommand(instance, "pingall", "");
    }

    public void registerCommand(TelegramCommand telegramCommand) {
        commands.put(telegramCommand.getName().toLowerCase(), telegramCommand);

        for (String alias : telegramCommand.getAliases()) {
            commands.put(alias.toLowerCase(), telegramCommand);
        }
    }

    public void handleCommand(CommandMessageReceivedEvent event) {
        {
            statCounter.execute(event);
        }

        String cmdString = event.getCommand();
        cmdString = cmdString.toLowerCase();

        TelegramCommand command = commands.get(cmdString);

        if (command == null) return;

        command.execute(event);
    }

    public void handleText(final TextMessageReceivedEvent event) {
        commands.values().stream().forEach(c -> c.listen(event));
    }

}
