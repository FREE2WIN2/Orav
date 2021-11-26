package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import de.logilutions.orav.config.PlayerLogoutsConfig;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.discord.DiscordUtil;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.scoreboard.ScoreboardHandler;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.util.Helper;
import de.logilutions.orav.util.MessageManager;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.awt.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
public class PlayerJoinQuitListener implements Listener {
    private final DiscordUtil discordUtil;

    private final OravPlayerManager oravPlayerManager;
    @Setter
    private final Orav orav;
    private final SessionObserver sessionObserver;
    private final ScoreboardHandler scoreboardHandler;
    private final PlayerLogoutsConfig playerLogoutsConfig;
    private final Helper helper;
    private final MessageManager messageManager;
    private final DatabaseHandler databaseHandler;

    @EventHandler
    private void onLogin(AsyncPlayerPreLoginEvent event) {
        if (orav == null) {
            return;
        }
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(event.getUniqueId());
        if (oravPlayer == null) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage("§aDiesen Server dürfen nur angemeldete Spieler von Minecraft §5ORAV #5 §abetreten!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (orav == null) {
            return;
        }
        if (orav.getState() == Orav.State.DEVELOPING && !player.isOp()) {
            player.kickPlayer("§aMinecraft §5ORAV #5 §ahat noch nicht begonnen!");
            return;
        }
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        LocalTime now = LocalTime.now();
        if (orav.getEarlyLogin().isAfter(now) && orav.getLatestLogin().isBefore(now)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            String from = orav.getEarlyLogin().format(formatter);
            String to = orav.getLatestLogin().format(formatter);
            if (!player.isOp()) {
                player.kickPlayer("§aHeute darf nicht mehr gespielt werden! (Nur von §e" + from + "Uhr §abis §e" + to + "Uhr§a)");
            } else {
                messageManager.sendMessage(player, "KEINE SPIELZEIT! (Nur von " + from + "Uhr bis " + to + "Uhr)");
            }
            event.setJoinMessage("");
            return;
        }


        if (!helper.handleSpawn(oravPlayer)) {
            scoreboardHandler.playerSpawned(player);
            event.setJoinMessage(player.getDisplayName() + "§e hat als Zuschauer den Server betreten!");
            return;
        }
        sessionObserver.startSession(oravPlayer);
        if (!oravPlayer.isHasValidSession()) {
            if (!player.isOp()) {
                player.kickPlayer("§aDeine Spielzeit ist für heute abgelaufen!");
            } else {
                messageManager.sendMessage(player, "Deine Spielzeit ist abgelaufen! Bitte mach nur administrativen Quatsch!");
            }
            event.setJoinMessage("");
            return;
        }
        scoreboardHandler.playerSpawned(player);
        event.setJoinMessage(player.getDisplayName() + "§e hat den Server betreten!");
        discordUtil.send(
                null, null, null,
                Color.GRAY,
                player.getName() + " hat den Server betreten.",
                null,
                "https://visage.surgeplay.com/face/" + player.getUniqueId());


        if(databaseHandler.getSessions(oravPlayer).size() == 1){

        }
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
        discordUtil.send(
                null, null, null,
                Color.GRAY,
                player.getName() + " hat den Server verlassen.",
                null,
                "https://visage.surgeplay.com/face/" + player.getUniqueId());
        playerLogoutsConfig.saveLogOutPosition(player.getUniqueId(), player.getLocation());
        oravPlayerManager.removePlayer(oravPlayer.getUuid());
    }


}
