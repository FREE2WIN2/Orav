package de.logilutions.orav.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerLogoutsConfig {

    private final FileConfiguration configuration;
    private final File file;

    public PlayerLogoutsConfig(File file) {
        this.file = file;
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void saveLogOutPosition(UUID uuid, Location location) {
        configuration.set(uuid.toString(), location);
        save();
    }

    public Map<String, Location> readPositions() {
        Map<String, Location> map = new HashMap<>();
        for (String uuidString : configuration.getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Location location = configuration.getLocation(uuidString);
            map.put(name, location);
        }
        return map;
    }

    public void removePlayer(UUID uuid) {
        configuration.set(uuid.toString(), null);
        save();
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
