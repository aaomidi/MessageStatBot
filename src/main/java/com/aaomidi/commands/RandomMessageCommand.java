package com.aaomidi.commands;

import com.aaomidi.MessageStatBot;
import com.aaomidi.model.TelegramCommand;
import com.aaomidi.model.TelegramMessage;
import com.aaomidi.model.TelegramUser;
import com.aaomidi.util.IntegerConverter;
import com.aaomidi.util.LogHandler;
import pro.zackpollard.telegrambot.api.TelegramBot;
import pro.zackpollard.telegrambot.api.chat.Chat;
import pro.zackpollard.telegrambot.api.chat.ChatType;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.chat.message.content.Content;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableForwardMessage;
import pro.zackpollard.telegrambot.api.chat.message.send.SendableMessage;
import pro.zackpollard.telegrambot.api.event.chat.message.CommandMessageReceivedEvent;
import pro.zackpollard.telegrambot.api.user.User;

import java.util.List;

/**
 * Created by amir on 2015-11-29.
 */
public class RandomMessageCommand extends TelegramCommand {
    public RandomMessageCommand(MessageStatBot instance, String name, String description, String... aliases) {
        super(instance, name, description, aliases);
    }

    @Override
    public void execute(CommandMessageReceivedEvent event) {
        if (event.getArgs().length == 0) return;

        Chat chat = event.getChat();
        String name = event.getArgs()[0];

        for (int i = 1; i < event.getArgs().length - 1; i++) {
            name = String.format("%s %s", name, event.getArgs()[i]);
        }
        String s = event.getArgs()[event.getArgs().length - 1];

        Integer minChar = IntegerConverter.fromString(s);

        if (minChar == null && event.getArgs().length > 1) {
            name = String.format("%s %s", name, event.getArgs()[event.getArgs().length - 1]);
        }

        LogHandler.logn(name);
        List<TelegramUser> telegramUsers = getInstance().getDataManager().getUsers(chat.getId());

        TelegramUser telegramUser = null;

        for (TelegramUser t : telegramUsers) {
            if (t.getUsername().equalsIgnoreCase(name) || t.getName().equalsIgnoreCase(name)) {
                telegramUser = t;
                break;
            }
        }

        if (telegramUser == null) return;

        TelegramMessage randomMessage;

        if (minChar != null) {
            randomMessage = telegramUser.getRandomMessage(minChar);
        } else {
            randomMessage = telegramUser.getRandomMessage();
        }

        if (randomMessage == null) return;

        SendableForwardMessage forwardMessage = SendableForwardMessage.builder()
                .forwardedMessage(new Message() {
                    @Override
                    public int getMessageId() {
                        return randomMessage.getId();
                    }

                    @Override
                    public int getTimeStamp() {
                        return 0;
                    }

                    @Override
                    public User getSender() {
                        return null;
                    }

                    @Override
                    public Chat getChat() {
                        return new Chat() {
                            @Override
                            public String getId() {
                                return chat.getId();
                            }

                            @Override
                            public ChatType getType() {
                                return null;
                            }

                            @Override
                            public Message sendMessage(SendableMessage sendableMessage, TelegramBot telegramBot) {
                                return null;
                            }
                        };
                    }

                    @Override
                    public User getForwardedFrom() {
                        return null;
                    }

                    @Override
                    public int getForwardedDate() {
                        return 0;
                    }

                    @Override
                    public Message getRepliedTo() {
                        return null;
                    }

                    @Override
                    public Content getContent() {
                        return null;
                    }
                }).build();

        chat.sendMessage(forwardMessage, getTelegramBot());
    }
}
