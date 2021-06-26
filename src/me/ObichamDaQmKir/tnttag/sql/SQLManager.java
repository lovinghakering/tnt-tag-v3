package me.ObichamDaQmKir.tnttag.sql;

import me.ObichamDaQmKir.tnttag.TntTag;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager
{
    private final TntTag plugin;

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    private Connection connection;

    public SQLManager(TntTag plugin)
    {
        this.plugin = plugin;
        host = plugin.getConfig().getString("SQL.Host");
        port = plugin.getConfig().getString("SQL.Port");
        database = plugin.getConfig().getString("SQL.Database");
        username = plugin.getConfig().getString("SQL.Username");
        password = plugin.getConfig().getString("SQL.Password");
    }

    public boolean isConnected()
    {
        return connection != null;
    }

    public boolean connect()
    {
        try
        {
            if (!isConnected())
            {
                connection = DriverManager.getConnection("jdbc:mysql://" +
                                host + ":" + port + "/" + database + "?characterEncoding=latin1",
                        username, password);
                return isConnected();
            }
            else
            {
                return true;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public void disconnect()
    {
        if (isConnected())
        {
            try
            {
                connection.close();
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection()
    {
        return connection;
    }
}
