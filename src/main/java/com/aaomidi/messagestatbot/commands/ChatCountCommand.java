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

    public ChatCountCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Message message = event.getMessage();

        SendableTextMessage textMessage = SendableTextMessage.builder()
                .message(String.format("I am in %d chats! I keep a track of %d users! I have %d messages, and %d words D: statistisized; whew...", getInstance().getDataManager().getChatCount(), getInstance().getDataManager().getUserCount(), getInstance().getDataManager().getMessageCount(), getInstance().getDataManager().getWordCount()))
                .replyTo(message)
                .build();

        event.getChat().sendMessage(textMessage, getTelegramBot());
    }
}
