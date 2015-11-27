package com.aaomidi.handler;

import com.aaomidi.MessageStatBot;
import com.aaomidi.commands.GetMessagesCommand;
import com.aaomidi.commands.StatCounter;
import com.aaomidi.commands.TopUsersCommand;
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
