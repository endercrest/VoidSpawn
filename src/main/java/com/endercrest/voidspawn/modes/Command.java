package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.VoidSpawn;
import org.bukkit.entity.Player;

public class Command implements SubMode {
    @Override
    public boolean onActivate(Player player, String worldName) {
        player.setFallDistance(0);
        Boolean cmdsucc = player.performCommand(ConfigManager.getInstance().getString(worldName + ".command"));
        if (!cmdsucc) {
            player.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "&cContact Admin. Command to teleport to spawn failed."));
        }
        return cmdsucc;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public String getHelp() {
        return "&6Command &f- Uses configurable command to send player to spawn";
    }
}
