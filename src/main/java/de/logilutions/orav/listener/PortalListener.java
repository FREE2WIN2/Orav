package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

@RequiredArgsConstructor
public class PortalListener implements Listener {

    private final Orav orav;

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {
        if (event.getReason() == PortalCreateEvent.CreateReason.FIRE && orav.getState() != Orav.State.DEVELOPING) {
            event.setCancelled(true);
        }
    }
}
