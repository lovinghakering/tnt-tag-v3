package me.ObichamDaQmKir.tnttag.bungeecord;

import me.ObichamDaQmKir.tnttag.TntTag;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BungeeMessages
{
    private final TntTag plugin;

    public BungeeMessages(TntTag plugin)
    {
        this.plugin = plugin;
    }

    public void sendServer(Player player, String server)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

        try
        {
            dataOutputStream.writeUTF("Connect");
            dataOutputStream.writeUTF(server);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        player.sendPluginMessage(plugin, "BungeeCord", byteArrayOutputStream.toByteArray());
    }
}
