package de.logilutions.orav.start;

import de.logilutions.orav.util.MessageManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class StartCountDownRunnable implements Runnable {

    private long remainingMillis = 5 * 60 * 1000 + 1; //5 Minutes
    private long lastSystemMillis = System.currentTimeMillis();
    private final MessageManager messageManager;
    private final Player player;
    private final Consumer<Player> onEnd;

    @Override
    public void run() {

        long lastRemaining = remainingMillis;
        long currentSystemMillis = System.currentTimeMillis();
        long delta = currentSystemMillis - lastSystemMillis;
        lastSystemMillis = currentSystemMillis;
        remainingMillis -= delta;


        if (remainingMillis <= 5 * 60 * 1000 && lastRemaining > 5 * 60 * 1000) {
            sendInfo(player, "5 Minuten", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 4 * 60 * 1000 && lastRemaining > 4 * 60 * 1000) {
            sendInfo(player, "4 Minuten", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 3 * 60 * 1000 && lastRemaining > 3 * 60 * 1000) {
            sendInfo(player, "3 Minuten", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 2 * 60 * 1000 && lastRemaining > 52* 60 * 1000) {
            sendInfo(player, "2 Minuten", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }


        if (remainingMillis <= 60 * 1000 && lastRemaining > 60 * 1000) {
            sendInfo(player, "1 Minute", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 30 * 1000 && lastRemaining > 30 * 1000) {
            sendInfo(player, "30 Sekunden", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 10 * 1000 && lastRemaining > 10 * 1000) {
            sendInfo(player, "10 Sekunden", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 3 * 1000 && lastRemaining > 3 * 1000) {
            sendInfo(player, "3 Sekunden", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 2 * 1000 && lastRemaining > 2 * 1000) {
            sendInfo(player, "2 Sekunden", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 1000 && lastRemaining > 1000) {
            sendInfo(player, "1 Sekunde", Sound.BLOCK_NOTE_BLOCK_PLING, 1);
        }

        if (remainingMillis <= 0) {
            if (player != null && player.isOnline()) {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                messageManager.sendMessage(player, "Orav5 ist gestartet!");
                messageManager.sendTitle(player, "§5Orav 5", "§7gestartet!");
                onEnd.accept(player);
            }
        }

    }

    private void sendInfo(Player player, String time, Sound sound, float pitch) {
        if (player != null && player.isOnline()) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%" + time + "§7!");
            player.playSound(player.getLocation(), sound, 1, pitch);
            messageManager.sendTitle(player, "§5Orav 5 §6startet in", "§7" + time);
        }
    }
}
