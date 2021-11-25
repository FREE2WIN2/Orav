package de.logilutions.orav.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PlayerLogoutsConfig extends Config{

    public PlayerLogoutsConfig(File file) {
       super(file);
    }

    public void saveLogOutPosition(UUID uuid, Location location) {
        getConfiguration().set(uuid.toString(), location);
        save();
    }

    public Map<String, Location> readPositions() {
        Map<String, Location> map = new HashMap<>();
        for (String uuidString : getConfiguration().getKeys(false)) {
            UUID uuid = UUID.fromString(uuidString);
            String name = Bukkit.getOfflinePlayer(uuid).getName();
            Location location = getConfiguration().getLocation(uuidString);
            map.put(name, location);
        }
        return map;
    }

    public void removePlayer(UUID uuid) {
        getConfiguration().set(uuid.toString(), null);
        save();
    }

}
