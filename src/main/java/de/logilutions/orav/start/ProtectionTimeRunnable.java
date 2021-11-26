package de.logilutions.orav.start;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.util.MessageManager;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Consumer;

public class ProtectionTimeRunnable implements Runnable {
    private long remainingMillis;
    private long lastSystemMillis = System.currentTimeMillis();
    private final MessageManager messageManager;
    private final Consumer<OravPlayer> onEnd;
    private final OravPlayer player;

    public ProtectionTimeRunnable(long remainingMillis, MessageManager messageManager, OravPlayer player, Consumer<OravPlayer> onEnd) {
        this.remainingMillis = remainingMillis;
        this.messageManager = messageManager;
        this.onEnd = onEnd;
        this.player = player;
    }

    @Override
    public void run() {
        long lastRemaining = remainingMillis;
        long currentSystemMillis =  System.currentTimeMillis();
        long delta = currentSystemMillis - lastSystemMillis;
        lastSystemMillis = currentSystemMillis;
        remainingMillis -= delta;
        Player p = player.getPlayer();
        if (p == null) {
            onEnd.accept(player);
            return;
        }
        if (remainingMillis <= 30 * 60 * 1000 && lastRemaining > 30 * 60 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in %hc%30 Minuten§7!");
        }

        if (remainingMillis <= 15 * 60 * 1000 && lastRemaining > 15 * 60 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in %hc%15 Minuten§7!");
        }

        if (remainingMillis <= 10 * 60 * 1000 && lastRemaining > 10 * 60 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in %hc%10 Minuten§7!");
        }

        if (remainingMillis <= 5 * 60 * 1000 && lastRemaining > 5 * 60 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in %hc%5 Minuten§7!");
        }

        if (remainingMillis <= 60 * 1000 && lastRemaining > 60 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%1 Minute§7!");
        }

        if (remainingMillis <= 30 * 1000 && lastRemaining > 30 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%30 Sekunden§7!");
        }

        if (remainingMillis <= 10 * 1000 && lastRemaining > 10 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%10 Sekunden§7!");
        }

        if (remainingMillis <= 3 * 1000 && lastRemaining > 3 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%3 Sekunden§7!");
        }

        if (remainingMillis <= 2 * 1000 && lastRemaining > 2 * 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%2 Sekunden§7!");
        }

        if (remainingMillis <= 1000 && lastRemaining > 1000) {
            messageManager.sendMessage(p, "Die Schutzzeit endet in%hc%1 Sekunde§7!");
        }

        if (remainingMillis <= 0) {
            onEnd.accept(null);
        }
    }
}
