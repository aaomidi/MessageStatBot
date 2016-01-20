package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramChat;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.List;

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
        StringBuilder sb = new StringBuilder();

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
                    sb.append(String.format("%d - %s", user.getWordCount(), user.getName()));

                    if (user.getUsername() != null)
                        sb.append(String.format(" - %s", user.getUsername()));
                    sb.append("\n");
                }
                break;

            }

            case "ratio": {
                List<TelegramUser> list = telegramChat.getTopUsersByRatio();

                int i = 0;
                for (TelegramUser user : list) {
                    if (i++ == NUM_OF_USER) break;
                    sb.append(String.format("%.2f - %s", user.getWordCount() / (double) user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null)
                        sb.append(String.format("- %s", user.getUsername()));
                    sb.append("\n");
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
                    sb.append(String.format("%d - %s", user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null)
                        sb.append(String.format(" - %s", user.getUsername()));
                    sb.append("\n");
                }
                break;
            }

        }


        chat.sendMessage(sb.toString(), getTelegramBot());
    }
}
