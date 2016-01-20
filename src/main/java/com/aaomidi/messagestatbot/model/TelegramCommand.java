package com.aaomidi.messagestatbot.model;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.util.LogHandler;
import lombok.Getter;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

/**
 * Created by amir on 2015-11-26.
 */
public abstract class TelegramCommand {
    @Getter
    private final MessageStatBot instance;
    @Getter
    private final String name;
    @Getter
    private final String description;
    @Getter
    private final boolean globalAdminCommand;
    @Getter
    private final boolean localAdminCommand;
    @Getter
    private final String[] aliases;

    public TelegramCommand(MessageStatBot instance, String name, String description, boolean globalAdminCommand, boolean localAdminCommand, String... aliases) {
        this.instance = instance;
        this.name = name;
        this.description = description;
        this.globalAdminCommand = globalAdminCommand;
        this.localAdminCommand = localAdminCommand;
        this.aliases = aliases;

        // Auto register commands
        this.instance.getCommandHandler().registerCommand(this);
        this.generateBotFatherString();

    }

    /**
     * Registers a normal command
     *
     * @param instance
     * @param name
     * @param description
     * @param aliases
     */
    public TelegramCommand(MessageStatBot instance, String name, String description, String... aliases) {
        this(instance, name, description, false, aliases);
    }

    /**
     * Registers a locally admin command
     *
     * @param instance
     * @param name
     * @param description
     * @param localAdminCommand
     * @param aliases
     */
    public TelegramCommand(MessageStatBot instance, String name, String description, boolean localAdminCommand, String... aliases) {

        this(instance, name, description, false, localAdminCommand, aliases);

    }


    public abstract void execute(CommandMessageReceivedEvent event);

    public void listen(TextMessageReceivedEvent event) {
        // Override pls
    }

    protected TelegramBot getTelegramBot() {
        return instance.getTelegramHook().getBot();
    }

    public void generateBotFatherString() {
        LogHandler.logn("%s - %s", getName(), getDescription());
    }
}
