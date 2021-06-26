package me.ObichamDaQmKir.tnttag.commands;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LeaveCommand implements CommandExecutor
{
    private final TntTag plugin;

    public LeaveCommand(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (label.equalsIgnoreCase("leave"))
        {
            if (!(sender instanceof Player))
            {
                sender.sendMessage(ChatColor.RED + "Only players can use this command!");
                return true;
            }

            Player player = (Player) sender;
            PlayerManager pm = plugin.playerManagers.get(player.getUniqueId());
            if (pm.arenaIndex != 0)
            {
                GameManager gm = plugin.gameManagers.get(pm.arenaIndex);
                gm.removePlayer(player.getUniqueId());
            }
            else
            {
                player.sendMessage(ChatColor.RED + "There is no game to leave from!");
            }
            return true;
        }
        return false;
    }
}
