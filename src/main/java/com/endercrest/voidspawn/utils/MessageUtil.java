package com.endercrest.voidspawn.utils;

public class MessageUtil {

    /**
     * Add Color to Messages
     *
     * @param str The String
     * @return Coloured String
     */
    public static String colorize(String str){
        return str.replaceAll("(?i)&([a-f0-9k-or])", "\u00a7$1");
    }
}
