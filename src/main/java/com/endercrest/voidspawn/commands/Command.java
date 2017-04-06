 package com.endercrest.voidspawn.commands;
 
 import com.endercrest.voidspawn.ConfigManager;
 import com.endercrest.voidspawn.VoidSpawn;
 import org.bukkit.World;
 import org.bukkit.entity.Player;
 
 
 
 public class Command
   implements SubCommand
 {
   public boolean onCommand(Player p, String[] args)
   {
     if (!p.hasPermission(permission())) {
       p.sendMessage(VoidSpawn.colorize("&cYou do not have permission."));
       return true;
     }
     if (args.length > 1) {
       String command = "";
       for (int i = 1; i < args.length; i++) {
         command = command + args[i] + " ";
       }
       ConfigManager.getInstance().setCommand(command, p.getWorld().getName());
     } else {
       ConfigManager.getInstance().setCommand(null, p.getWorld().getName());
       p.sendMessage(VoidSpawn.colorize("Removed Command(s)"));
       return true;
     }
     p.sendMessage(VoidSpawn.colorize(VoidSpawn.prefix + "Command(s) Set"));
     return true;
   }
   
   public String helpInfo()
   {
     return "/vs command [commands] - Set command(s) for the command mode, separate commands with semicolon.";
   }
   
   public String permission()
   {
     return "vs.admin.command";
   }
 }