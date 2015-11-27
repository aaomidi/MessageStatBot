package com.aaomidi.engine;

import com.aaomidi.model.TelegramUser;
import com.aaomidi.util.LogHandler;
import com.google.gson.Gson;
import org.apache.commons.collections4.list.TreeList;
import pro.zackpollard.telegrambot.api.user.User;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by amir on 2015-11-27.
 */
public class DataManager {
    // <ChatID, UserID>
    private TreeMap<String, TreeList<TelegramUser>> chatUserMap = new TreeMap<>();

    public DataManager() {
        String currentPath = System.getProperty("user.dir");
        currentPath = String.format("%s\\Data", currentPath);

        createFile(currentPath, true);

        this.loadData();
    }


    private void loadData() {
        Gson gson = new Gson();

        String currentPath = System.getProperty("user.dir");
        currentPath = String.format("%s%sData", currentPath, File.separator);

        File file = new File(currentPath);
        if (!file.isDirectory() || file.listFiles() == null) return;

        for (File f : file.listFiles()) {
            if (!f.isDirectory() || file.listFiles() == null) continue; // Wrong data.

            TreeList<TelegramUser> users = new TreeList<>();
            chatUserMap.put(f.getName(), users);

            for (File userFile : f.listFiles()) {
                if (userFile.isDirectory()) continue; // Wrong data.
                LogHandler.logn("\tChatID: %s UserFile: %s", f.getName(), userFile.getName());

                if (!userFile.getName().contains("json")) continue; // Bad data.

                try {
                    Reader reader = new FileReader(userFile);
                    TelegramUser telegramUser = gson.fromJson(reader, TelegramUser.class);
                    if (telegramUser == null) {
                        userFile.delete(); // Get rid of shitty file
                        continue;
                    }
                    users.add(telegramUser);
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
        TreeList<TelegramUser> users = chatUserMap.get(chatID);

        if (users == null) return false;

        for (TelegramUser t : users) {
            if (t.getId() == userID) return true;
        }

        return false;
    }

    public TelegramUser getUser(String chatID, Integer userID) {
        TreeList<TelegramUser> users = chatUserMap.get(chatID);

        if (users == null) return null;

        for (TelegramUser t : users) {
            if (t.getId() == userID) return t;
        }

        return null;
    }

    public void initializeChat(String chatID) {
        if (doesChatExist(chatID)) return;

        String currentPath = System.getProperty("user.dir");

        LogHandler.logn(currentPath);

        currentPath = String.format("%s\\Data\\%s", currentPath, chatID);

        LogHandler.logn(currentPath);

        createFile(currentPath, true);

        chatUserMap.put(chatID, new TreeList<TelegramUser>());
    }

    public void initializeUser(String chatID, int userID, User user) {
        if (doesUserExist(chatID, userID)) return;


        String currentPath = System.getProperty("user.dir");

        LogHandler.logn(currentPath);

        currentPath = String.format("%s\\Data\\%s\\%s.json", currentPath, chatID, userID);

        LogHandler.logn(currentPath);

        createFile(currentPath, false);

        TreeList<TelegramUser> list = chatUserMap.get(chatID);
        list.add(new TelegramUser(user));
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
            for (Map.Entry<String, TreeList<TelegramUser>> entry : chatUserMap.entrySet()) {
                String chatID = entry.getKey();
                TreeList<TelegramUser> users = entry.getValue();

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
                    telegramUser.setChangesMade(false);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
