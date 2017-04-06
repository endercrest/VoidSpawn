package com.endercrest.voidspawn;

import com.endercrest.voidspawn.modes.SubMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements org.bukkit.event.Listener
{
	VoidSpawn plugin;
	public MoveListener(VoidSpawn instance){
		this.plugin = instance;
	}
	@org.bukkit.event.EventHandler
	public void onMoveEvent(PlayerMoveEvent event)
	{
		Player player = event.getPlayer();
		String worldName = player.getWorld().getName();

		if (player.getLocation().getBlock().getRelative(org.bukkit.block.BlockFace.DOWN).getType().isSolid()) {
			TeleportManager.getInstance().setPlayerLocation(player.getUniqueId(), player.getLocation());
		}
		int offset = ConfigManager.getInstance().getOffSet(player.getWorld().getName());
		if ((player.getLocation().getY() < -offset) && 
				(ConfigManager.getInstance().isModeSet(worldName))) {
			for (String mode : ModeManager.getInstance().getModes().keySet()) {
				if (ConfigManager.getInstance().getMode(worldName).equalsIgnoreCase(mode)) {
					if(this.plugin.Toggle.containsKey(player.getUniqueId())){
						if(this.plugin.Toggle.get(player.getUniqueId())){
							String string = ConfigManager.getInstance().getMessage(worldName);
							if (!string.equals(""))
								player.sendMessage(VoidSpawn.colorize(string));
							if (!ConfigManager.getInstance().getKeepInventory(worldName))
								player.getInventory().clear();
							SubMode subMode = ModeManager.getInstance().getSubMode(mode);
							boolean success = subMode.onActivate(player, worldName);
							if ((!(subMode instanceof com.endercrest.voidspawn.modes.Command)) && (ConfigManager.getInstance().isHybrid(worldName)))
								ModeManager.getInstance().getSubMode("command").onActivate(player, worldName);
							if (!success) {
								player.sendMessage(VoidSpawn.colorize("&cAn error occurred! Please notify an administrator."));
							}
						}
					}
					else{
						String string = ConfigManager.getInstance().getMessage(worldName);
						if (!string.equals(""))
							player.sendMessage(VoidSpawn.colorize(string));
						if (!ConfigManager.getInstance().getKeepInventory(worldName))
							player.getInventory().clear();
						SubMode subMode = ModeManager.getInstance().getSubMode(mode);
						boolean success = subMode.onActivate(player, worldName);
						if ((!(subMode instanceof com.endercrest.voidspawn.modes.Command)) && (ConfigManager.getInstance().isHybrid(worldName)))
							ModeManager.getInstance().getSubMode("command").onActivate(player, worldName);
						if (!success) {
							player.sendMessage(VoidSpawn.colorize("&cAn error occurred! Please notify an administrator."));
						}
					}
				}
			}
		}
	}
}