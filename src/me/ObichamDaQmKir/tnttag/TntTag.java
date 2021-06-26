package me.ObichamDaQmKir.tnttag;

import me.ObichamDaQmKir.tnttag.bungeecord.BungeeMessages;
import me.ObichamDaQmKir.tnttag.commands.DebugCommand;
import me.ObichamDaQmKir.tnttag.commands.LeaveCommand;
import me.ObichamDaQmKir.tnttag.commands.QuickJoinCommand;
import me.ObichamDaQmKir.tnttag.commands.TTCommand;
import me.ObichamDaQmKir.tnttag.events.onEntityDamageByEntity;
import me.ObichamDaQmKir.tnttag.events.onPlayerChangeWorld;
import me.ObichamDaQmKir.tnttag.events.onPlayerJoin;
import me.ObichamDaQmKir.tnttag.events.onPlayerQuit;
import me.ObichamDaQmKir.tnttag.game.ArenaManager;
import me.ObichamDaQmKir.tnttag.game.GameManager;
import me.ObichamDaQmKir.tnttag.game.PlayerManager;
import me.ObichamDaQmKir.tnttag.scoreboard.PlayerScoreboard;
import me.ObichamDaQmKir.tnttag.sql.SQLGetter;
import me.ObichamDaQmKir.tnttag.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class TntTag extends JavaPlugin
{
    public SQLManager sqlManager;
    public SQLGetter sqlGetter;
    public BungeeMessages bungeeMessages;
    public ArenaManager arenaManager;
    public PlayerScoreboard playerScoreboard;

    public HashMap<Integer, GameManager> gameManagers = new HashMap<>();
    public ArrayList<String> arenaWorlds = new ArrayList<>();
    public HashMap<UUID, PlayerManager> playerManagers = new HashMap<>();

    public Location hubLocation;
    public String serverIP;

    @Override
    public void onEnable()
    {
        initializeClasses();
        registerEvents();
        registerCommandExecutors();
        setupDatabase();
        loadArenas();
        saveDefaultConfig();
        serverIP = this.getConfig().getString("ServerIP");
        hubLocation = new Location(Bukkit.getWorld(this.getConfig().getString("HubLocation.World")),
                this.getConfig().getDouble("HubLocation.X"),
                this.getConfig().getDouble("HubLocation.Y"),
                this.getConfig().getDouble("HubLocation.Z"),
                (float) this.getConfig().getDouble("HubLocation.Yaw"),
                (float) this.getConfig().getDouble("HubLocation.Pitch"));

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "TNT TAG has been enabled!");
    }

    @Override
    public void onDisable()
    {
        sqlManager.disconnect();
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "TNT TAG has been Disabled!");
    }

    private void registerEvents()
    {
        getServer().getPluginManager().registerEvents(new onPlayerJoin(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerQuit(this), this);
        getServer().getPluginManager().registerEvents(new onPlayerChangeWorld(this), this);
        getServer().getPluginManager().registerEvents(new onEntityDamageByEntity(this), this);
    }

    private void registerCommandExecutors()
    {
        getCommand("tt").setExecutor(new TTCommand(this));
        getCommand("quickjoin").setExecutor(new QuickJoinCommand(this));
        getCommand("leave").setExecutor(new LeaveCommand(this));
        getCommand("debug").setExecutor(new DebugCommand(this));
    }

    private void setupDatabase()
    {
        if (sqlManager.connect())
            getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "Database is connected!");
        else
            getServer().getConsoleSender().sendMessage(ChatColor.RED + "Database is not connected");
    }

    private void initializeClasses()
    {
        sqlManager = new SQLManager(this);
        sqlGetter = new SQLGetter(this);
        bungeeMessages = new BungeeMessages(this);
        arenaManager = new ArenaManager(this);
        playerScoreboard = new PlayerScoreboard(this);
    }

    private void loadArenas()
    {
        int arenaIndex = 1;
        while (arenaIndex > 0)
        {
            arenaIndex = loadArena(arenaIndex);
        }
    }

    private int loadArena(int arenaIndex)
    {
        if (getConfig().contains("Arenas.Arena" + arenaIndex))
        {
            gameManagers.put(arenaIndex, new GameManager(this, arenaIndex));
            arenaWorlds.add(getConfig().getString("Arenas.Arena" + arenaIndex + ".Locations.GameLocation.World"));
            arenaIndex++;
        }

        if (getConfig().contains("Arenas.Arena" + arenaIndex))
        {
            return arenaIndex;
        }

        return -1;
    }
}
