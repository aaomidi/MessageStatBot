package com.aaomidi.hooks;

import com.aaomidi.MessageStatBot;
import com.aaomidi.util.LogHandler;
import lombok.Getter;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.event.chat.message.TextMessageReceivedEvent;

/**
 * Created by amir on 2015-11-26.
 */
public class TelegramHook implements Listener {
    private final MessageStatBot instance;
    @Getter
    private TelegramBot bot;

    public TelegramHook(MessageStatBot instance, String auth) {
        this.instance = instance;

        bot = TelegramBot.login(auth);

        bot.getEventsManager().register(this);

        bot.startUpdates(true);

        //Chat mazenChat = TelegramBot.getChat(-17349250);
        //mazenChat.sendMessage("I LIKE TITS!!! @zackpollard", bot);
    }

    @Override
    public void onCommandMessageReceived(CommandMessageReceivedEvent event) {
        // if (event.getChat().getId().equals("-17349250")) return;

        LogHandler.logn("Command received: %s", event.getContent().getContent());

        instance.getCommandHandler().handleCommand(event);
    }

    @Override
    public void onTextMessageReceived(TextMessageReceivedEvent event) {
        LogHandler.logn("Message received %s - %s (%d): %s", event.getChat().getId(), event.getMessage().getSender().getFullName(), event.getMessage().getSender().getId(), event.getContent().getContent());

        /*if (event.getChat().getId().equals("55395012") && event.getMessage().getSender().getId() == 55395012) {
            String message = event.getContent().getContent();

            for (TelegramChat telegramChat : instance.getDataManager().getChats()) {
                Chat chat = telegramChat.toChat();
                if (chat == null)
                    continue;
                chat.sendMessage(message, bot);
            }
        } */

        instance.getCommandHandler().handleText(event);
    }
}

