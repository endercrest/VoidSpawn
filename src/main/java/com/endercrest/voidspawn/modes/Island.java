package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import org.bukkit.entity.Player;

public class Island
implements SubMode
{
	public boolean onActivate(Player player, String worldName)
	{
		return TeleportManager.getInstance().teleportIsland(player);
	}

	public boolean onSet(String[] args, String worldName, Player p)
	{
		ConfigManager.getInstance().setMode(worldName, args[1]);

		return true;
	}

	public String getHelp()
	{
		return "&6Island &f- Will teleport player back to IslandWorld island";
	}
}