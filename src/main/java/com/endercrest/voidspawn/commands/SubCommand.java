package com.endercrest.voidspawn.commands;

import org.bukkit.entity.Player;

public abstract interface SubCommand {
	public abstract boolean onCommand(Player paramPlayer, String[] paramArrayOfString);

	public abstract String helpInfo();

	public abstract String permission();
}