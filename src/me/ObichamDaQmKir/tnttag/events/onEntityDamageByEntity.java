package me.ObichamDaQmKir.tnttag.events;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class onEntityDamageByEntity implements Listener
{
    private final TntTag plugin;

    public onEntityDamageByEntity(TntTag plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerHit(EntityDamageByEntityEvent event)
    {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            Player damager = (Player) event.getDamager();
            PlayerManager damagerManager = plugin.playerManagers.get(damager.getUniqueId());

            Player damaged = (Player) event.getEntity();
            PlayerManager damagedManager = plugin.playerManagers.get(damaged.getUniqueId());

            event.setDamage(0);

            if (damagerManager.hasTnt && !damagedManager.hasTnt)
            {
                untagPlayer(damager, damagerManager);
                tagPlayer(damaged, damagedManager);
            }
        }
    }

    private void tagPlayer(Player player, PlayerManager playerManager)
    {
        playerManager.hasTnt = true;
        player.getInventory().clear();
        player.getInventory().setHelmet(new ItemStack(Material.TNT));
        player.getInventory().addItem(new ItemStack(Material.TNT, 576));
        player.playSound(player.getLocation(), Sound.FUSE, 1, 1);
        player.setPlayerListName(ChatColor.RED + player.getName());
        Bukkit.broadcastMessage(ChatColor.RED + player.getDisplayName() + ChatColor.BLUE + " is IT");
    }

    private void untagPlayer(Player player, PlayerManager playerManager)
    {
        playerManager.hasTnt = false;
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.setPlayerListName(ChatColor.WHITE + player.getName());
    }
}
