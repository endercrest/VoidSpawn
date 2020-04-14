package com.endercrest.voidspawn.commands;

import com.endercrest.voidspawn.ModeManager;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.Mode;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ModesCommand implements SubCommand {

    @Override
    public boolean onCommand(Player p, String[] args) {
        p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "--- &6Available Modes&f ---"));
        for (String s: ModeManager.getInstance().getModes().keySet()) {
            Mode mode = ModeManager.getInstance().getSubMode(s);
            if (!mode.isEnabled())
                continue;
            p.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + mode.getHelp()));
        }
        return true;
    }

    @Override
    public String helpInfo() {
        return "/vs modes - Gets all available modes";
    }

    @Override
    public String permission() {
        return "vs.admin.modes";
    }

    @Override
    public List<String> getTabCompletion(Player player, String[] args) {
        return new ArrayList<>();
    }
}