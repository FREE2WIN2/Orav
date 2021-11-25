package de.logilutions.orav.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event){
        event.setCancelled(true);
    }
}