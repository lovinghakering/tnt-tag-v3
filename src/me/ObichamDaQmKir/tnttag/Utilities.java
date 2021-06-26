package me.ObichamDaQmKir.tnttag;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.UUID;

public class Utilities
{
    public static int randomNumber(int min, int max)
    {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static int nextArenaIndex(FileConfiguration config)
    {
        int arenaIndex = 1;
        while(config.contains("Arenas.Arena" + arenaIndex))
        {
            arenaIndex ++;
        }

        return arenaIndex;
    }

    public static boolean isInteger(String strNum)
    {
        if (strNum == null)
        {
            return false;
        }
        try
        {
            Integer.parseInt(strNum);
        }
        catch (NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}