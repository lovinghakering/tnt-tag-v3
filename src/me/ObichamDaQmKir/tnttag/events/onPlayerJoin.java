package me.ObichamDaQmKir.tnttag.events;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class onPlayerJoin implements Listener
{
    private final TntTag plugin;

    public onPlayerJoin(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        plugin.playerManagers.put(uuid, new PlayerManager(uuid));
        player.teleport(plugin.hubLocation);
    }
}
