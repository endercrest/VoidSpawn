package com.endercrest.voidspawn.options.container;

import com.endercrest.voidspawn.options.EmptyOption;
import com.endercrest.voidspawn.options.Option;
import com.endercrest.voidspawn.options.OptionIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicOptionContainer implements OptionContainer {
    private final Map<String, Option<?>> options = new HashMap<>();

    protected void attachOption(Option<?> option) {
        options.put(option.getIdentifier().getName(), option);
    }

    protected void detachOption(@NotNull OptionIdentifier<?> identifier) {
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
