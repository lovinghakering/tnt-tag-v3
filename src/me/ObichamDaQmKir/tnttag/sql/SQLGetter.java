package me.ObichamDaQmKir.tnttag.sql;

import me.ObichamDaQmKir.tnttag.TntTag;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter
{
    private final TntTag plugin;

    public SQLGetter(TntTag plugin)
    {
        this.plugin = plugin;
    }

    public void addCoins(UUID uuid, int coinsEarned)
    {
        try
        {
            String totalCoins = String.valueOf(getCoins(uuid) + coinsEarned);

            PreparedStatement ps = plugin.sqlManager.getConnection().prepareStatement("UPDATE players SET coins=? WHERE uuid=?");
            ps.setString(1, totalCoins);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();

            Bukkit.getPlayer(uuid).sendMessage(ChatColor.GREEN + "Congratulations you won " + ChatColor.YELLOW + coinsEarned + " coins " + ChatColor.GREEN + ", now you have a total of " + ChatColor.YELLOW + totalCoins + " coins " + ChatColor.GREEN + "!");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public int getCoins(UUID uuid)
    {
        try
        {
            PreparedStatement ps = plugin.sqlManager.getConnection().prepareStatement("SELECT coins FROM players WHERE uuid=?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next())
            {
                return rs.getInt("coins");
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return 0;
    }
}
