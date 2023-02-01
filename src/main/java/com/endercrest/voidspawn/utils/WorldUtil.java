package com.endercrest.voidspawn.utils;

import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * get a list of world names that begin with partial. If partial is
     * empty, all worlds are returned.
     * @param partial String to compare against.
     * @return List of world names.
     */
    public static List<String> getMatchingWorlds(String partial) {
        return Bukkit.getWorlds().stream()
                .map(World::getName)
                .filter(world -> world.toLowerCase().startsWith(partial.toLowerCase()))
                .collect(Collectors.toList());
    }
}