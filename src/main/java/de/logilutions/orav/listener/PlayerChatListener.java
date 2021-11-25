package de.logilutions.orav.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    @EventHandler
    public void asyncChat(AsyncPlayerChatEvent event) {
        event.setCancelled(false);
        event.setFormat(event.getPlayer().getDisplayName() + "§7: §r%2$s");
    }
}
