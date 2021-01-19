package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.ConfigManager;
import com.endercrest.voidspawn.TeleportResult;
import com.endercrest.voidspawn.modes.options.EmptyOption;
import com.endercrest.voidspawn.modes.options.Option;
import com.endercrest.voidspawn.modes.options.OptionIdentifier;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;

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
    public @NotNull <T> Option<T> getOption(OptionIdentifier<T> identifier) {
        return new EmptyOption<>(identifier);
    }

    @Override
    public @Nullable Option<?> getOption(String name) {
        return null;
    }

    @Override
    public Collection<Option<?>> getOptions() {
        return Collections.emptyList();
    }
}