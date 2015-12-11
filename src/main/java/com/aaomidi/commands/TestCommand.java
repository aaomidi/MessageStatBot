package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.mcapi.MCAPI;
import com.aaomidi.mcapi.api.queries.SendableUUIDQuery;
import com.aaomidi.mcapi.api.uuid.UUIDResponse;
import com.aaomidi.model.TelegramCommand;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-12-05.
 */
public class TestCommand extends TelegramCommand {
    public TestCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        try {
            if (event.getArgs().length == 0) return;

            MCAPI mcapi = MCAPI.getInstance();

/*            IconResponse response = (IconResponse) mcapi.sendQuery(SendableIconQuery
                    .builder()
                    .serverHost(event.getArgsString()).build());

            SendablePhotoMessage textMessage = SendablePhotoMessage.builder()
                    .photo(new InputFile(response.getIcon())).build(); */

    /*    InfoResponse response = (InfoResponse) mcapi.sendQuery(
                SendableInfoQuery.builder().serverHost(event.getArgsString()).build()
        );

        SendableTextMessage textMessage = SendableTextMessage.builder()
                .message("MOTD: " + response.getMotd())
                .build(); */

            UUIDResponse response = (UUIDResponse) mcapi.sendQuery(SendableUUIDQuery.builder().username(event.getArgsString()).build());

            SendableTextMessage textMessage = SendableTextMessage.builder()
                    .message("UUID1: " + response.getUuid() + "\nUUID2: " + response.getUuid_formatted())
                    .build();


            event.getChat().sendMessage(textMessage, getTelegramBot());
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

    }
}
