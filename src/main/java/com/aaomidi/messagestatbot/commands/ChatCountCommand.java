package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2016-01-08.
 */
public class ChatCountCommand extends TelegramCommand {

    public ChatCountCommand(MessageStatBot instance, String name, String description, boolean globalAdminCommand, boolean localAdminCommand, String... aliases) {
        super(instance, name, description, globalAdminCommand, localAdminCommand, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Message message = event.getMessage();

        SendableTextMessage textMessage = SendableTextMessage.builder()
                .message(String.format("I am in %d chats! I keep a track of %d users!", getInstance().getDataManager().getChatCount(), getInstance().getDataManager().getUserCount()))
                .replyTo(message)
                .build();

        event.getChat().sendMessage(textMessage, getTelegramBot());
    }
}
