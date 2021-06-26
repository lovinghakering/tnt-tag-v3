package me.ObichamDaQmKir.tnttag.scoreboard;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PlayerScoreboard
{
    private final TntTag plugin;

    public PlayerScoreboard(TntTag plugin)
    {
        this.plugin = plugin;
    }

    public void lobbyScoreboard(Player player)
    {
        GameManager gm = plugin.gameManagers.get(plugin.playerManagers.get(player.getUniqueId()).arenaIndex);
        CustomScoreboard customScoreboard = new CustomScoreboard(ChatColor.DARK_RED + "TNT " + ChatColor.WHITE + "Tag");
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Coins: " + ChatColor.GOLD + plugin.sqlGetter.getCoins(player.getUniqueId()));
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Players: " + ChatColor.GREEN + gm.gamePlayers.size());
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Needed players: " + ChatColor.YELLOW + gm.neededPlayers);
        customScoreboard.addEmptyLine();
        customScoreboard.addLine(ChatColor.DARK_PURPLE + plugin.serverIP);

        player.setScoreboard(customScoreboard.getScoreboard());
    }

    public void gameScoreboard(Player player)
    {
        PlayerManager pm = plugin.playerManagers.get(player.getUniqueId());
        if (pm == null) return;
        GameManager gm = plugin.gameManagers.get(pm.arenaIndex);
        if (gm == null) return;
        CustomScoreboard customScoreboard = new CustomScoreboard(ChatColor.DARK_RED + "TNT " + ChatColor.WHITE + "Tag");
        customScoreboard.addEmptyLine();
        customScoreboard.addLine(ChatColor.WHITE + "Round: " + ChatColor.AQUA + gm.round);
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Explosion in " + ChatColor.YELLOW + gm.explosionTimerCounter+ "s");
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Goal: " + (pm.hasTnt ? ChatColor.RED + "Tag someone!" : ChatColor.GREEN + "Run!"));
        customScoreboard.addEmptyLine();
        customScoreboard.addLine("Alive: " + ChatColor.GREEN + gm.alivePlayers.size() + " Players");
        customScoreboard.addEmptyLine();
        customScoreboard.addLine(ChatColor.DARK_PURPLE + plugin.serverIP);

        player.setScoreboard(customScoreboard.getScoreboard());
    }
}
