package com.endercrest.voidspawn.utils;

/**
 * Created by Thomas Cordua-von Specht on 12/10/2016.
 */
public class WorldName {

    public static String configSafe(String worldName){
        return worldName.replace(" ", "_");
    }
}
