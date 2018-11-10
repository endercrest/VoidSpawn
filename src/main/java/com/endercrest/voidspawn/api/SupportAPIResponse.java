package com.endercrest.voidspawn.api;

public class SupportAPIResponse {

    private boolean isSuccessful;
    private String message;

    public SupportAPIResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
    }

    public boolean isSuccessful(){
        return isSuccessful;
    }

    public String getMessage(){
        return message;
    }
}
