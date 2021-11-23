package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.scoreboard.ScoreboardHandler;
import de.logilutions.orav.session.SessionObserver;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class PlayerJoinQuitListener implements Listener {

    private final OravPlayerManager oravPlayerManager;
    private final DatabaseHandler databaseHandler;
    @Setter
    private final Orav orav;
    private final SessionObserver sessionObserver;
    private final ScoreboardHandler scoreboardHandler;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (orav == null) {
            return;
        }
        Player player = event.getPlayer();
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        LocalTime now = LocalTime.now();
        if (orav.getEarlyLogin().isBefore(now) || orav.getLatestLogin().isBefore(now)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String from = orav.getEarlyLogin().format(formatter);
            String to = orav.getLatestLogin().format(formatter);
            player.kickPlayer("Heute darf nicht mehr gespielt werden! (Nur von " + from + "Uhr bis " + to + "Uhr)");
            return;
        }

        if (oravPlayer.isDroppedOut()) {
            OravPlayer teamMate = databaseHandler.getTeamMate(oravPlayer);
            if (teamMate.isDroppedOut()) {
                player.setGameMode(GameMode.SPECTATOR);
                scoreboardHandler.playerSpawned(player);
            } else {
                player.kickPlayer("Du bist ausgeschieden! Du darfst erst spectaten, wenn dein Teammate getötet wurde!");
            }
            return;
        }
        sessionObserver.startSession(oravPlayer);
        scoreboardHandler.playerSpawned(player);
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (orav == null) {
            return;
        }
        Player player = event.getPlayer();
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        sessionObserver.endSession(oravPlayer);
        scoreboardHandler.playerQuit(player);
    }

}
