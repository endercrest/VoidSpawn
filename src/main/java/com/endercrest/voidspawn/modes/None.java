 package com.endercrest.voidspawn.modes;
 
 import org.bukkit.entity.Player;
 
 public class None implements SubMode
 {
   public boolean onActivate(Player player, String worldName)
   {
     return true;
   }
   
   public boolean onSet(String[] args, String worldName, Player p)
   {
     com.endercrest.voidspawn.ConfigManager.getInstance().setMode(worldName, args[1]);
     return true;
   }
   
   public String getHelp()
   {
     return "&6None &f- Sets the world to have no mode";
   }
 }