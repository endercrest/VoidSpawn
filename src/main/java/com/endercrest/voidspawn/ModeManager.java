package com.endercrest.voidspawn;

import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.*;
import com.endercrest.voidspawn.modes.island.*;

import javax.naming.NameAlreadyBoundException;
import java.util.HashMap;

public class ModeManager {
    private static final ModeManager instance = new ModeManager();
    private final HashMap<String, Mode> modes = new HashMap<>();

    /**
     * Get the running instance of the ModeManager.
     *
     * @return The ModeManager
     */
    public static ModeManager getInstance() {
        return instance;
    }

    /**
     * Setup the ModeManager instance. Should only be called on startup.
     */
    public void setUp(VoidSpawn plugin) {
        try {
            addMode("spawn", new SpawnMode());
            addMode("touch", new TouchMode());
            addMode("none", new None());
            addMode("command", new CommandMode(plugin));
            addMode("looper", new LooperMode());

            // Load the correct island mode.
            if (ASkyblockIslandMode.isModeEnabled()) {
                plugin.log("&eASkyBlock found, initializing support.");
                plugin.log("&cASkyBlock has been deprecated, ASkyBlock has been discontinued and it is recommended to switch to BentoBox");

                addMode("island", new ASkyblockIslandMode());
                plugin.log("&eASkyBlock support initialized.");
            } else if (BentoBoxIslandMode.isModeEnabled()) {
                plugin.log("&eBentoBox found, initializing support.");
                addMode("island", new BentoBoxIslandMode());
                plugin.log("&eBentoBox support initialized.");
            } else if (IslandWorldIslandMode.isModeEnabled()) {
                plugin.log("&eIslandWorld found, initializing support.");
                addMode("island", new IslandWorldIslandMode());
                plugin.log("&eIslandWorld support initialized.");
            } else if (USkyBlockIslandMode.isModeEnabled()) {
                plugin.log("&eUSkyBlock found, initializing support.");
                addMode("island", new USkyBlockIslandMode());
                plugin.log("&eUSkyBlock support initialized.");
            } else {
                plugin.log("&eNo SkyBlock plugins found, disabling island mode support.");
                addMode("island", new DisabledIslandMode());
            }
        } catch (NameAlreadyBoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add a new mode that is accessible via command and can be set for worlds.
     *
     * @param modeName The name of the mode which is used throughout settings and selection via commands.
     * @param mode     Class that implements SubMode with the functionality of the mode.
     * @throws NameAlreadyBoundException Thrown when a mode with the name specified already exists.
     */
    public void addMode(String modeName, Mode mode) throws NameAlreadyBoundException {
        modeName = modeName.toLowerCase();

        if (modes.containsKey(modeName))
            throw new NameAlreadyBoundException(String.format("A mode with the name %s has already been set.", modeName));
        modes.put(modeName, mode);
    }

    /**
     * Removes the mode from being selectable, if it exists..
     *
     * @param modeName The mode name.
     */
    public void removeMode(String modeName) {
        modes.remove(modeName.toLowerCase());
    }

    /**
     * Get a mode's class from it's mode name.
     *
     * @param modeName The mode name.
     * @return Returns the SubMode containing the logic behind the mode.
     */
    public Mode getMode(String modeName) {
        return modes.get(modeName.toLowerCase());
    }

    /**
     * Get the mode's class for the specified world. Returns null if not set or if mode can't be found.
     *
     * @param world The world name.
     * @return Class of mode or null if can't find it.
     */
    public Mode getWorldMode(String world) {
        String mode = ConfigManager.getInstance().getMode(world);
        return getMode(mode.toLowerCase());
    }

    /**
     * Gets the HashMap containing all the modes and it's mode names. This is not a copy of the HashMap.
     *
     * @return HashMap clone containing to the mode names and SubMode class.
     */
    public HashMap<String, Mode> getModes() {
        return new HashMap<>(modes);
    }
}