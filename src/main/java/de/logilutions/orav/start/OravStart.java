package de.logilutions.orav.start;

import de.logilutions.orav.Orav;
import de.logilutions.orav.config.Config;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.util.MessageManager;
import de.logilutions.orav.util.Scheduler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OravStart extends Config {
    private final Orav orav;
    private final Location middleLocation;
    private final SpawnGenerator spawnGenerator;
    private final int radius;
    private final DatabaseHandler databaseHandler;
    private Map<UUID, Location> startLocation = new HashMap<>();
    private final JavaPlugin javaPlugin;
    private final MessageManager messageManager;
    private final OravPlayerManager oravPlayerManager;

    private final Map<UUID, LocalDateTime> protectedUntil = new HashMap<>();
    private final SessionObserver sessionObserver;

    public OravStart(File file, Orav orav, Location middleLocation, SpawnGenerator spawnGenerator, int radius, DatabaseHandler databaseHandler, JavaPlugin javaPlugin, MessageManager messageManager, OravPlayerManager oravPlayerManager, SessionObserver sessionObserver) {
        super(file);
        this.orav = orav;
        this.middleLocation = middleLocation;
        this.spawnGenerator = spawnGenerator;
        this.radius = radius;
        this.databaseHandler = databaseHandler;
        this.javaPlugin = javaPlugin;
        this.messageManager = messageManager;
        this.oravPlayerManager = oravPlayerManager;
        this.sessionObserver = sessionObserver;
        readLocations();
    }

    private void readLocations() {
        for (String key : getConfiguration().getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            startLocation.put(uuid, getConfiguration().getLocation(key));
        }
    }

    public void startOrav(Player player, boolean countdown) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        player.teleport(getLocation(oravPlayer));
        player.setGameMode(GameMode.ADVENTURE);
        if (countdown) {
            Scheduler scheduler = new Scheduler();
            scheduler.setBukkitTask(Bukkit.getScheduler().runTaskTimerAsynchronously(
                    javaPlugin,
                    new StartCountDownRunnable(messageManager, player, (player1) -> {
                        scheduler.cancel();
                        startProtectionTime(player);
                        Bukkit.getScheduler().runTask(javaPlugin, () -> {
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setHealth(20);
                            player.setFoodLevel(20);
                            player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                            player.getInventory().clear();
                            Bukkit.getWorlds().get(0).setDifficulty(Difficulty.NORMAL);
                        });
                    }),
                    0, 20));
        } else {
            startProtectionTime(player);
        }
    }

    public void startOrav() {
        if (orav.getState() == Orav.State.RUNNING || orav.getState() == Orav.State.OVER) {
            return;
        }
        orav.setState(Orav.State.RUNNING);
        databaseHandler.updateOrav(orav);

        generateLocations();
        LocalDateTime until = LocalDateTime.now().plus(orav.getProtectionTime(), ChronoUnit.MINUTES);
        for (UUID uuid : this.startLocation.keySet()) {
            protectedUntil.put(uuid, until);
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            startOrav(player, true);
        }
    }

    private Location getLocation(OravPlayer oravPlayer) {
        return this.startLocation.get(oravPlayer.getUuid()).clone().add(0.5, 0, 0.5);
    }

    private void startProtectionTime(Player player) {
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if (oravPlayer == null) {
            return;
        }
        sessionObserver.startSession(oravPlayer);
        long millis = 0;
        if (protectedUntil.containsKey(player.getUniqueId())) {
            Duration remainig = Duration.between(LocalDateTime.now(), protectedUntil.get(player.getUniqueId()));
            if (remainig.isNegative()) {
                return;
            }
            millis = remainig.toMillis();
        } else {
            if (databaseHandler.getSessions(oravPlayer).size() == 1 && !orav.getStartDate().toLocalDate().isEqual(LocalDate.now())) {
                millis = orav.getProtectionTimeAfterDayOne() * 60 * 1000;
            } else if (databaseHandler.getSessions(oravPlayer).size() == 1 && orav.getStartDate().toLocalDate().isEqual(LocalDate.now())) {
                millis = orav.getProtectionTimeAfterDayOne() * 60 * 1000;
            }
        }

        Scheduler scheduler = new Scheduler();
        scheduler.setBukkitTask(Bukkit.getScheduler().runTaskTimerAsynchronously(javaPlugin, new ProtectionTimeRunnable(
                millis,
                messageManager,
                oravPlayer,
                oravPlayer1 -> oravPlayer1.setFightProtected(false)
        ), 0, 20));
    }

    public void generateLocations() {
        clear();
        this.startLocation = spawnGenerator.generateLocations(databaseHandler.getAllPlayers(orav.getId()), middleLocation, radius);
        for (Map.Entry<UUID, Location> entry : startLocation.entrySet()) {
            getConfiguration().set(entry.getKey().toString(), entry.getValue());
        }
        save();
    }


}
