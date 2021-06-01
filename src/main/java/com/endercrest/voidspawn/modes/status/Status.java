package com.endercrest.voidspawn.modes.status;

public class Status {
    private final Type type;
    private final String message;

    public Status(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public enum Type {
        COMPLETE("[&a+&f]"),
        INCOMPLETE("[&cx&f]"),
        INFO("[&b@&f]"),
        UNSET("[&7-&f]");

        private String symbol;

        Type(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}
