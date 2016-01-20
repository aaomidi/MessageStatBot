package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramChat;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import com.aaomidi.util.IntegerConverter;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-12-05.
 */
public class PromoteCommand extends TelegramCommand {


    public PromoteCommand(MessageStatBot instance, String name, String description, boolean globalAdminCommand, boolean localAdminCommand, String... aliases) {
        super(instance, name, description, globalAdminCommand, localAdminCommand, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        if (event.getArgs().length == 0) return;
        Chat chat = event.getChat();
        TelegramChat telegramChat = getInstance().getDataManager().getChat(chat.getId());
        Message message = event.getMessage();

        String userIDString = event.getArgs()[0];
        Integer userID = IntegerConverter.fromString(userIDString);
        SendableTextMessage errorMessage = SendableTextMessage.builder()
                .message("That user was not recognized")
                .replyTo(message)
                .build();

        SendableTextMessage successMessage = SendableTextMessage.builder()
                .message("That action was successful.")
                .replyTo(message)
                .build();

        if (userID == null) {
            chat.sendMessage(errorMessage, getTelegramBot());
            return;
        }

        TelegramUser user = telegramChat.getUser(userID);

        if (user == null) {
            chat.sendMessage(errorMessage, getTelegramBot());
            return;
        }

        user.setIsAdmin(true);
        chat.sendMessage(successMessage, getTelegramBot());
    }
}
