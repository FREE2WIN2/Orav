package de.logilutions.orav.listener;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
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

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        handleEvent(event.getPlayer().getUniqueId(),event);
    }

    @EventHandler
    public void onInteract(PlayerMoveEvent event){
        handleEvent(event.getPlayer().getUniqueId(),event);
    }

    private void handleEvent(UUID uuid, Cancellable cancellable){
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(uuid);

        if(oravPlayer != null && (oravPlayer.isDroppedOut() || !oravPlayer.isHasValidSession())){
            cancellable.setCancelled(true);
        }
    }
}
