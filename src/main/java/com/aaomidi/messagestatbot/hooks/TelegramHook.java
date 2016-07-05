package com.aaomidi.messagestatbot.hooks;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.util.LogHandler;
import com.aaomidi.messagestatbot.util.pagination.PaginatedMessage;

import java.util.UUID;

import lombok.Getter;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.CallbackQueryReceivedEvent;
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

    @Override
    public void onCallbackQueryReceivedEvent(CallbackQueryReceivedEvent event) {
        String ID = event.getCallbackQuery().getData();

            /**
             * Pagination via UUID
             */
            String action;
            UUID uuid;

            try {
                uuid = UUID.fromString(ID.split("\\|")[0]);
                action = ID.split("\\|")[1];

                PaginatedMessage paginatedMessage = MessageStatBot.getInstance().getPaginationHandler().getMessage(uuid);

                if (paginatedMessage != null) {
                    String content;
                    switch (action) {
                        case "next": {
                            content = paginatedMessage.getPaginatedList().switchToNextPage();
                            break;
                        }
                        case "prev": {
                            content = paginatedMessage.getPaginatedList().switchToPreviousPage();
                            break;
                        }
                        case "ignore": {
                            event.getCallbackQuery().answer("Use Next or Previous to navigate.", true);
                            return;
                        }
                        default: {
                            event.getCallbackQuery().answer("Unable to continue! Contact @aaomidi", true);
                            return;
                        }
                    }

                    MessageStatBot.getInstance().getTelegramHook().getBot().editMessageText(
                            paginatedMessage.getMessage(), content, ParseMode.NONE, false, paginatedMessage.getButtons()
                    );
                    return;
                }
            } catch (Exception ignore) {

            }


        event.getCallbackQuery().answer("Unknown action! Button ID: " + ID, true);
    }
}

