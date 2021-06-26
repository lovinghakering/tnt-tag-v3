package me.ObichamDaQmKir.tnttag.game;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ArenaManager
{
    private final TntTag plugin;

    private HashMap<UUID, Integer> managers = new HashMap<>();

    public ArenaManager(TntTag plugin)
    {
        this.plugin = plugin;
    }

    public void createArena(CommandSender sender, String name)
    {
        int arenaIndex = Utilities.nextArenaIndex(plugin.getConfig());
        if (plugin.getConfig().contains("Arenas.Arena" + arenaIndex))
        {
            sender.sendMessage(ChatColor.RED + "Something went really wrong!");
            return;
        }
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.MapName", name);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.LobbyCountdown", 10);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.WinnerCountdown", 3);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.ExplosionCountdownShort", 30);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.ExplosionCountdownLong", 60);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.PlayersNeeded", 2);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".GameDetails.MaxPlayers", 12);
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".Locations.Lobby", new Location(Bukkit.getWorld("world"), 0, 60, 0, 0, 0));
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".Locations.Game", new Location(Bukkit.getWorld("world"), 0, 60, 0, 0, 0));
        plugin.getConfig().set("Arenas.Arena" + arenaIndex + ".Locations.Spectate", new Location(Bukkit.getWorld("world"), 0, 60, 0, 0, 0));
        plugin.saveConfig();
        sender.sendMessage(ChatColor.GREEN + "Successfully crated a new arena");
    }

    public void removeArena(CommandSender sender, int index)
    {
        if (plugin.getConfig().contains("Arenas.Arena" + index))
        {
            plugin.getConfig().set("Arenas.Arena" + index, null);
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Successfully removed an arena!");
        }
        else
        {
            sender.sendMessage(ChatColor.RED + "Arena with that index does not exist!");
        }
    }

    public void manageArena(Player player, int index)
    {
        if (plugin.getConfig().contains("Arenas.Arena" + index))
        {
            if (managers.containsKey(player.getUniqueId()))
            {
                if (managers.get(player.getUniqueId()) == index)
                {
                    player.sendMessage(ChatColor.YELLOW + "You are already managing Arena" + index + "!");
                    return;
                }
                managers.remove(player.getUniqueId());
            }

            managers.put(player.getUniqueId(), index);
            player.sendMessage(ChatColor.GREEN + "Managing Arena" + index);
        }
        else
        {
            player.sendMessage(ChatColor.RED + "Arena with that index does not exist!");
        }
    }

    public void removeManager(UUID uuid)
    {
        managers.remove(uuid);
    }
}
