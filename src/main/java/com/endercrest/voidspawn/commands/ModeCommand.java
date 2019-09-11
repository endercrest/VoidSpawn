package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.IMode;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ModeCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        if (args.length == 1) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "--- &6Available Modes&f ---"));
            for (String s: ModeManager.getInstance().getModes().keySet()) {
                IMode mode = ModeManager.getInstance().getSubMode(s);
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + mode.getHelp()));
            }
        } else if (args.length >= 2) {
            String world = CommandUtil.constructWorldFromArgs(args, 2, p.getWorld().getName());
            if (world == null) {
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
                return false;
            }

            if (ModeManager.getInstance().getModes().containsKey(args[1].toLowerCase())) {
                IMode mode = ModeManager.getInstance().getSubMode(args[1].toLowerCase());
                if (!mode.isEnabled()) {
                    p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThis is not a valid mode!"));
                    return false;
                } else if (mode.onSet(args, world, p)) {
                    p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Set mode for '&6" + world + "&f'"));
                    return true;
                }
            } else {
                p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThis is not a valid mode!"));
            }
        }
        return false;
    }

    @Override
    public String helpInfo() {
        return "/vs mode (mode) [world] - Sets world mode";
    }

    @Override
    public String permission() {
        return "vs.admin.mode";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        switch (args.length) {
            case 1:
                Set<String> modes = ModeManager.getInstance().getModes().keySet();
                return modes.stream()
                        .filter(mode -> mode.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList());
            case 2:
                return WorldUtil.getMatchingWorlds(args[1]);
            default:
                return new ArrayList<>();
        }
    }
}