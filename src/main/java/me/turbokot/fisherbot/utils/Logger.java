package me.turbokot.fisherbot.utils;

public class Logger {
    private static final String PREFIX = "McFisher >> ";

    public static void log(String message) {
        System.out.println(PREFIX + message);
    }

    public static void error(String message) {
        System.err.println(PREFIX + "ERROR: " + message);
    }
}