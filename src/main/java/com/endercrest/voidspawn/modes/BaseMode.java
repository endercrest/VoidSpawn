package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.modes.options.*;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseMode implements Mode {
    public static final OptionIdentifier<Sound> OPTION_SOUND = new OptionIdentifier<>(Sound.class, "sound");
    public static final OptionIdentifier<Float> OPTION_SOUND_VOLUME = new OptionIdentifier<>(Float.class, "sound_volume");
    public static final OptionIdentifier<Float> OPTION_SOUND_PITCH = new OptionIdentifier<>(Float.class, "sound_pitch");
    public static final OptionIdentifier<Boolean> OPTION_HYBRID = new OptionIdentifier<>(Boolean.class, "hybrid");
    public static final OptionIdentifier<Boolean> OPTION_KEEP_INVENTORY = new OptionIdentifier<>(Boolean.class, "keep_inventory");
    public static final OptionIdentifier<String> OPTION_MESSAGE = new OptionIdentifier<>(String.class, "message");
    public static final OptionIdentifier<Integer> OPTION_OFFSET = new OptionIdentifier<>(Integer.class, "offset");

    private final Map<String, Option<?>> options = new HashMap<>();

    public BaseMode() {
        attachOption(new SoundOption(OPTION_SOUND));
        attachOption(new FloatOption(OPTION_SOUND_VOLUME, 1f));
        attachOption(new FloatOption(OPTION_SOUND_PITCH, 1f));
        attachOption(new BooleanOption(OPTION_HYBRID, false));
        attachOption(new BooleanOption(OPTION_KEEP_INVENTORY, true));
        attachOption(new StringOption(OPTION_MESSAGE));
        attachOption(new IntegerOption(OPTION_OFFSET, 0));
    }

    protected void attachOption(Option<?> option) {
        options.put(option.getIdentifier().getName(), option);
    }

    protected void detachOption(OptionIdentifier<?> identifier) {
        options.remove(identifier.getName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull <T> Option<T> getOption(OptionIdentifier<T> identifier) {
        Option<?> option = options.get(identifier.getName());
        if (option == null || option.getType() != identifier.getType()) {
            return new EmptyOption<>(identifier);
        }
        return (Option<T>) option;
    }

    @Override
    public @Nullable Option<?> getOption(String name) {
        return options.get(name);
    }

    @Override
    public Collection<Option<?>> getOptions() {
        return options.values();
    }
}
