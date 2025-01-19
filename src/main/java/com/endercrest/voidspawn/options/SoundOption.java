package com.endercrest.voidspawn.options;

import com.endercrest.voidspawn.ConfigManager;
import org.bukkit.Sound;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SoundOption extends BaseOption<Sound> {
    private static final List<String> sounds = new ArrayList<>();

    static {
        try {
            // Use reflection to get all available sounds
            for (Sound sound : Sound.class.getEnumConstants()) {
                sounds.add(sound.name());
            }
        } catch (Exception e) {
            System.err.println("Error initializing sounds: " + e.getMessage());
        }
    }

    public SoundOption(OptionIdentifier<Sound> identifier) {
        super(identifier);
    }

    @Override
    public Optional<Sound> getLoadedValue(@NotNull World world) {
        String value = ConfigManager.getInstance().getOption(world.getName(), getIdentifier());
        if (value == null) return Optional.empty();

        try {
            return Optional.of(Sound.valueOf(value.toUpperCase()));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    @Override
    public void setValue(@NotNull World world, String value) {
        if (!sounds.contains(value.toUpperCase())) {
            throw new IllegalArgumentException(value + " is not a valid sound!");
        }
        super.setValue(world, value);
    }

    @Override
    public void setValue(@NotNull World world, String[] args) throws IllegalArgumentException {
        setValue(world, String.join(" ", args));
    }

    @Override
    public List<String> getOptions() {
        return Collections.unmodifiableList(sounds);
    }
}
