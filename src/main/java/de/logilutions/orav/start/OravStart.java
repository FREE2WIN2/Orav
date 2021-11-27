package de.logilutions.orav.start;

import de.logilutions.orav.Orav;
import de.logilutions.orav.config.Config;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.discord.DiscordUtil;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.util.MessageManager;
import de.logilutions.orav.util.Scheduler;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.Color;
import java.io.File;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final DiscordUtil discordUtil;
    private boolean oravStartMessageSend = true;

    public OravStart(File file, Orav orav, Location middleLocation, SpawnGenerator spawnGenerator, int radius, DatabaseHandler databaseHandler, JavaPlugin javaPlugin, MessageManager messageManager, OravPlayerManager oravPlayerManager, SessionObserver sessionObserver, DiscordUtil discordUtil) {
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
        this.discordUtil = discordUtil;
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
        player.getInventory().clear();
        player.teleport(getLocation(oravPlayer));
        LocalDateTime until = LocalDateTime.now().plus(orav.getProtectionTime() + 5, ChronoUnit.MINUTES);
        protectedUntil.put(player.getUniqueId(), until);

        if (countdown) {
            player.setGameMode(GameMode.ADVENTURE);
            Scheduler scheduler = new Scheduler();
            scheduler.setBukkitTask(Bukkit.getScheduler().runTaskTimerAsynchronously(
                    javaPlugin,
                    new StartCountDownRunnable(messageManager, player, (player1) -> {
                        Bukkit.getScheduler().runTask(javaPlugin, () -> {
                            startProtectionTime(player);
                            player.setGameMode(GameMode.SURVIVAL);
                            player.setHealth(20);
                            player.setFoodLevel(20);
                            player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                            player.getInventory().clear();
                            if (!oravStartMessageSend) {
                                oravStartMessageSend = true;
                                discordUtil.send(
                                        "Minecraft ORAV #5", "Das Projekt Minecraft Orav #5 hat soeben begonnen!", "https://logilutions.de/Minecraft/ORAV-5/images/start.png",
                                        Color.GREEN, null, null, null);
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
                            }
                        });
                        scheduler.cancel();
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
        oravStartMessageSend = false;
        World world = Bukkit.getWorlds().get(0);
        world.setDifficulty(Difficulty.NORMAL);
        world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setClearWeatherDuration(20*60*20);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "advancement revoke @a everything");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
        orav.setState(Orav.State.RUNNING);
        databaseHandler.updateOrav(orav);

        generateLocations();
        for (Player player : Bukkit.getOnlinePlayers()) {
            startOrav(player, true);
        }
    }

    private Location getLocation(OravPlayer oravPlayer) {
        return this.startLocation.get(oravPlayer.getUuid()).clone().add(0.5, 0, 0.5);
    }

    public void startProtectionTime(Player player) {
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
                millis = orav.getPlayTimeDayOne() * 60 * 1000;
            }
            protectedUntil.put(player.getUniqueId(), LocalDateTime.now().plus(millis, ChronoUnit.MILLIS));
        }
        if (millis == 0) {
            return;
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
