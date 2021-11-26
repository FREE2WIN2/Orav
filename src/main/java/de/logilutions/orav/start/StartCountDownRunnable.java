package de.logilutions.orav.start;

import de.logilutions.orav.util.MessageManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
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
        long currentSystemMillis =  System.currentTimeMillis();
        long delta = currentSystemMillis - lastSystemMillis;
        lastSystemMillis = currentSystemMillis;
        remainingMillis -= delta;


        if (remainingMillis <= 5 * 60 * 1000 && lastRemaining > 5 * 60 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%5 Minuten§7!");
        }

        if (remainingMillis <= 60 * 1000 && lastRemaining > 60 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%1 Minute§7!");
        }

        if (remainingMillis <= 30 * 1000 && lastRemaining > 30 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%30 Sekunden§7!");
        }

        if (remainingMillis <= 10 * 1000 && lastRemaining > 10 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%10 Sekunden§7!");
        }

        if (remainingMillis <= 3 * 1000 && lastRemaining > 3 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%3 Sekunden§7!");
        }

        if (remainingMillis <= 2 * 1000 && lastRemaining > 2 * 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%2 Sekunden§7!");
        }

        if (remainingMillis <= 1000 && lastRemaining > 1000) {
            messageManager.sendMessage(player, "Orav5 startet in %hc%1 Sekunde§7!");
        }

        if (remainingMillis <= 0) {
            if (player != null && player.isOnline()) {
                onEnd.accept(player);
            }
        }

    }
}
