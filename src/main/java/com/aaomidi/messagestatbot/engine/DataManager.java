package com.aaomidi.messagestatbot.engine;

import com.aaomidi.messagestatbot.model.TelegramChat;
import com.aaomidi.messagestatbot.model.TelegramUser;
import com.aaomidi.messagestatbot.util.LogHandler;
import com.google.gson.Gson;
import lombok.Getter;
import org.apache.commons.collections4.list.TreeList;
import pro.zackpollard.telegrambot.api.user.User;

import java.io.*;
import java.util.*;

/**
 * Created by amir on 2015-11-27.
 */
public class DataManager {
    // <ChatID, UserID>
    private Map<String, TelegramChat> chatUserMap = new TreeMap<>();
    @Getter
    private List<Integer> globalAdmins = new TreeList<>();

    public DataManager() {
        String currentPath = System.getProperty("user.dir");
        currentPath = String.format("%s%sData", currentPath, File.separator);

        createFile(currentPath, true);

        this.loadData();
        this.loadAdmins();
    }

    public int getChatCount() {
        return chatUserMap.size();
    }

    public int getUserCount() {
        HashSet<Integer> countedIDs = new HashSet<>();
        for (TelegramChat telegramChat : chatUserMap.values()) {
            for (TelegramUser telegramUser : telegramChat.getUsers().values()) {
                countedIDs.add(telegramUser.getId());
            }
        }
        return countedIDs.size();
    }

    public Collection<TelegramChat> getChats() {
        return this.chatUserMap.values();
    }

    private void loadAdmins() {
        globalAdmins.add(55395012);
    }


    private void loadData() {
        Gson gson = new Gson();
        String currentPath = System.getProperty("user.dir");
        currentPath = String.format("%s%sData", currentPath, File.separator);

        LogHandler.logn("File path: " + currentPath);

        File file = new File(currentPath);
        if (!file.isDirectory() || file.listFiles() == null) return;

        for (File f : file.listFiles()) {
            if (!f.isDirectory() || file.listFiles() == null) continue; // Wrong data.
            TelegramChat telegramChat = new TelegramChat(f.getName());

            chatUserMap.put(f.getName(), telegramChat);

            for (File userFile : f.listFiles()) {
                if (userFile.isDirectory()) continue; // Wrong data.
                //LogHandler.logn("\tChatID: %s UserFile: %s", f.getName(), userFile.getName());

                if (!userFile.getName().contains("json")) continue; // Bad data.

                try {
                    Reader reader = new FileReader(userFile);
                    TelegramUser telegramUser = gson.fromJson(reader, TelegramUser.class);
                    if (telegramUser == null) {
                        userFile.delete(); // Get rid of shitty file
                        continue;
                    }
                    telegramChat.addUser(telegramUser);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public boolean doesChatExist(String chatID) {
        return chatUserMap.containsKey(chatID);
    }

    public boolean doesUserExist(String chatID, Integer userID) {
        TelegramChat chat = getChat(chatID);
        Collection<TelegramUser> users = chat.getUsers().values();


        if (users == null) return false;

        for (TelegramUser t : users) {
            if (t.getId() == userID) return true;
        }

        return false;
    }

    public TelegramChat getChat(String chatID) {
        return chatUserMap.get(chatID);
    }

    public TelegramUser getUser(String chatID, Integer userID) {
        TelegramChat telegramChat = getChat(chatID);

        if (telegramChat == null) return null;

        Collection<TelegramUser> users = telegramChat.getUsers().values();

        for (TelegramUser t : users) {
            if (t.getId() == userID) return t;
        }

        return null;
    }

    public Collection<TelegramUser> getUsers(String chatID) {
        TelegramChat chat = getChat(chatID);

        if (chat == null) return null;

        return chat.getUsers().values();
    }

    public void initializeChat(String chatID) {
        if (doesChatExist(chatID)) return;

        String currentPath = System.getProperty("user.dir");

        LogHandler.logn(currentPath);

        currentPath = String.format("%s%sData%s%s", currentPath, File.separator, File.separator, chatID);

        LogHandler.logn(currentPath);

        createFile(currentPath, true);

        chatUserMap.put(chatID, new TelegramChat(chatID));
    }

    public void initializeUser(String chatID, int userID, User user) {
        if (doesUserExist(chatID, userID)) return;


        String currentPath = System.getProperty("user.dir");

        LogHandler.logn(currentPath);

        currentPath = String.format("%s%sData%s%s%s%s.json", currentPath, File.separator, File.separator, chatID, File.separator, userID);
        LogHandler.logn(currentPath);

        createFile(currentPath, false);

        TelegramChat telegramChat = getChat(chatID);
        telegramChat.addUser(new TelegramUser(user));
    }


    private void createFile(String path, boolean isDirectory) {
        File file = new File(path);

        try {
            if (file.isDirectory() || isDirectory) {
                file.mkdir();
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            Gson gson = new Gson();
            for (Map.Entry<String, TelegramChat> entry : chatUserMap.entrySet()) {
                String chatID = entry.getKey();
                TelegramChat telegramChat = entry.getValue();
                Collection<TelegramUser> users = telegramChat.getUsers().values();

                String currentPath = String.format("%s%sData%s%s", System.getProperty("user.dir"), File.separator, File.separator, chatID);
                for (TelegramUser telegramUser : users) {
                    if (!telegramUser.isChangesMade()) continue;
                    String p = String.format("%s%s%s.json", currentPath, File.separator, telegramUser.getId());
                    File file = new File(p);

                    if (!file.exists()) {
                        LogHandler.logn("Something went wrong!");
                        continue;
                    }
                    //file.delete();
                    //file.createNewFile();

                    FileWriter fileWriter = new FileWriter(file);
                    fileWriter.write(gson.toJson(telegramUser));
                    fileWriter.flush();
                    fileWriter.close();
                    telegramUser.setChangesMade(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public boolean isAdmin(User user) {
        return isAdmin(user.getId());
    }

    public boolean isAdmin(int id) {
        LogHandler.logn("ID IS: " + id);
        for (Integer i : globalAdmins) {
            if (i.equals(id)) {
                return true;
            }
        }
        return false;
    }
}
