package com.endercrest.voidspawn;

import com.endercrest.voidspawn.modes.SubMode;
import com.endercrest.voidspawn.modes.*;

import java.util.HashMap;

public class ModeManager {
    private static ModeManager instance = new ModeManager();

    private HashMap<String, SubMode> modes = new HashMap<String, SubMode>();

    /**
     * Get the running instance of the ModeManager.
     *
     * @return The ModeManager
     */
    public static ModeManager getInstance(){
        return instance;
    }

    /**
     * Setup the ModeManager instance. Should only be called on startup.
     */
    public void setUp(){
        addMode("spawn", new Spawn());
        addMode("touch", new Touch());
        addMode("none", new None());
        addMode("command", new Command());
        if(VoidSpawn.IslandWorld || VoidSpawn.ASkyBlock || VoidSpawn.USkyBlock){
            addMode("island", new Island());
        }
    }

    /**
     * Add a new mode that is accessible via command and can be set for worlds.
     *
     * @param modeName The name of the mode which is used throughout settings and selection via commands.
     * @param mode     Class that implements SubMode with the functionality of the mode.
     */
    public void addMode(String modeName, SubMode mode){
        modes.put(modeName, mode);
    }

    /**
     * Removes the mode from being selectable.
     *
     * @param modeName The mode name.
     */
    public void removeMode(String modeName){
        modes.remove(modeName);
    }

    /**
     * Get a mode's class from it's mode name.
     *
     * @param modeName The mode name.
     * @return Returns the SubMode containing the logic behind the mode.
     */
    public SubMode getSubMode(String modeName){
        return modes.get(modeName);
    }

    /**
     * Get the mode's class for the specified world. Returns null if not set or if mode can't be found.
     *
     * @param world The world name.
     * @return Class of mode or null if can't find it.
     */
    public SubMode getWorldSubMode(String world){
        String mode = ConfigManager.getInstance().getMode(world);
        return getSubMode(mode);
    }

    /**
     * Gets the HashMap containing all the modes and it's mode names. This is not a copy of the HashMap.
     *
     * @return HashMap clone containing to the mode names and SubMode class.
     */
    public HashMap<String, SubMode> getModes(){
        return new HashMap<>(modes);
    }
}