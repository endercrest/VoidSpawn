 package com.endercrest.voidspawn;
 
 import com.endercrest.voidspawn.modes.SubMode;
 import com.endercrest.voidspawn.modes.Touch;
 import java.util.HashMap;
 
 public class ModeManager
 {
   private static ModeManager instance = new ModeManager();
   
   private HashMap<String, SubMode> modes = new HashMap();
   
 
 
 
 
   public static ModeManager getInstance()
   {
     return instance;
   }
   
 
 
   public void setUp()
   {
     addMode("spawn", new com.endercrest.voidspawn.modes.Spawn());
     addMode("touch", new Touch());
     addMode("none", new com.endercrest.voidspawn.modes.None());
     addMode("command", new com.endercrest.voidspawn.modes.Command());
     if ((VoidSpawn.IslandWorld) || (VoidSpawn.ASkyBlock) || (VoidSpawn.USkyBlock)) {
       addMode("island", new com.endercrest.voidspawn.modes.Island());
     }
   }
   
 
 
 
 
 
   public void addMode(String modeName, SubMode mode)
   {
     this.modes.put(modeName, mode);
   }
   
 
 
 
   public void removeMode(String modeName)
   {
     this.modes.remove(modeName);
   }
   
 
 
 
 
   public SubMode getSubMode(String modeName)
   {
     return (SubMode)this.modes.get(modeName);
   }
   
 
 
 
   public HashMap<String, SubMode> getModes()
   {
     return this.modes;
   }
 }