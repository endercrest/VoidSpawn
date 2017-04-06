 package com.endercrest.voidspawn.modes;
 
 import com.endercrest.voidspawn.TeleportManager;
 import org.bukkit.entity.Player;
 
 public class Touch implements SubMode
 {
   public boolean onActivate(Player player, String worldName)
   {
     return TeleportManager.getInstance().teleportTouch(player);
   }
   
   public boolean onSet(String[] args, String worldName, Player p)
   {
     com.endercrest.voidspawn.ConfigManager.getInstance().setMode(worldName, args[1]);
     return true;
   }
   
   public String getHelp()
   {
     return "&6Touch &f- Will teleport player to place they last touched the ground.";
   }
 }