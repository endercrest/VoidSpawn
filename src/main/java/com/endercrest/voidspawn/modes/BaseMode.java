package com.endercrest.voidspawn.modes;

import com.endercrest.voidspawn.options.*;
import com.endercrest.voidspawn.options.container.BasicOptionContainer;
import org.bukkit.Sound;

public abstract class BaseMode extends BasicOptionContainer implements Mode {
    public static final OptionIdentifier<String> OPTION_SOUND = new OptionIdentifier<>(String.class, "sound", "The sound played when detected in void");
    public static final OptionIdentifier<Float> OPTION_SOUND_VOLUME = new OptionIdentifier<>(Float.class, "sound_volume", "The sound volume");
    public static final OptionIdentifier<Float> OPTION_SOUND_PITCH = new OptionIdentifier<>(Float.class, "sound_pitch", "The sound pitch");
    public static final OptionIdentifier<Boolean> OPTION_HYBRID = new OptionIdentifier<>(Boolean.class, "hybrid", "Whether to run in hybrid mode (mode and command)");
    public static final OptionIdentifier<Boolean> OPTION_KEEP_INVENTORY = new OptionIdentifier<>(Boolean.class, "keep_inventory", "Whether players keep inventory");
    public static final OptionIdentifier<String> OPTION_MESSAGE = new OptionIdentifier<>(String.class, "message", "Message sent when detected in void");
    public static final OptionIdentifier<String> OPTION_COMMAND = new OptionIdentifier<>(String.class, "command", "The command(s) for either command mode or hybrid");
    public static final OptionIdentifier<Boolean> OPTION_INC_DEATH_STAT = new OptionIdentifier<>(Boolean.class, "inc_death_stat", "Whether to increment the death statistic");
    public static final OptionIdentifier<Integer> OPTION_DAMAGE = new OptionIdentifier<>(Integer.class, "damage", "Amount of damage applied upon entering the void");
    public static final OptionIdentifier<Integer> OPTION_BOUNCE = new OptionIdentifier<>(Integer.class, "bounce", "Number of times to bounce from the void before activating mode");
    public static final OptionIdentifier<Float> OPTION_MIN_BOUNCE_VELOCITY = new OptionIdentifier<>(Float.class, "min_bounce_velocity", "The minimum bounce velocity");

    public BaseMode() {
        attachOption(new SoundOption(OPTION_SOUND));
        attachOption(new FloatOption(OPTION_SOUND_VOLUME, 1f));
        attachOption(new FloatOption(OPTION_SOUND_PITCH, 1f));
        attachOption(new BooleanOption(OPTION_HYBRID, false));
        attachOption(new BooleanOption(OPTION_KEEP_INVENTORY, true));
        attachOption(new StringOption(OPTION_MESSAGE));
        attachOption(new StringOption(OPTION_COMMAND));
        attachOption(new BooleanOption(OPTION_INC_DEATH_STAT, false));
        attachOption(new IntegerOption(OPTION_DAMAGE, 0));
        attachOption(new IntegerOption(OPTION_BOUNCE, 0));
        attachOption(new FloatOption(OPTION_MIN_BOUNCE_VELOCITY, 2.0f));
    }
}
