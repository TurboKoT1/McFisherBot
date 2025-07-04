package me.turbokot.fisherbot.utils;

public class ThreadUtils {
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Throwable ignored) {}
    }
}
