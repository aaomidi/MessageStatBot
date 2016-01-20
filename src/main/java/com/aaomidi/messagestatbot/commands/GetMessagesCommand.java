package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramMessage;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.util.IntegerConverter;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-11-27.
 */
public class GetMessagesCommand extends TelegramCommand {


    public GetMessagesCommand(MessageStatBot instance, String name, String description, boolean localAdminCommand, String... aliases) {
        super(instance, name, description, localAdminCommand, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Integer userID = IntegerConverter.fromString(event.getArgs()[0]);
        Chat chat = event.getChat();

        TelegramUser telegramUser = null;

        if (userID == null) {

            telegramUser = getInstance().getDataManager().getChat(chat.getId()).getUser(event.getArgs()[0]);
        } else {

            telegramUser = getInstance().getDataManager().getUser(chat.getId(), userID);
        }

        if (telegramUser == null) {

            event.getChat().sendMessage(SendableTextMessage.builder().message("User was not found, please provide the ID or Username of the user.").replyTo(event.getMessage()).build(), getTelegramBot());
            return;
        }

        StringBuilder sb = new StringBuilder();

        for (TelegramMessage m : telegramUser.getMessages()) {
            sb.append(m.getMessage()).append("\n");
        }

        chat.sendMessage(sb.toString(), getTelegramBot());
    }
}
