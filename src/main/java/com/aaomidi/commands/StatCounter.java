package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramMessage;
import com.aaomidi.model.TelegramUser;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;

/**
 * Created by amir on 2015-11-27.
 */
public class StatCounter extends TelegramCommand {
    public StatCounter(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        Message message = event.getMessage();
        String content = event.getArgsString();

        content = String.format("%s %s", event.getCommand(), content);

        this.handle(message, chat, content, TelegramMessage.Type.COMMAND);
    }

    @Override
    public void listen(TextMessageReceivedEvent event) {
        Chat chat = event.getChat();
        Message message = event.getMessage();
        String content = event.getContent().getContent();

        this.handle(message, chat, content, TelegramMessage.Type.TEXT_MESSAGE);
    }

    private void handle(Message message, Chat chat, String content, TelegramMessage.Type type) {
        User user = message.getSender();

        { // Initalize files incase they don't exist
            getInstance().getDataManager().initializeChat(chat.getId());
            getInstance().getDataManager().initializeUser(chat.getId(), user.getId(), user);
        }

        TelegramUser telegramUser = getInstance().getDataManager().getUser(chat.getId(), user.getId());
        TelegramMessage telegramMessage = new TelegramMessage(message.getTimeStamp(), message.getMessageId(), content, type);


        telegramUser.updateInformation(user);
        telegramUser.say(telegramMessage);
    }
}
