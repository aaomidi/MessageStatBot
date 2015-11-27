package com.aaomidi;

import com.aaomidi.engine.DataManager;
import com.aaomidi.handler.CommandHandler;
import com.aaomidi.hooks.TelegramHook;
import com.aaomidi.util.LogHandler;
import lombok.Getter;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by amir on 2015-11-27.
 */
public class MessageStatBot {
    @Getter
    private TelegramHook telegramHook;
    @Getter
    private CommandHandler commandHandler;
    @Getter
    private DataManager dataManager;

    private TimerTask timerTask;

    public MessageStatBot(String... args) {
        this.setupTelegram(args[0]);

        this.setupCommands();

        this.setupDataManager();

        this.addSaveTimer();

        this.addShutdownHook();
    }


    public static void main(String... args) {
        new MessageStatBot(args);
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
}
