package de.logilutions.orav.listener;

import de.logilutions.orav.util.MessageManager;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

@AllArgsConstructor
public class TimsListener implements Listener {
    private final MessageManager messageManager;

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) return;

        Player player = (Player) event.getEntity();

        Location location = player.getLocation();

        if (isOutOfWorldBorder(location)) {
            event.setCancelled(true);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Beweg deinen fetten Arsch aus meiner schönen Border, du Hund."));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        Player player = event.getPlayer();

        if (!isOutOfWorldBorder(player.getLocation())) return;
    }

    public boolean isOutOfWorldBorder(Location location) {
        WorldBorder worldBorder = location.getWorld().getWorldBorder();
        Location center = worldBorder.getCenter();
        return center.distance(location) > worldBorder.getSize() / 2;
    }
}
