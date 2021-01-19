package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.flags.Flag;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;

public class FlagCommand implements SubCommand {
    private static final List<String> actionOptions = new ArrayList<String>() {{
       add("clear");
       add("set");
    }};

    @Override
    public boolean onCommand(Player p, String[] args) {
        World world = p.getWorld();
        Mode mode = ModeManager.getInstance().getWorldMode(world.getName());
        if (args.length == 1) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Specify either '&6set&f' or '&6clear&f'."));
            return true;
        } else if (args.length == 2) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMissing flag name!"));
            return true;
        }

        String flagName = args[2].toLowerCase();
        Flag<?> flag = mode.getFlag(flagName);
        if (flag == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix +"&cThere is no flag with that name!"));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "clear":
                flag.setValue(world, (String) null);
                p.sendMessage(MessageUtil.colorize(String.format("%s Flag '&6%s&f' cleared.", VoidSpawn.prefix, flagName)));
                return true;
            case "set":
                if (args.length == 3) {
                    p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMissing a flag value!"));
                    return true;
                }

                try {
                    flag.setValue(world, Arrays.copyOfRange(args, 3, args.length));
                    p.sendMessage(MessageUtil.colorize("%s Flag '&6%s&f' updated.", VoidSpawn.prefix, flagName));
                } catch (IllegalArgumentException e) {
                    p.sendMessage(MessageUtil.colorize("%s &c%s.", VoidSpawn.prefix, e.getMessage()));
                }
                return true;
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs flag [set/clear] [name] [value...] - Set the value of a flag";
    }

    @Override
    public String permission() {
        return "vs.admin.flag";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        World world = player.getWorld();
        Mode mode = ModeManager.getInstance().getWorldMode(world.getName());
        if (mode == null)
            return Collections.singletonList("<No mode set for the current world>");

        if (args.length == 1)
            return actionOptions;
        else if (args.length == 2)
            return mode.getFlags().stream().map(f -> f.getIdentifier().getName()).collect(Collectors.toList());

        String flagName = args[1].toLowerCase();
        Flag<?> flag = mode.getFlag(flagName);
        if (flag == null)
            return Collections.singletonList("<invalid flag>");
        if (flag.getOptions() != null)
            return flag.getOptions().stream()
                    .filter(option -> option.contains(String.join(" ", Arrays.copyOfRange(args, 2, args.length))))
                    .collect(Collectors.toList());
        return Collections.singletonList("<value>");
    }
}
