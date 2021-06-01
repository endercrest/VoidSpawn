package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.status.Status;
import org.bukkit.entity.Player;

public class TouchMode extends BaseMode {

    @Override
    public TeleportResult onActivate(Player player, String worldName) {
        return TeleportManager.getInstance().teleportTouch(player);
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
        return "&6Touch &f- Will teleport player to place they last touched the ground.";
    }

    @Override
    public String getName() {
        return "Touch";
    }
}