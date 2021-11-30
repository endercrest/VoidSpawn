package com.endercrest.voidspawn.options;

public class OptionIdentifier<T> {
    private final Class<T> type;
    private final String name;
    private final String description;

    public OptionIdentifier(Class<T> type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
    }

    public Class<T> getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
