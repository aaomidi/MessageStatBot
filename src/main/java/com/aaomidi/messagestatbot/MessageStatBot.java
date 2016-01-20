package com.aaomidi.messagestatbot;

import com.aaomidi.messagestatbot.engine.DataManager;
import com.aaomidi.messagestatbot.engine.UpdateHandler;
import com.aaomidi.messagestatbot.handler.CommandHandler;
import com.aaomidi.messagestatbot.hooks.TelegramHook;
import com.aaomidi.messagestatbot.util.LogHandler;
import lombok.Getter;
import pro.zackpollard.telegrambot.api.TelegramBot;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amir on 2015-11-27.
 */
public class MessageStatBot {
    @Getter
    private static MessageStatBot instance;
    @Getter
    private static Integer build;
    @Getter
    private TelegramHook telegramHook;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private DataManager dataManager;
    private TimerTask timerTask;
    private Thread updaterThread;

    public MessageStatBot(String... args) {

        instance = this;

        this.setupTelegram(args[0]);

        this.setupCommands();

        this.setupDataManager();

        this.addSaveTimer();

        this.addShutdownHook();
    }


    public static void main(String... args) {
        new MessageStatBot(args);
    }

    private void startAutoUpdater() {
        LogHandler.logn("Starting auto updater...");
        updaterThread = new Thread(new UpdateHandler(this, "MessageStatBot"));
        updaterThread.start();
        LogHandler.logn("\tStarted");
    }

    private void setupTelegram(String key) {
        LogHandler.logn("Connecting to telegram...");
        telegramHook = new TelegramHook(this, key);
        LogHandler.logn("\tConnected");
    }

    private void setupCommands() {
        LogHandler.logn("Registering commands...");
        commandHandler = new CommandHandler(this);
        commandHandler.registerCommands();
        LogHandler.logn("\tRegistered");
    }

    private void setupDataManager() {
        LogHandler.logn("Setting up DataManger...");
        dataManager = new DataManager();
        LogHandler.logn("\tDone");
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                timerTask.run();
            }
        });
    }

    private void addSaveTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                new Thread(() -> {
                    getDataManager().save();
                }).start();
            }
        };

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 5000, 5000);
    }

    public void sendToAdmins(String message) {
        for (int userID : dataManager.getGlobalAdmins()) {
            TelegramBot.getChat(userID + "").sendMessage(message, telegramHook.getBot());
        }
    }

    public void stopUpdater() {
        updaterThread.interrupt();
    }
}
