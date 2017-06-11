package com.endercrest.voidspawn.utils;

public class NumberUtil {

    public static boolean isInteger(String s){
        try{
            Integer.parseInt(s);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }

    public static boolean isFloat(String s){
        try{
            Float.parseFloat(s);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
}