package de.logilutions.orav.session;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.util.MessageManager;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class SessionRunnable implements Runnable{
    private final OravPlayer oravPlayer;
    private long remainingMillis;
    private long lastRun = System.currentTimeMillis();
    private final MessageManager messageManager;
    private final Consumer<OravPlayer> onEnd;

    public SessionRunnable(OravPlayer oravPlayer, long remainingMillis, MessageManager messageManager, Consumer<OravPlayer> onEnd) {
        this.oravPlayer = oravPlayer;
        this.remainingMillis = remainingMillis;
        this.messageManager = messageManager;
        this.onEnd = onEnd;
    }

    @Override
    public void run() {
        Player player = oravPlayer.getPlayer();
        if(player == null){
            return;
        }

        long lastBackup = lastRun;
        long lastRemaining = remainingMillis;
        lastRun = System.currentTimeMillis();
        long timeSinceLastRun = lastBackup - lastRun;
        remainingMillis -= timeSinceLastRun;

        if(remainingMillis <= 5*60*1000 && lastRemaining > 5*60*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%5 Minuten Spielzeit!");
        }

        if(remainingMillis <= 60*1000 && lastRemaining > 60*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%eine Minute Spielzeit!");
        }

        if(remainingMillis <= 30*1000 && lastRemaining > 30*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%30 Sekunden Spielzeit!");
        }

        if(remainingMillis <= 10*1000 && lastRemaining > 10*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%10 Sekunden Spielzeit!");
        }

        if(remainingMillis <= 3*1000 && lastRemaining > 3*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%3 Sekunden Spielzeit!");
        }

        if(remainingMillis <= 2*1000 && lastRemaining > 2*1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%2 Sekunden Spielzeit!");
        }

        if(remainingMillis <= 1000 && lastRemaining > 1000){
            messageManager.sendMessage(player,"Du hast nur noch %hc%1 Sekunde Spielzeit!");
        }

        if(remainingMillis <= 0){
            onEnd.accept(oravPlayer);
        }

    }
}
