package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@RequiredArgsConstructor
public class PlayerJoinQuitListener implements Listener {

    private final OravPlayerManager oravPlayerManager;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(event.getPlayer().getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        if (oravPlayer.isDroppedOut()) {
            //TODO dropped out
            return;
        }

        //TODO start sessionwatcer
    }
}
