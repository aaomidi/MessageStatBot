package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramUser;
import org.apache.commons.collections4.list.TreeList;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;

import java.util.Collections;

/**
 * Created by amir on 2015-11-27.
 */
public class TopUsersCommand extends TelegramCommand {
    public TopUsersCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        Chat chat = event.getChat();
        TreeList<TelegramUser> users = getInstance().getDataManager().getUsers(chat.getId());
        StringBuilder sb = new StringBuilder();


        if (event.getArgs().length == 0) return;

        switch (event.getArgs()[0].toLowerCase()) {
            case "messages": {
                Collections.sort(users, (o1, o2) -> {
                    if (o1.getMessages().size() == o2.getMessages().size()) return 0;

                    return o1.getMessages().size() > o2.getMessages().size() ? -1 : 1;
                });
                int i = 0;
                for (TelegramUser user : users) {
                    if (i++ == 5) break;
                    sb.append(String.format("%d - %s", user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null) sb.append(String.format("(%s)", user.getUsername()));
                    sb.append("\n");
                }
                break;
            }
            case "words": {
                Collections.sort(users, (o1, o2) -> {
                    if (o1.getWordCount() == o2.getWordCount()) return 0;

                    return o1.getWordCount() > o2.getWordCount() ? -1 : 1;
                });
                int i = 0;
                for (TelegramUser user : users) {
                    if (i++ == 5) break;
                    sb.append(String.format("%d - %s", user.getWordCount(), user.getName()));

                    if (user.getUsername() != null) sb.append(String.format("(%s)", user.getUsername()));
                    sb.append("\n");
                }
                break;
            }

            case "ratio": {
                Collections.sort(users, (o1, o2) -> {
                    if (o1.getWordCount() / (double) o1.getMessages().size() == o2.getWordCount() / (double) o2.getMessages().size())
                        return 0;

                    return o1.getWordCount() / (double) o1.getMessages().size() > o2.getWordCount() / (double) o2.getMessages().size() ? -1 : 1;
                });
                int i = 0;
                for (TelegramUser user : users) {
                    if (i++ == 5) break;
                    sb.append(String.format("%.2f - %s", user.getWordCount() / (double) user.getMessages().size(), user.getName()));

                    if (user.getUsername() != null) sb.append(String.format("(%s)", user.getUsername()));
                    sb.append("\n");
                }
            }

            break;
        }
        chat.sendMessage(sb.toString(), getTelegramBot());
    }
}
