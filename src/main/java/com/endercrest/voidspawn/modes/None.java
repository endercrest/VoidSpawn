package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportResult;
import org.bukkit.entity.Player;

public class None implements IMode {

    @Override
    public TeleportResult onActivate(Player player, String worldName) {
        return TeleportResult.SUCCESS;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public Status[] getStatus(String worldName) {
        return new Status[0];
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getHelp() {
        return "&6None &f- Sets the world to have no mode";
    }

    @Override
    public String getName() {
        return "None";
    }
}