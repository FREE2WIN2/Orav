package de.logilutions.orav.listener;

import de.logilutions.orav.discord.DiscordUtil;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.awt.*;

@AllArgsConstructor
public class PlayerDeathListener implements Listener {

    private DiscordUtil discordUtil;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        discordUtil.send(
                "Spieler ausgeschieden",
                event.getDeathMessage(),
                "https://visage.surgeplay.com/full/" + player.getUniqueId(),
                Color.RED,
                null, null, null);
    }
}
