package de.logilutions.orav;

import de.logilutions.orav.command.LeakCoords;
import de.logilutions.orav.database.DatabaseConnectionHolder;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.discord.DiscordUtil;
import de.logilutions.orav.discord.DiscordWebhook;
import de.logilutions.orav.exception.DatabaseConfigException;
import de.logilutions.orav.listener.PlayerDeathListener;
import de.logilutions.orav.listener.PlayerJoinQuitListener;
import de.logilutions.orav.listener.TimsListener;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.spawn.SpawnCycleGenerator;
import de.logilutions.orav.spawn.SpawnGenerator;
import de.logilutions.orav.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;
import java.util.logging.Level;

public class OravPlugin extends JavaPlugin {

    private Orav orav;

    private DatabaseConnectionHolder databaseConnectionHolder;
    private DatabaseHandler databaseHandler;
    private OravPlayerManager oravPlayerManager;
    private SessionObserver sessionObserver;
    private MessageManager messageManager;
    private DiscordUtil discordUtil;

    @Override
    public void onEnable() {
        super.onEnable();
        FileConfiguration config = getConfig();
        try {
            initDatabase(config);
        } catch (DatabaseConfigException e) {
            getLogger().log(Level.WARNING,"Error while enabling ORAV, Disabling itself!");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.messageManager = new MessageManager();
        this.databaseHandler = new DatabaseHandler(databaseConnectionHolder);
        this.orav = databaseHandler.readOrav(config.getInt("current-orav"));
        this.oravPlayerManager = new OravPlayerManager(databaseHandler,orav);
        this.discordUtil = new DiscordUtil("https://discord.com/api/webhooks/912863008508760095/PYhV2onPsh-geKovWFeOSIWUt7_kh8rO27gTV796jtOIFHNyQz6kXEpxZPRxC2-dKDUh");

        Bukkit.getPluginManager().registerEvents(new TimsListener(messageManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinQuitListener(discordUtil), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(discordUtil), this);

        initCommands();


//        DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/912863008508760095/PYhV2onPsh-geKovWFeOSIWUt7_kh8rO27gTV796jtOIFHNyQz6kXEpxZPRxC2-dKDUh");
//        webhook.setContent("----------------------------\nDer Server wurde gestartet!");
//        try {
//            webhook.execute();
//            System.out.println("executed dc send");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }


    private void initDatabase(FileConfiguration config) throws DatabaseConfigException {
        ConfigurationSection databaseSection = config.getConfigurationSection("database");
        if (databaseSection == null) {
            getLogger().warning("Database not configurated! Please fill out the config.yml!");
        } else {
            databaseConnectionHolder = new DatabaseConnectionHolder(databaseSection);
        }
    }
    private void initCommands() {
        getCommand("generatespawn").setExecutor(new SpawnGenerator(messageManager));
        getCommand("generatespawncycle").setExecutor(new SpawnCycleGenerator(messageManager));
        getCommand("leakcoords").setExecutor(new LeakCoords(messageManager, discordUtil));
    }

    @Override
    public void onDisable() {

        this.discordUtil.send("\":octagonal_sign: Der Server wurde gestoppt!\"", null, null, Color.CYAN, null, null, null);

        super.onDisable();
        if(sessionObserver != null){
            sessionObserver.cancel();
        }
    }
}
