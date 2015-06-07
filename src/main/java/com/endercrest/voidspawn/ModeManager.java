package com.endercrest.voidspawn;

import com.endercrest.voidspawn.modes.*;

import java.util.HashMap;

public class ModeManager {

    private static ModeManager instance = new ModeManager();

    private HashMap<String, SubMode> modes = new HashMap<String, SubMode>();

    public static ModeManager getInstance(){
        return instance;
    }

    public void setUp(){
        addMode("spawn", new Spawn());
        addMode("touch", new Touch());
        addMode("none", new None());
        if(VoidSpawn.IslandWorld) {
            addMode("island", new Island());
        }
    }

    public void addMode(String modeName, SubMode mode){
        modes.put(modeName, mode);
    }

    public void removeMode(String modeName){
        modes.remove(modeName);
    }

    public SubMode getSubMode(String modeName){
        return modes.get(modeName);
    }

    public HashMap<String, SubMode> getModes(){
        return modes;
    }


}
