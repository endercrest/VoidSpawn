 package com.endercrest.voidspawn.commands;
 
 import com.endercrest.voidspawn.ModeManager;
 import com.endercrest.voidspawn.VoidSpawn;
 import com.endercrest.voidspawn.modes.SubMode;
 import org.bukkit.entity.Player;
 
 public class Modes implements SubCommand
 {
   public boolean onCommand(Player p, String[] args)
   {
     if (!p.hasPermission(permission())) {
       p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
       return true;
     }
     p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "--- &6Available Modes&f ---"));
     for (String s : ModeManager.getInstance().getModes().keySet()) {
       SubMode mode = ModeManager.getInstance().getSubMode(s);
       p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + mode.getHelp()));
     }
     return true;
   }
   
   public String helpInfo()
   {
     return "/vs modes - Gets all available modes";
   }
   
   public String permission()
   {
     return "vs.admin.modes";
   }
 }