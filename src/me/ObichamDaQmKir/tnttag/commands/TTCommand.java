package me.ObichamDaQmKir.tnttag.commands;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.Utilities;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TTCommand implements CommandExecutor
{
    private final TntTag plugin;

    public TTCommand(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (label.equalsIgnoreCase("tt"))
        {
            if (args.length > 0)
            {
                //at least one argument
                if (args[0].equalsIgnoreCase("arena"))
                {
                    if (args.length > 1)
                    {
                        if (args[1].equalsIgnoreCase("create"))
                        {
                            if (args.length > 2)
                            {
                                plugin.arenaManager.createArena(sender, args[2]);
                                return true;
                            }
                            else
                            {
                                return argsError(sender);
                            }
                        }
                        else if (args[1].equalsIgnoreCase("delete") || args[1].equalsIgnoreCase("remove"))
                        {
                            if (args.length > 2)
                            {
                                if (Utilities.isInteger(args[2]))
                                {
                                    plugin.arenaManager.removeArena(sender, Integer.parseInt(args[2]));
                                }
                                else
                                {
                                    sender.sendMessage(ChatColor.RED + "Usage: /tt arena remove <arena index>");
                                }
                                return true;
                            }
                            else
                            {
                                return argsError(sender);
                            }
                        }
                        else if (args[1].equalsIgnoreCase("manage"))
                        {
                            if (sender instanceof Player)
                            {
                                if (args.length > 2)
                                {
                                    Player player = (Player) sender;
                                    plugin.arenaManager.manageArena(player, Integer.parseInt(args[2]));
                                    return true;
                                }
                                else
                                {
                                    return argsError(sender);
                                }
                            }
                            else
                            {
                                sender.sendMessage("Only players can use this command!");
                                return true;
                            }
                        }
                    }
                    else
                        return argsError(sender);
                }
            }
            else
                return argsError(sender);
        }
        return false;
    }

    private boolean argsError(CommandSender sender)
    {
        sender.sendMessage(ChatColor.RED + "Not enough arguments!");
        return true;
    }
}
