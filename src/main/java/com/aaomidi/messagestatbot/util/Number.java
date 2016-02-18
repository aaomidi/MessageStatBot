package com.aaomidi.messagestatbot.util;

/**
 * Created by amir on 2015-11-27.
 */
public class Number {
    public static Integer fromString(String s) {
        try {
            return Integer.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }

    public static Long fromLongString(String s) {
        try {
            return Long.valueOf(s);
        } catch (Exception ex) {
            return null;
        }
    }
}
