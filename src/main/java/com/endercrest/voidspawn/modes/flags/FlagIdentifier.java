package com.endercrest.voidspawn.modes.flags;

public class FlagIdentifier<T> {
    private final Class<T> type;
    private final String name;

    public FlagIdentifier(Class<T> type, String name) {
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
