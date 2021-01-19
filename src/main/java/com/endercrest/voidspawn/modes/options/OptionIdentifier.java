package com.endercrest.voidspawn.modes.options;

public class OptionIdentifier<T> {
    private final Class<T> type;
    private final String name;

    public OptionIdentifier(Class<T> type, String name) {
        this.type = type;
        this.name = name;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
