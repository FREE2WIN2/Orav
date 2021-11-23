package de.logilutions.orav.session;

import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.util.MessageManager;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionObserver {

    private final DatabaseHandler databaseHandler;
    private final JavaPlugin javaPlugin;
    private long sessionDuration; //millis
    private final Map<UUID, BukkitTask> bukkitTaskMap = new HashMap<>();
    private final MessageManager messageManager;

    public SessionObserver(DatabaseHandler databaseHandler, JavaPlugin javaPlugin, long sessionDuration, MessageManager messageManager) {
        this.databaseHandler = databaseHandler;
        this.javaPlugin = javaPlugin;
        this.sessionDuration = sessionDuration;
        this.messageManager = messageManager;
    }

    public void startSession(OravPlayer oravPlayer) {
        if (this.bukkitTaskMap.containsKey(oravPlayer.getUuid())) {
            return;
        }
        SessionRunnable sessionRunnable = new SessionRunnable(oravPlayer, sessionDuration, messageManager);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimer(javaPlugin, sessionRunnable, 0, 20);
        this.bukkitTaskMap.put(oravPlayer.getUuid(), bukkitTask);
    }

    public void endSession(OravPlayer oravPlayer) {
        if (!this.bukkitTaskMap.containsKey(oravPlayer.getUuid())) {
            return;
        }
        this.bukkitTaskMap.get(oravPlayer.getUuid()).cancel();
        this.bukkitTaskMap.remove(oravPlayer.getUuid());
    }

    public void cancel() {
        for (BukkitTask bukkitTask : bukkitTaskMap.values()) {
            bukkitTask.cancel();
        }
    }
}
