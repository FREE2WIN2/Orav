package de.logilutions.orav.session;

import de.logilutions.orav.Orav;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.util.MessageManager;
import de.logilutions.orav.util.Scheduler;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
public class SessionObserver {

    private final DatabaseHandler databaseHandler;
    private final JavaPlugin javaPlugin;
    private final Map<UUID, BukkitTask> bukkitTaskMap = new HashMap<>();
    private final MessageManager messageManager;
    private final OravPlayerManager oravPlayerManager;
    private final Orav orav; //millis

    public void startSession(OravPlayer oravPlayer) {
        if (this.bukkitTaskMap.containsKey(oravPlayer.getUuid())) {
            return;
        }
        long remainingSessionTime;
        if (orav.getStartDate().toLocalDate().isEqual(LocalDate.now())) {
            remainingSessionTime = orav.getPlayTimeDayOne();
        } else {
            remainingSessionTime = orav.getPlayTime();
        }
        Collection<PlaySession> playSessions = databaseHandler.getSessionsFromToday(oravPlayer);
        for (PlaySession playSession : playSessions) {
            remainingSessionTime -= playSession.getPlayedTime();
        }
        if (remainingSessionTime <= 0) {
            endSession(oravPlayer);
            return;
        }
        oravPlayer.setCurrentSession(new PlaySession(oravPlayer));
        oravPlayer.setHasValidSession(true);
        this.databaseHandler.startSession(oravPlayer);
        SessionRunnable sessionRunnable = new SessionRunnable(oravPlayer, remainingSessionTime, messageManager, this::endSession);
        BukkitTask bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, sessionRunnable, 0, 20);
        this.bukkitTaskMap.put(oravPlayer.getUuid(), bukkitTask);
    }

    public void endSession(OravPlayer oravPlayer) {
        synchronized (bukkitTaskMap) {
            if (this.bukkitTaskMap.containsKey(oravPlayer.getUuid())) {
                this.bukkitTaskMap.get(oravPlayer.getUuid()).cancel();
                this.bukkitTaskMap.remove(oravPlayer.getUuid());
            }
        }
        if (oravPlayer.getCurrentSession() != null) {
            databaseHandler.stopSession(oravPlayer);
        }
        oravPlayer.setHasValidSession(false);
        Scheduler scheduler = new Scheduler();
        scheduler.setBukkitTask(Bukkit.getScheduler().runTaskTimer(
                javaPlugin,
                new SessionOverRunnable(oravPlayer, p -> scheduler.cancel()),
                0,
                40));
    }

    public void cancel() {
        for (Map.Entry<UUID, BukkitTask> entry : new HashSet<>(bukkitTaskMap.entrySet())) {
            OravPlayer oravPlayer = oravPlayerManager.getPlayer(entry.getKey());
            endSession(oravPlayer);
        }
    }
}
