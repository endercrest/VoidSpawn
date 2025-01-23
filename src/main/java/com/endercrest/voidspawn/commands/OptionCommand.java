package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.DetectorManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.detectors.Detector;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionCommand implements SubCommand {
    private static final List<String> actionOptions = new ArrayList<>() {{
       add("clear");
       add("set");
    }};

    @Override
    public boolean onCommand(Player p, String[] args) {
        if (args.length == 1) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "Specify either '&6set&f' or '&6clear&f'."));
            return true;
        } else if (args.length == 2) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cMissing option name!"));
            return true;
        }

        World world = p.getWorld();
        Mode mode = ModeManager.getInstance().getWorldMode(world.getName());
        Detector detector = DetectorManager.getInstance().getWorldDetector(world.getName());
        if (mode == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cNo mode set for the current world!"));
            return true;
        } else if (detector == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cNo detector set for the current world!"));
            return true;
        }

        String optionName = args[2].toLowerCase();
        Option<?> option = mode.getOption(optionName);
        if (option == null) {
            option = detector.getOption(optionName);
        }

        if (option == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix +"&cThere is no option with that name!"));
            return true;
        }

        switch (args[1].toLowerCase()) {
            case "clear" -> {
                option.setValue(world, (String) null);
                p.sendMessage(MessageUtil.colorize(String.format("%sOption '&6%s&f' cleared.", VoidSpawn.prefix, optionName)));
                return true;
            }
            case "set" -> {
                if (args.length == 3) {
                    p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + option.getDescription()));
                    return true;
                }
                try {
                    option.setValue(world, Arrays.copyOfRange(args, 3, args.length));
                    p.sendMessage(MessageUtil.colorize("%sOption '&6%s&f' updated.", VoidSpawn.prefix, optionName));
                } catch (IllegalArgumentException e) {
                    p.sendMessage(MessageUtil.colorize("%s&c%s.", VoidSpawn.prefix, e.getMessage()));
                }
                return true;
            }
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs option [set/clear] [name] [value...] - Set the value of an option";
    }

    @Override
    public String permission() {
        return "vs.admin.option";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        World world = player.getWorld();
        Mode mode = ModeManager.getInstance().getWorldMode(world.getName());
        Detector detector = DetectorManager.getInstance().getWorldDetector(world.getName());
        if (mode == null)
            return Collections.singletonList("<No mode set for the current world>");
        if (detector == null)
            return Collections.singletonList("<No detector set for the current world>");

        if (args.length == 1) {
            return actionOptions;
        } else if (args.length == 2) {
            Stream<Option<?>> modeStream = mode.getOptions().stream();
            Stream<Option<?>> detectorStream = detector.getOptions().stream();
            Stream<Option<?>> optionStream = Stream.concat(modeStream, detectorStream);

            return optionStream.map(f -> f.getIdentifier().getName()).collect(Collectors.toList());
        }

        String optionName = args[1].toLowerCase();
        Option<?> option = mode.getOption(optionName);
        if (option == null)
            option = detector.getOption(optionName);

        if (option == null)
            return Collections.singletonList("<invalid option>");

        if (option.getOptions() != null)
            return option.getOptions().stream()
                    .filter(val -> filterOption(val, String.join(" ", Arrays.copyOfRange(args, 2, args.length))))
                    .collect(Collectors.toList());
        return Collections.singletonList("<value>");
    }

    private boolean filterOption(String option, String search) {
        if (option.startsWith("<") && option.endsWith(">")) {
            return true;
        }

        return option.contains(search);
    }
}
