package me.ObichamDaQmKir.tnttag.commands;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuickJoinCommand implements CommandExecutor
{
    private final TntTag plugin;

    public QuickJoinCommand(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (label.equalsIgnoreCase("quickjoin"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            PlayerManager pm = plugin.playerManagers.get(player.getUniqueId());
            if (pm.arenaIndex == 0)
            {
                quickJoin(player);
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "You are already in a game!");
            }
            return true;
        }
        return false;
    }

    private void quickJoin(Player player)
    {
        int mostPlayersCount = 0;
        GameManager mostPlayersGM = null;
        for (int i = 1; i < plugin.gameManagers.size() + 1; i++)
        {
            GameManager gm = plugin.gameManagers.get(i);
            if (mostPlayersGM == null)
            {
                mostPlayersCount = gm.gamePlayers.size();
                mostPlayersGM = gm;
            }
            else if (gm.gamePlayers.size() > mostPlayersCount)
            {
                if (gm.gamePlayers.size() < gm.maxPlayers)
                {
                    mostPlayersCount = gm.gamePlayers.size();
                    mostPlayersGM = gm;
                }
            }
        }

        if (mostPlayersGM == null) { player.sendMessage(ChatColor.RED + "There are no available games!"); return; }
        mostPlayersGM.addPlayerToGame(player);
    }
}
