package com.endercrest.voidspawn;

public enum TeleportResult {
    SUCCESS("", false),
    INVALID_WORLD("Invalid world", true),
    MISSING_ISLAND_DEPEND("Missing skyblock plugin dependency", true),
    MISSING_ISLAND("No island found", false),
    INCOMPLETE_MODE("Mode has not been setup completely", true),
    FAILED_COMMAND("Command failed", true),
    MODE_DISABLED("The current mode is disabled!", true);

    private final String message;
    private final boolean error;

    TeleportResult(String message, boolean error) {
        this.message = message;
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isError() {
        return error;
    }
}
