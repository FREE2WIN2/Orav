package de.logilutions.orav;

import de.logilutions.orav.database.DatabaseConnectionHolder;
import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.exception.DatabaseConfigException;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class OravPlugin extends JavaPlugin {

    private Orav orav;

    private DatabaseConnectionHolder databaseConnectionHolder;
    private DatabaseHandler databaseHandler;
    private OravPlayerManager oravPlayerManager;
    private SessionObserver sessionObserver;
    private MessageManager messageManager;
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

        initCommands();
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
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if(sessionObserver != null){
            sessionObserver.cancel();
        }
    }
}
