package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import org.bukkit.entity.Player;

public class Touch implements SubMode {

    @Override
    public boolean onActivate(Player player, String worldName) {
        return TeleportManager.getInstance().teleportTouch(player);
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public String getHelp() {
        return "&6Touch &f- Will teleport player to place they last touched the ground.";
    }
}