package de.logilutions.orav.listener;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.util.Helper;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

@RequiredArgsConstructor
public class PlayerSessionListener implements Listener {
    private final OravPlayerManager oravPlayerManager;
    private final Helper helper;
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        handleEvent(event.getPlayer().getUniqueId(), event);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(event.getPlayer().getUniqueId());
        if(oravPlayer == null){
            return;
        }
        if (helper.teamIsDroppedOut(oravPlayer)) {
            return;
        }
        handleEvent(event.getPlayer().getUniqueId(), event);
    }

    private void handleEvent(UUID uuid, Cancellable cancellable) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(uuid);
        if (oravPlayer != null && !oravPlayer.isHasValidSession() && !oravPlayer.isOravAdmin()) {
            cancellable.setCancelled(true);
        }
    }
}
