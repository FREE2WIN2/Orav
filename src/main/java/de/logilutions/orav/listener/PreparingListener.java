package de.logilutions.orav.listener;

import de.logilutions.orav.Orav;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

@RequiredArgsConstructor
public class PreparingListener implements Listener {

    private final Orav orav;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (orav.getState() == Orav.State.PREPARATION || orav.getState() == Orav.State.COUNTDOWN) {
            event.setCancelled(true);
        }
    }
}
