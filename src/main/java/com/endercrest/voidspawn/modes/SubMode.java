package com.endercrest.voidspawn.modes;

import org.bukkit.entity.Player;

public interface SubMode {

    public boolean onActivate(Player player, String worldName);

    public boolean onSet(String[] args, String worldName, Player p);

    public String getHelp();
}
