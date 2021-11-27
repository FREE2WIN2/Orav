package de.logilutions.orav.listener;

import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.discord.DiscordUtil;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.util.Helper;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.awt.*;

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private DiscordUtil discordUtil;
    private final OravPlayerManager oravPlayerManager;
    private final DatabaseHandler databaseHandler;
    private final Helper helper;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        if(oravPlayer.getCurrentSession() == null){
            return;
        }
        oravPlayer.setDroppedOut(true);
        databaseHandler.stopSession(oravPlayer);
        databaseHandler.updatePlayer(oravPlayer);
        discordUtil.send(
                "Spieler ausgeschieden",
                event.getDeathMessage(),
                "https://visage.surgeplay.com/full/" + player.getUniqueId(),
                Color.RED,
                null, null, null);

        for(Player player1: Bukkit.getOnlinePlayers()){
            player1.playSound(player1.getLocation(), Sound.ENTITY_WITHER_DEATH,1,1);
        }

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        if (!helper.handleSpawn(oravPlayer)) {
            event.setRespawnLocation(player.getLocation());
        }
    }
}
