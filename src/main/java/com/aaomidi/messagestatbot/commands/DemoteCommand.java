package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramChat;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.util.IntegerConverter;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-12-05.
 */
public class DemoteCommand extends TelegramCommand {
    public DemoteCommand(MessageStatBot instance, String name, String description, boolean globalAdminCommand, boolean localAdminCommand, String... aliases) {
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

        user.setIsAdmin(false);
        chat.sendMessage(successMessage, getTelegramBot());
    }
}
