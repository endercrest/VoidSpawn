package com.endercrest.voidspawn;

public enum TeleportResult {
    SUCCESS(""),
    INVALID_WORLD("Invalid world"),
    MISSING_ISLAND_DEPEND("Missing skyblock plugin dependency"),
    MISSING_ISLAND("No island found"),
    INCOMPLETE_MODE("Mode has not been setup completely"),
    FAILED_COMMAND("Command failed");

    private String message;

    TeleportResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
