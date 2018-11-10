package com.endercrest.voidspawn.utils;

public class CommandUtil {

    public static String constructWorldFromArgs(String[] args, int start, String defaultWorld) {
        String world = defaultWorld;
        if(args.length > start) {
            StringBuilder worldName = new StringBuilder();
            for(int i = start; i < args.length; i++){
                worldName.append(args[i]).append(" ");
            }
            if(!WorldUtil.isValidWorld(worldName.toString().trim())){
                return null;
            }
            world = worldName.toString().trim();
        }
        return world;
    }
}
