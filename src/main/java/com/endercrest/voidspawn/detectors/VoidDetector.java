package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.entity.Player;

/**
 * Created by Thomas Cordua-von Specht on 5/27/2017.
 *
 * This is the classic mode for VoidSpawn which detects when the player enters the void.
 */
public class VoidDetector implements SubDetector {

    @Override
    public boolean isDetected(Player player, String worldName){
        int offset = ConfigManager.getInstance().getOffSet(worldName);
        return player.getLocation().getBlockY() < -offset;
    }

    @Override
    public String getInfo(){
        return getName() + " - [Default] Activated by entering the void.";
    }

    @Override
    public String getName(){
        return "Void";
    }
}
