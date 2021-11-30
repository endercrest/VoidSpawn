package com.endercrest.voidspawn.detectors;

import com.endercrest.voidspawn.options.IntegerOption;
import com.endercrest.voidspawn.options.OptionIdentifier;
import com.endercrest.voidspawn.options.container.BasicOptionContainer;

public abstract class BaseDetector extends BasicOptionContainer implements Detector {
    public static final OptionIdentifier<Integer> OPTION_VOID = new OptionIdentifier<>(Integer.class, "void_height", "The height to detect at (default just below the void)");

    public BaseDetector() {
        attachOption(new IntegerOption(OPTION_VOID));
    }
}
