package me.ObichamDaQmKir.tnttag.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class CustomScoreboard
{
    private ArrayList<String> scores = new ArrayList<>();;

    private int emptySpaces = 1;

    private final Scoreboard scoreboard;
    private final Objective obj;

    public CustomScoreboard(String label)
    {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreboard = manager.getNewScoreboard();
        obj = scoreboard.registerNewObjective(label, label);
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public void addEmptyLine()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < emptySpaces; i++)
            str.append(" ");

        scores.add(str.toString());
        emptySpaces++;
    }

    public void addLine(String label)
    {
        scores.add(label);
    }

    public Scoreboard getScoreboard()
    {
        int placeInScoreboard = scores.size() - 1;
        for (String s : scores)
        {
            Score score = obj.getScore(s);
            score.setScore(placeInScoreboard);
            placeInScoreboard--;
        }
        return scoreboard;
    }
}