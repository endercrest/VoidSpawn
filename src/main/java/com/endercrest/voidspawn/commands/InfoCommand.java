package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.DetectorManager;
import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.detectors.Detector;
import com.endercrest.voidspawn.modes.BaseMode;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.modes.options.Option;
import com.endercrest.voidspawn.modes.status.Status;
import com.endercrest.voidspawn.utils.CommandUtil;
import com.endercrest.voidspawn.utils.MessageUtil;
import com.endercrest.voidspawn.utils.WorldUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class InfoCommand implements SubCommand {
    @Override
    public boolean onCommand(Player p, String[] args) {
        String worldName = CommandUtil.constructWorldFromArgs(args, 1, p.getWorld().getName());
        if (worldName == null) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cThat is not a valid world!"));
            return false;
        }
        World world = Bukkit.getWorld(worldName);

        Mode mode = ModeManager.getInstance().getWorldMode(worldName);

        List<String> messages = new ArrayList<>();
        messages.add(String.format("--- &6%s Info &f---", worldName));
        messages.add("Status:");
        messages.add(format(toType(mode != null), String.format("Mode Set (%s)", mode != null ? mode.getName() : "/vs mode")
        ));

        if (mode != null) {
            for (Status status: mode.getStatus(worldName)) {
                messages.add(format(status.getType(), status.getMessage()));
            }

            Detector detector = DetectorManager.getInstance().getWorldDetector(worldName);
            messages.add(format(Status.Type.INFO, String.format("Void Detector: %s", detector.getName())));

            messages.add("Options:");
            for (Option<?> option: mode.getOptions()) {
                Status status = option.getStatus(world);
                messages.add(format(status.getType(), status.getMessage()));
            }

        }

        for (String message: messages) {
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + message));
        }
        return false;
    }

    private Status.Type toType(boolean status) {
        return status ? Status.Type.COMPLETE : Status.Type.INCOMPLETE;
    }

    private String format(Status.Type type, String message) {
        return String.format("  %s %s", type.getSymbol(), message);
    }

    @Override
    public String helpInfo() {
        return "/vs info [name] - Get VoidSpawn info for the given world";
    }

    @Override
    public String permission() {
        return "vs.admin.status";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        switch (args.length) {
            case 1:
                return WorldUtil.getMatchingWorlds(args[0]);
            default:
                return new ArrayList<>();
        }
    }
}
