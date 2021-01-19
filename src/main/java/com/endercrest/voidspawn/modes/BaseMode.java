package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.modes.flags.*;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMode implements Mode {
    public static final FlagIdentifier<Sound> FLAG_SOUND = new FlagIdentifier<>(Sound.class, "sound");
    public static final FlagIdentifier<Float> FLAG_SOUND_VOLUME = new FlagIdentifier<>(Float.class, "sound_volume");
    public static final FlagIdentifier<Float> FLAG_SOUND_PITCH = new FlagIdentifier<>(Float.class, "sound_pitch");
    public static final FlagIdentifier<Boolean> FLAG_HYBRID = new FlagIdentifier<>(Boolean.class, "hybrid");
    public static final FlagIdentifier<Boolean> FLAG_KEEP_INVENTORY = new FlagIdentifier<>(Boolean.class, "keep_inventory");
    public static final FlagIdentifier<String> FLAG_MESSAGE = new FlagIdentifier<>(String.class, "message");
    public static final FlagIdentifier<Integer> FLAG_OFFSET = new FlagIdentifier<>(Integer.class, "offset");

    private final Map<String, Flag<?>> flags = new HashMap<>();

    public BaseMode() {
        attachFlag(new SoundFlag(FLAG_SOUND));
        attachFlag(new FloatFlag(FLAG_SOUND_VOLUME, 1f));
        attachFlag(new FloatFlag(FLAG_SOUND_PITCH, 1f));
        attachFlag(new BooleanFlag(FLAG_HYBRID, false));
        attachFlag(new BooleanFlag(FLAG_KEEP_INVENTORY, true));
        attachFlag(new StringFlag(FLAG_MESSAGE));
        attachFlag(new IntegerFlag(FLAG_OFFSET, 0));
    }

    protected void attachFlag(Flag<?> flag) {
        flags.put(flag.getIdentifier().getName(), flag);
    }

    protected void detachFlag(FlagIdentifier<?> identifier) {
        flags.remove(identifier.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Flag<T> getFlag(FlagIdentifier<T> identifier) {
        Flag<?> flag = flags.get(identifier.getName());
        if (flag == null || flag.getType() != identifier.getType()) {
            return new EmptyFlag<>(identifier);
        }
        return (Flag<T>) flag;
    }

    @Override
    public @Nullable Flag<?> getFlag(String name) {
        return flags.get(name);
    }

    @Override
    public Collection<Flag<?>> getFlags() {
        return flags.values();
    }
}
