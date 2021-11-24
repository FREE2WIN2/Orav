package de.logilutions.orav.listener;

import de.logilutions.orav.discord.DiscordWebhook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.awt.*;
import java.io.IOException;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity().getPlayer();

        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/912863008508760095/PYhV2onPsh-geKovWFeOSIWUt7_kh8rO27gTV796jtOIFHNyQz6kXEpxZPRxC2-dKDUh");
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject();
        embedObject.setTitle("Spieler ausgeschieden");
        embedObject.setDescription(event.getDeathMessage());
        embedObject.setImage("https://visage.surgeplay.com/full/" + player.getUniqueId());
        embedObject.setColor(Color.RED);
        webhook.addEmbed(embedObject);
        try {
            webhook.execute();
            System.out.println("executed dc send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
