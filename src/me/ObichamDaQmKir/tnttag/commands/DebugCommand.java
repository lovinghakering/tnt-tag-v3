package me.ObichamDaQmKir.tnttag.commands;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.Utilities;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.UUID;

public class DebugCommand implements CommandExecutor
{
    private final TntTag plugin;

    public DebugCommand(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (label.equalsIgnoreCase("debug"))
        {
            if (!sender.hasPermission("tnttag.debug"))
            {
                sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
                return true;
            }

            if (args.length >= 1)
            {
                if (args[0].equalsIgnoreCase("gm"))
                {
                    if (args.length >= 2)
                    {
                        if (args[1].equalsIgnoreCase("ls"))
                        {
                            if (args.length >= 3)
                            {
                                if (args[2].equalsIgnoreCase("p"))
                                {
                                    if (args.length >= 4)
                                    {
                                        if (Utilities.isInteger(args[3]))
                                        {
                                            listGameManagersPlayers(sender, Integer.parseInt(args[3]));
                                        }
                                        else
                                        {
                                            sender.sendMessage(ChatColor.RED + "Usage: /debug gm ls p <arena index>");
                                            return true;
                                        }
                                    }
                                    else
                                    {
                                        sender.sendMessage(ChatColor.RED + "Not enough arguments!");
                                        return true;
                                    }
                                }
                                else
                                {
                                    sender.sendMessage(ChatColor.RED + "This command does not exist!");
                                    return true;
                                }
                            }
                            else
                            {
                                listGameManagers(sender);
                                return true;
                            }
                        }
                        else
                        {
                            sender.sendMessage(ChatColor.RED + "This command does not exist!");
                            return true;
                        }
                    }
                    else
                    {
                        sender.sendMessage(ChatColor.RED + "Not enough arguments!");
                        return true;
                    }
                }
            }
            else
            {
                sender.sendMessage(ChatColor.RED + "Not enough arguments!");
                return true;
            }
        }
        return false;
    }

    private void listGameManagers(CommandSender sender)
    {
        StringBuilder msg = new StringBuilder("Game Managers:\n");
        for (GameManager gm : plugin.gameManagers.values())
        {
            msg.append(gm.arenaIndex).append(". ").append(gm.name).append("\n");
        }
        sender.sendMessage(msg.toString());
    }

    private void listGameManagersPlayers(CommandSender sender, int arenaIndex)
    {
        GameManager gm = plugin.gameManagers.get(arenaIndex);

        StringBuilder msg = new StringBuilder(ChatColor.GRAY + "Players:\nAlive:\n" + ChatColor.WHITE);
        for (UUID uuid : gm.gamePlayers)
        {
            msg.append(Bukkit.getPlayer(uuid).getDisplayName()).append("\n");
        }

        msg.append(ChatColor.GRAY + "Dead:\n" + ChatColor.WHITE);
        ArrayList<UUID> dead = new ArrayList<>(gm.gamePlayers);
        dead.removeAll(gm.alivePlayers);
        for (UUID uuid : dead)
        {
            msg.append(Bukkit.getPlayer(uuid).getDisplayName()).append("\n");
        }

        msg.append(ChatColor.GRAY + "Spectators:\n" + ChatColor.WHITE);
        ArrayList<UUID> spectators = new ArrayList<>(gm.allPlayers);
        spectators.removeAll(gm.gamePlayers);
        for (UUID uuid : spectators)
        {
            msg.append(Bukkit.getPlayer(uuid).getDisplayName()).append("\n");
        }

        sender.sendMessage(msg.toString());
    }
}
