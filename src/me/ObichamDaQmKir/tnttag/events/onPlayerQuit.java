package me.ObichamDaQmKir.tnttag.events;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class onPlayerQuit implements Listener
{
    private final TntTag plugin;

    public onPlayerQuit(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        UUID uuid = event.getPlayer().getUniqueId();
        plugin.arenaManager.removeManager(uuid);
        PlayerManager pm = plugin.playerManagers.get(uuid);
        if (pm == null) return;
        plugin.gameManagers.get(pm.arenaIndex).removePlayer(uuid);
    }
}
