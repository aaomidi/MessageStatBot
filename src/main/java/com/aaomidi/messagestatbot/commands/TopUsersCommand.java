package com.aaomidi.messagestatbot.commands;

import com.aaomidi.messagestatbot.MessageStatBot;
import com.aaomidi.messagestatbot.model.TelegramChat;
import com.aaomidi.messagestatbot.model.TelegramCommand;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.util.pagination.PaginatedList;
import com.aaomidi.messagestatbot.util.pagination.PaginatedMessage;

import java.util.ArrayList;
import java.util.List;

import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.message.send.ParseMode;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableTextMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

/**
 * Created by amir on 2015-11-27.
 */
public class TopUsersCommand extends TelegramCommand {
    private final static int NUM_OF_USER = 20;

    public TopUsersCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        TelegramChat telegramChat = getInstance().getDataManager().getChat(chat.getId());
        List<String> messageList = new ArrayList<>();

        String arg = "messages";
        if (event.getArgs().length > 0) {
            arg = event.getArgs()[0];
        }

        switch (arg.toLowerCase()) {

            case "words": {
                List<TelegramUser> list = telegramChat.getTopUsersByWordsCount();

                int i = 0;
                for (TelegramUser user : list) {
                    if (i++ == NUM_OF_USER) break;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("%d - %s", user.getWordCount(), user.getName()));

                    if (user.getUsername() != null)
                        stringBuilder.append(String.format(" - %s", user.getUsername()));
                    stringBuilder.append("\n");

                    messageList.add(stringBuilder.toString());
                }
                break;

            }

            case "ratio": {
                List<TelegramUser> list = telegramChat.getTopUsersByRatio();

                int i = 0;
                for (TelegramUser user : list) {
                    if (i++ == NUM_OF_USER) break;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("%.2f - %s", user.getWordCount() / (double) user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null)
                        stringBuilder.append(String.format("- %s", user.getUsername()));
                    stringBuilder.append("\n");

                    messageList.add(stringBuilder.toString());
                }
                break;
            }

            case "messages":
                // Follow to default
            default: {
                List<TelegramUser> list = telegramChat.getTopUsersByMessageCount();

                int i = 0;
                for (TelegramUser user : list) {
                    if (i++ == NUM_OF_USER) break;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("%d - %s", user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null)
                        stringBuilder.append(String.format(" - %s", user.getUsername()));
                    stringBuilder.append("\n");

                    messageList.add(stringBuilder.toString());
                }
                break;
            }

        }

        PaginatedMessage paginatedMessage = new PaginatedMessage(
                new PaginatedList(messageList, 10)
        );

        paginatedMessage.setMessage(chat.sendMessage(
                SendableTextMessage.builder()
                        .message(paginatedMessage.getPaginatedList().getCurrentPageContent())
                        .replyMarkup(paginatedMessage.getButtons())
                        .parseMode(ParseMode.NONE)
                        .disableWebPagePreview(true)
                        .build()));
    }
}
