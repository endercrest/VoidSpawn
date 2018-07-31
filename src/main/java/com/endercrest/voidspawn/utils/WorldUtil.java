package com.endercrest.voidspawn.utils;

import org.bukkit.Bukkit;

public class WorldUtil {

    public static String configSafe(String worldName){
        return worldName.replace(" ", "_");
    }

    /**
     * Checks if the selected world is a valid world.
     *
     * @param worldName The world name that will be checked.
     * @return True if the world does not return null.
     */
    public static boolean isValidWorld(String worldName){
        return Bukkit.getWorld(worldName) != null;
    }
}