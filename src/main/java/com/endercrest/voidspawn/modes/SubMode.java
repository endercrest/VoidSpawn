package com.endercrest.voidspawn.modes;

import org.bukkit.entity.Player;

public abstract interface SubMode
{
  public abstract boolean onActivate(Player paramPlayer, String paramString);
  
  public abstract boolean onSet(String[] paramArrayOfString, String paramString, Player paramPlayer);
  
  public abstract String getHelp();
}