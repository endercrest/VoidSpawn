package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.flags.EmptyFlag;
import com.endercrest.voidspawn.modes.flags.Flag;
import com.endercrest.voidspawn.modes.flags.FlagIdentifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class None implements Mode {

    @Override
    public TeleportResult onActivate(Player player, String worldName) {
        return TeleportResult.SUCCESS;
    }

    @Override
    public boolean onSet(String[] args, String worldName, Player p) {
        ConfigManager.getInstance().setMode(worldName, args[1]);
        return true;
    }

    @Override
    public Status[] getStatus(String worldName) {
        return new Status[0];
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getHelp() {
        return "&6None &f- Sets the world to have no mode";
    }

    @Override
    public String getName() {
        return "None";
    }

    @Override
    public @NotNull <T> Flag<T> getFlag(FlagIdentifier<T> identifier) {
        return new EmptyFlag<>(identifier);
    }

    @Override
    public @Nullable Flag<?> getFlag(String name) {
        return null;
    }

    @Override
    public Collection<Flag<?>> getFlags() {
        return Collections.emptyList();
    }
}