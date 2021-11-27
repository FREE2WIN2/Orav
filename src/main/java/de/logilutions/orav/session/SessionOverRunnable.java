package de.logilutions.orav.session;

import de.logilutions.orav.player.OravPlayer;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SessionOverRunnable implements Runnable {
    private final OravPlayer oravPlayer;
    private final Consumer<OravPlayer> onEnd;
    private final PotionEffect potionEffect = new PotionEffect(PotionEffectType.BLINDNESS, 80, 255, false, false, false);

    @Override
    public void run() {

        Player player = oravPlayer.getPlayer();
        if (player == null) {
            onEnd.accept(oravPlayer);
            return;
        }
        if (!oravPlayer.isHasValidSession() && !oravPlayer.isOravAdmin()) {
            player.addPotionEffect(potionEffect);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§cDeine Spielzeit ist abgelaufen!"));
        }
    }
}
