package com.endercrest.voidspawn.utils;

/**
 * Created by Thomas Cordua-von Specht on 12/10/2016.
 */
public class NumberUtil {

    public static boolean isInteger(String s){
        try {
            int d = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
