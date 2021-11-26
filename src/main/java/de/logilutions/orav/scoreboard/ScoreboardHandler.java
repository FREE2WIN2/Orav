package de.logilutions.orav.scoreboard;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.team.OravTeam;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@RequiredArgsConstructor
public class ScoreboardHandler {

    private final OravPlayerManager oravPlayerManager;

    public void playerSpawned(Player player) {
        System.out.println("player spawned");
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        addToScoreboard(oravPlayer, scoreboard, true);
        for (OravPlayer oravPlayer1 : oravPlayerManager.getAll()) {
            if (oravPlayer1.equals(oravPlayer)) {
                continue;
            }
            Player player1 = oravPlayer1.getPlayer();
            if (player1 == null) {
                continue;
            }
            addToScoreboard(oravPlayer1, scoreboard, false);
            addToScoreboard(oravPlayer, player1.getScoreboard(), false);
        }
        player.setScoreboard(scoreboard);
    }

    public void playerQuit(Player player) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        OravTeam oravTeam = oravPlayer.getOravTeam();
        if (oravTeam == null) {
            return;
        }
        String teamName = oravTeam.getShortName();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team team = p.getScoreboard().getTeam(teamName);
            if (team != null) {
                team.removePlayer(player);
            }
        }
    }

    private void addToScoreboard(OravPlayer oravPlayer, Scoreboard scoreboard, boolean displayname) {
        Player player = oravPlayer.getPlayer();
        if (player == null) {
            return;
        }
        OravTeam oravTeam = oravPlayer.getOravTeam();
        if (oravTeam == null) {
            return;
        }
        net.md_5.bungee.api.ChatColor color = oravTeam.getTeamColor().getChatColor();
        String teamName = oravTeam.getShortName();
        Team team = scoreboard.getTeam(teamName);
        String prefix = color.toString() + oravTeam.getShortName() + " §7| ";
        if (team == null) {
            team = scoreboard.registerNewTeam(teamName);
            team.setPrefix(prefix);
            team.setColor(ChatColor.GRAY);
            team.setAllowFriendlyFire(false);
            team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
        }
        team.addEntry(player.getName());

        if (displayname) {
            player.setDisplayName(prefix + player.getName());
        }
    }
}
