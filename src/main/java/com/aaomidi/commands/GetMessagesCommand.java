package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramMessage;
import com.aaomidi.model.TelegramUser;
import com.aaomidi.util.IntegerConverter;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-11-27.
 */
public class GetMessagesCommand extends TelegramCommand {

    public GetMessagesCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Integer userID = IntegerConverter.fromString(event.getArgsString());
        Chat chat = event.getChat();
        if (userID == null) return;

        TelegramUser telegramUser = getInstance().getDataManager().getUser(chat.getId(), userID);
        if (telegramUser == null) return;

        StringBuilder sb = new StringBuilder();

        for (TelegramMessage m : telegramUser.getMessages()) {
            sb.append(m.getMessage()).append("\n");
        }

        chat.sendMessage(sb.toString(), getTelegramBot());
    }
}
