package com.aaomidi.handler;

import com.aaomidi.MessageStatBot;
import com.aaomidi.commands.*;
import com.aaomidi.model.TelegramChat;
import com.aaomidi.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;

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

        new ChatCountCommand(instance, "chatcount", "Returns chat count.", true, false);
        new DemoteCommand(instance, "demote", "Demotes a user.", true, true);
        new GetMessagesCommand(instance, "getmessages", "", true, "");
        new TopUsersCommand(instance, "topusers", "Sends the top users of this chat.");
        new TopWordsCommand(instance, "topwords", "Sends the most words used in this chat.");
        new DeveloperCommand(instance, "developer", "Sends information about the developer of this bot.");
        new RandomMessageCommand(instance, "randommessage", "Sends a random message from the specified user.");
        new InfoCommand(instance, "info", "Sends information about this chat.");
        new ListUsersCommand(instance, "listall", "Sends the list of all known users.", "listusers");
        new PingAllCommand(instance, "pingall", "Pings all users.", true);
        new PromoteCommand(instance, "promote", "Promotes a user.", true, true);
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
        User user = event.getMessage().getSender();
        Chat chat = event.getChat();

        TelegramChat telegramChat = instance.getDataManager().getChat(chat.getId());
        TelegramCommand command = commands.get(cmdString);

        if (command == null) return;
        if ((command.isGlobalAdminCommand() && !instance.getDataManager().isAdmin(user)) || (command.isLocalAdminCommand() && !telegramChat.isAdmin(user.getId()) && !instance.getDataManager().isAdmin(user))) {
            SendableTextMessage noPermsMessage = SendableTextMessage.builder()
                    .message("You do not have permissions to execute that command " + user.getFullName())
                    .replyTo(event.getMessage())
                    .build();
            event.getChat().sendMessage(noPermsMessage, instance.getTelegramHook().getBot());
            return;
        }

        command.execute(event);
    }

    public void handleText(final TextMessageReceivedEvent event) {
        commands.values().stream().forEach(c -> c.listen(event));
    }

}
