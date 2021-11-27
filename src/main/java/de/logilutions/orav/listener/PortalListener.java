package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

import java.util.Objects;

@RequiredArgsConstructor
public class PortalListener implements Listener {

    private final OravPlayerManager oravPlayerManager;
    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(Objects.requireNonNull(event.getEntity()).getUniqueId());
        if(oravPlayer == null){
            return;
        }
        if(!oravPlayer.isOravAdmin()){
            event.setCancelled(true);
        }
    }
}
