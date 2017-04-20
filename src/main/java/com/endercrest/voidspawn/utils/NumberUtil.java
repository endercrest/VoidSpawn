package com.endercrest.voidspawn.utils;

public class NumberUtil {

    public static boolean isInteger(String s){
        try{
            int d = Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
}