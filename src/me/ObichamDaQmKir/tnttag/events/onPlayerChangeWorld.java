package me.ObichamDaQmKir.tnttag.events;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class onPlayerChangeWorld implements Listener
{
    private final TntTag plugin;

    public onPlayerChangeWorld(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChangeWorld(PlayerChangedWorldEvent event)
    {
        Player player = event.getPlayer();
        String toWorldName = player.getWorld().getName();

        String hubWorld = plugin.hubLocation.getWorld().getName();

        if (hubWorld.equals(toWorldName))
        {
            //Left from a game
            PlayerManager playerManager = plugin.playerManagers.get(player.getUniqueId());
            GameManager gameManager = plugin.gameManagers.get(playerManager.arenaIndex);

            gameManager.removePlayer(playerManager.uuid);
        }
    }
}
