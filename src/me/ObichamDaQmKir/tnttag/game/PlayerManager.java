package me.ObichamDaQmKir.tnttag.game;

import java.util.UUID;

public class PlayerManager
{
    public final UUID uuid;
    public boolean inGame = false;
    public boolean spectator = false;
    public int arenaIndex = 0;
    public boolean hasTnt = false;
    public int coinsEarned = 0;

    public PlayerManager(UUID uuid)
    {
        this.uuid = uuid;
    }

    public void reset()
    {
        inGame = false;
        spectator = false;
        arenaIndex = 0;
        hasTnt = false;
        coinsEarned = 0;
    }
}
