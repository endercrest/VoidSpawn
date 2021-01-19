package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.CommandHandler;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VoidSpawnTabCompleter implements TabCompleter {

    private CommandHandler handler;

    public VoidSpawnTabCompleter(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if(!cmd.getName().equalsIgnoreCase("voidspawn") || !(sender instanceof Player)) {
            return null;
        }
        List<String> output = new ArrayList<>();
        Player player = (Player) sender;
        // Send all sub-commands that user has perms for
        if(args.length <= 1) {
            for(Map.Entry<String, SubCommand> subCommand: handler.getCommands()) {
                String permission = subCommand.getValue().permission();
                if(permission == null || !player.hasPermission(permission))
                    continue;

                if(args.length == 0 || subCommand.getKey().startsWith(args[0])) {
                    output.add(subCommand.getKey());
                }
            }

            return output;
        }

        SubCommand subCommand = handler.getSubCommand(args[0]);
        return subCommand != null ? subCommand.getTabCompletion(player, Arrays.copyOfRange(args, 1, args.length)) : null;
    }

    /////////////////////////////////////////////
    ////        Common Auto Completes        ////
    /////////////////////////////////////////////


    static List<String> getBooleanWorldCompletion(String[] args) {
        switch(args.length) {
            case 1:
                return new ArrayList<String>() {{
                    add("true");
                    add("false");
                }}
                .stream()
                .filter(s -> s.startsWith(args[0].toLowerCase()))
                .collect(Collectors.toList());
            case 2:
                return WorldUtil.getMatchingWorlds(args[1]);
            default:
                return new ArrayList<>();
        }
    }
}
