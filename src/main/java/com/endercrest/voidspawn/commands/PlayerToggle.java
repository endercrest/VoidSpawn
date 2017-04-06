package com.endercrest.voidspawn.commands;

import org.bukkit.entity.Player;

import com.endercrest.voidspawn.VoidSpawn;

public class PlayerToggle implements SubCommand

{
	VoidSpawn plugin;
	public PlayerToggle(VoidSpawn instance){
		this.plugin = instance;
	}
	public boolean onCommand(Player p, String[] args)
	{
		if(p.hasPermission("vs.player.toggle")){
			if(this.plugin.Toggle.containsKey(p.getUniqueId())){
				if(this.plugin.Toggle.get(p.getUniqueId()) == false){
					this.plugin.Toggle.put(p.getUniqueId(), true);
					p.sendMessage("Toggled teleport to: On");
				}
				else{
					p.sendMessage("Toggled teleport to: Off");
					this.plugin.Toggle.put(p.getUniqueId(), false);
				}
			}
			else{
				p.sendMessage("Toggled teleport to: Off");
				this.plugin.Toggle.put(p.getUniqueId(), false);
			}
			return true;
		}
		else{
			return false;
		}
	}

	public String helpInfo()
	{
		return "/vs toggle - Turns off void teleport.";
	}

	public String permission()
	{
		return "vs.player.toggle";
	}
}
