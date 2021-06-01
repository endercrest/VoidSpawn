package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.VoidSpawn;
import com.endercrest.voidspawn.modes.options.Option;
import com.endercrest.voidspawn.modes.status.Status;
import com.endercrest.voidspawn.utils.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Optional;

public class CommandMode extends BaseMode {
    private final VoidSpawn plugin;

    public CommandMode(VoidSpawn plugin) {
        detachOption(BaseMode.OPTION_HYBRID); // Command mode can't be hybrid.
        this.plugin = plugin;
    }

    @Override
    public TeleportResult onActivate(Player player, String worldName) {
        Location touch = TeleportManager.getInstance().getPlayerLocation(player.getUniqueId());
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return TeleportResult.INVALID_WORLD;
        }

        // If the player hasn't touch the ground, fallback to the spawn.
        if (touch == null) {
            touch = ConfigManager.getInstance().getSpawn(worldName);
        }
        // If fallback spawn not set, fall back to the spawn point.
        if (touch == null) {
            touch = world.getSpawnLocation();
        }

        Option<String> commandOption = getOption(BaseMode.OPTION_COMMAND);

        String commandString = commandOption.getValue(world).orElse("")
                .replace("${player.name}", player.getName())
                .replace("${player.uuid}", player.getUniqueId().toString())
                .replace("${player.coord.x}", player.getLocation().getBlockX() + "")
                .replace("${player.coord.y}", player.getLocation().getBlockY() + "")
                .replace("${player.coord.z}", player.getLocation().getBlockZ() + "")
                .replace("${player.coord.world}", player.getLocation().getWorld().getName())
                .replace("${player.touch.x}", touch.getBlockX() + "")
                .replace("${player.touch.y}", touch.getBlockY() + "")
                .replace("${player.touch.z}", touch.getBlockZ() + "")
                .replace("${player.touch.world}", touch.getWorld().getName());

        String[] commands = commandString.split(";");
        TeleportResult result = TeleportResult.SUCCESS;
        for (String command: commands) {
            boolean status;
            String[] perms = command.split(":", 2);
            //Check if cmd needs to be ran as OP/Console
            if (perms.length > 1 && perms[0].trim().equalsIgnoreCase("op")) {
                String cmd = perms[1].trim();
                status = Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            } else {
                status = player.performCommand(command.trim());
            }

            if (!status) {
                plugin.log(String.format("&cCommand Failed for %s! (%s)", worldName, command));
                result = TeleportResult.FAILED_COMMAND;
            }
        }
        if (result != TeleportResult.SUCCESS) {
            player.sendMessage(MessageUtil.colorize(VoidSpawn.prefix + "&cContact Admin. One of the commands failed."));
        }
        return result;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public Status[] getStatus(String worldName) {
        World world = Bukkit.getWorld(worldName);
        Option<String> commandOption = getOption(BaseMode.OPTION_COMMAND);

        Optional<String> command = commandOption.getValue(world);
        return new Status[]{
                new Status(!command.isPresent() ? Status.Type.INCOMPLETE : Status.Type.COMPLETE,
                        String.format("Command Set %s", !command.isPresent() ? "" : String.format("(%s)", command.get())))
        };
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getHelp() {
        return "&6Command &f- Uses configurable command(s) to send player to spawn";
    }

    @Override
    public String getName() {
        return "Command";
    }
}