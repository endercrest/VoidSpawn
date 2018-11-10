package com.endercrest.voidspawn.utils;

import com.endercrest.voidspawn.VoidSpawn;

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

    /**
     * Add prefix and colorizes a message.
     * @param message The message to format.
     * @return Returns a string that is ready to be sent to a player.
     */
    public static String format(String message) {
        return colorize(String.format("%s %s", VoidSpawn.prefix, message));
    }
}
