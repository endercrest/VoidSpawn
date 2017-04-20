package com.endercrest.voidspawn.utils;

public class WorldName {

    public static String configSafe(String worldName){
        return worldName.replace(" ", "_");
    }
}