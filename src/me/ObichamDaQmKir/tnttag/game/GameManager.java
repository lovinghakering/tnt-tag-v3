package me.ObichamDaQmKir.tnttag.game;

import me.ObichamDaQmKir.tnttag.TntTag;
import me.ObichamDaQmKir.tnttag.Utilities;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.UUID;

public class GameManager
{
    private final TntTag plugin;
    public final int arenaIndex;
    public String name;

    public GameState state;
    public int round = 1;

    public ArrayList<UUID> allPlayers = new ArrayList<>();
    public ArrayList<UUID> gamePlayers = new ArrayList<>();
    public ArrayList<UUID> alivePlayers = new ArrayList<>();
    public ArrayList<UUID> taggers = new ArrayList<>();
    public ArrayList<UUID> placement = new ArrayList<>();

    public int maxPlayers;
    public int neededPlayers;

    private int lobbyCountdownTime;
    private int winnerCountdownTime;
    private int explosionCountdownShortTime;
    private int explosionCountdownLongTime;

    public int explosionTimerCounter;
    public int lobbyTimerCounter;
    public int winnerTimerCounter;

    private Location lobbyLocation;
    private Location gameLocation;
    private Location spectatorLocation;

    public GameManager(TntTag plugin, int arenaIndex)
    {
        this.plugin = plugin;

        state = GameState.LOBBY;
        round = 1;
        this.arenaIndex = arenaIndex;

        loadValuesFromConfig(arenaIndex);
        lobbyTimerCounter = lobbyCountdownTime;
        winnerTimerCounter = winnerCountdownTime;
        oneSecTimerAsync();
        oneSecTimer();
    }

    private void setupGame()
    {
        state = GameState.LOBBY;

        lobbyTimerCounter = lobbyCountdownTime;
        winnerTimerCounter = winnerCountdownTime;
        round = 1;
    }

    private void loadValuesFromConfig(int arenaIndex)
    {
        String arenaName = "Arena" + arenaIndex;
        name = plugin.getConfig().getString("Arenas." + arenaName + ".GameDetails.MapName");
        lobbyCountdownTime = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.LobbyCountdown");
        winnerCountdownTime = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.WinnerCountdown");
        explosionCountdownShortTime = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.ExplosionCountdownShort");
        explosionCountdownLongTime = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.ExplosionCountdownLong");

        maxPlayers = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.MaxPlayers");
        neededPlayers = plugin.getConfig().getInt("Arenas." + arenaName + ".GameDetails.PlayersNeeded");

        lobbyLocation = new Location(Bukkit.getWorld(plugin.getConfig().getString("Arenas." + arenaName + ".Locations.LobbyLocation.World")),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.LobbyLocation.X"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.LobbyLocation.Y"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.LobbyLocation.Z"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.LobbyLocation.Yaw"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.LobbyLocation.Pitch"));
        gameLocation = new Location(Bukkit.getWorld(plugin.getConfig().getString("Arenas." + arenaName + ".Locations.GameLocation.World")),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.GameLocation.X"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.GameLocation.Y"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.GameLocation.Z"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.GameLocation.Yaw"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.GameLocation.Pitch"));
        spectatorLocation = new Location(Bukkit.getWorld(plugin.getConfig().getString("Arenas." + arenaName + ".Locations.SpectatorLocation.World")),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.SpectatorLocation.X"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.SpectatorLocation.Y"),
                plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.SpectatorLocation.Z"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.SpectatorLocation.Yaw"),
                (float) plugin.getConfig().getDouble("Arenas." + arenaName + ".Locations.SpectatorLocation.Pitch"));
    }

    private void startGame()
    {
        state = GameState.PLAYING;
        for (UUID uuid : gamePlayers)
        {
            Player player = Bukkit.getPlayer(uuid);
            player.teleport(gameLocation);
        }
    }

    public void addPlayerToGame(Player player)
    {
        UUID uuid = player.getUniqueId();
        allPlayers.add(uuid);
        gamePlayers.add(uuid);
        alivePlayers.add(uuid);

        player.getInventory().clear();
        player.getInventory().setHelmet(null);

        PlayerManager pm = plugin.playerManagers.get(uuid);
        pm.arenaIndex = arenaIndex;
        pm.inGame = true;
        pm.coinsEarned = 0;
        pm.hasTnt = false;
        if (state == GameState.LOBBY || state == GameState.STARTING)
        {
            player.teleport(lobbyLocation);
            pm.spectator = false;
            plugin.playerScoreboard.lobbyScoreboard(player);
        }
        else
        {
            player.teleport(spectatorLocation);
            pm.spectator = true;
        }

        if (gamePlayers.size() >= neededPlayers)
        {
            state = GameState.STARTING;
        }
        updateScoreboard();
    }

    public void removePlayer(UUID uuid)
    {
        switch (state)
        {
            case LOBBY:
                removePlayerFromLobby(uuid);
                break;
            case STARTING:
                removePlayerFromStarting(uuid);
                break;
            case PLAYING:
                removePlayerFromGame(uuid);
                break;
            case ENDING:
                removePlayerFromEnd(uuid);
                break;
        }

        Player player = Bukkit.getPlayer(uuid);
        player.teleport(plugin.hubLocation);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        player.setPlayerListName(ChatColor.WHITE + player.getDisplayName());
    }

    private void removePlayerFromLobby(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);
        PlayerManager pm = plugin.playerManagers.get(uuid);

        allPlayers.remove(uuid);
        gamePlayers.remove(uuid);
        alivePlayers.remove(uuid);

        player.getInventory().clear();
        player.getInventory().setHelmet(null);

        pm.reset();
    }

    private void removePlayerFromStarting(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);

        allPlayers.remove(uuid);
        gamePlayers.remove(uuid);
        alivePlayers.remove(uuid);

        player.getInventory().clear();
        player.getInventory().setHelmet(null);

        PlayerManager pm = plugin.playerManagers.get(uuid);
        pm.reset();

        if (gamePlayers.size() < neededPlayers)
        {
            state = GameState.LOBBY;
            lobbyTimerCounter = lobbyCountdownTime;
            Bukkit.broadcastMessage(ChatColor.RED + "Not enough players to start the game " + ChatColor.YELLOW + "(" + gamePlayers.size() + "/" + neededPlayers + ")" + ChatColor.RED + "!");
        }
    }

    private void removePlayerFromGame(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);

        allPlayers.remove(uuid);
        gamePlayers.remove(uuid);
        alivePlayers.remove(uuid);

        PlayerManager pm = plugin.playerManagers.get(uuid);

        player.getInventory().clear();
        player.getInventory().setHelmet(null);

        pm.reset();

        if (alivePlayers.size() <= 1)
        {
            winGame();
        }
    }

    private void removePlayerFromEnd(UUID uuid)
    {
        Player player = Bukkit.getPlayer(uuid);

        allPlayers.remove(uuid);
        gamePlayers.remove(uuid);
        alivePlayers.remove(uuid);

        PlayerManager pm = plugin.playerManagers.get(uuid);

        if (state == GameState.PLAYING)
        {
            if (alivePlayers.size() <= 1)
                winGame();
        }

        player.getInventory().clear();
        player.getInventory().setHelmet(null);

        pm.reset();

        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

    private void winGame()
    {
        state = GameState.ENDING;
        try
        {
            Bukkit.broadcastMessage("1st: " + Bukkit.getPlayer(placement.get(0)).getDisplayName());
            Bukkit.broadcastMessage("2nd: " + Bukkit.getPlayer(placement.get(1)).getDisplayName());
            Bukkit.broadcastMessage("3rd: " + Bukkit.getPlayer(placement.get(2)).getDisplayName());
        } catch (Exception ignored) {}

    }

    private void oneSecTimer()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                updateScoreboard();
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    private void updateScoreboard()
    {
        if (state != GameState.PLAYING)
            return;

        for (UUID uuid : allPlayers)
        {
            Player player = Bukkit.getPlayer(uuid);
            plugin.playerScoreboard.gameScoreboard(player);
        }
    }

    private void oneSecTimerAsync()
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                switch (state)
                {
                    case STARTING:
                        lobbyTimerCounter--;
                        for (UUID uuid : allPlayers)
                        {
                            Player player = Bukkit.getPlayer(uuid);
                            player.setLevel(lobbyTimerCounter);
                        }
                        if (lobbyTimerCounter <= 0)
                        {
                            startGame();
                        }
                        break;
                    case PLAYING:
                        explosionTimerCounter--;
                        if (explosionTimerCounter <= 0)
                        {
                            explodeTnt();
                            placeTnt();
                        }
                        break;
                    case ENDING:
                        winnerTimerCounter--;
                        if (winnerTimerCounter <= 0)
                        {
                            for (UUID uuid : allPlayers)
                            {
                                removePlayer(uuid);
                            }
                        }
                        break;
                }
            }
        }.runTaskTimerAsynchronously(plugin, 0, 20L);
    }

    private void placeTnt()
    {
        if (alivePlayers.size() <= 5)
        {
            explosionTimerCounter = explosionCountdownShortTime;
            int taggerIndex = Utilities.randomNumber(0, alivePlayers.size() - 1);
            Player tagger = Bukkit.getPlayer(alivePlayers.get(taggerIndex));
            PlayerManager taggerManager = plugin.playerManagers.get(tagger.getUniqueId());
            taggerManager.hasTnt = true;
            tagger.playSound(tagger.getLocation(), Sound.FUSE, 1, 1);
            tagger.sendMessage( ChatColor.RED + "You have been tagged!!!");
            tagger.getInventory().addItem(new ItemStack(Material.TNT, 576));
            tagger.getInventory().setHelmet(new ItemStack(Material.TNT, 1));
            tagger.setPlayerListName(ChatColor.RED + tagger.getDisplayName());
        }
        else
        {
            explosionTimerCounter = explosionCountdownLongTime;
            int neededTaggers = alivePlayers.size() / 4;

            while (neededTaggers >= 1)
            {
                int taggerIndex = Utilities.randomNumber(0, alivePlayers.size() - 1);
                Player tagger = Bukkit.getPlayer(alivePlayers.get(taggerIndex));
                PlayerManager taggerManager = plugin.playerManagers.get(tagger.getUniqueId());
                if (!taggerManager.hasTnt)
                {
                    taggerManager.hasTnt = true;
                    tagger.playSound(tagger.getLocation(), Sound.FUSE, 1, 1);
                    tagger.sendMessage( ChatColor.RED + "You have been tagged!!!");
                    tagger.getInventory().addItem(new ItemStack(Material.TNT, 576));
                    tagger.getInventory().setHelmet(new ItemStack(Material.TNT, 1));
                    tagger.setPlayerListName(ChatColor.RED + tagger.getDisplayName());
                    neededTaggers--;
                }
            }
        }
    }

    private void explodeTnt()
    {
        for (UUID uuid : alivePlayers)
        {
            PlayerManager pm = plugin.playerManagers.get(uuid);
            if (pm.hasTnt)
            {
                pm.hasTnt = false;
                pm.spectator = true;
                plugin.sqlGetter.addCoins(uuid, pm.coinsEarned);
                pm.coinsEarned = 0;
                Player player = Bukkit.getPlayer(uuid);
                player.getInventory().clear();
                player.getInventory().setHelmet(null);
                player.setPlayerListName(ChatColor.WHITE + player.getDisplayName());
                player.teleport(spectatorLocation);
                placement.add(0, player.getUniqueId());
                alivePlayers.remove(uuid);
                round++;

                if (state == GameState.PLAYING)
                {
                    if (alivePlayers.size() <= 1)
                    {
                        placement.add(0, alivePlayers.get(0));
                        winGame();
                    }
                }
            }
        }
    }
}
