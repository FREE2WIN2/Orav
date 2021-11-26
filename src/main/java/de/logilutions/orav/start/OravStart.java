package de.logilutions.orav.start;

import de.logilutions.orav.Orav;
import de.logilutions.orav.config.Config;
import de.logilutions.orav.database.DatabaseHandler;
import org.bukkit.Location;

import java.io.File;
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

    public OravStart(File file, Orav orav, Location middleLocation, SpawnGenerator spawnGenerator, int radius, DatabaseHandler databaseHandler) {
        super(file);
        this.orav = orav;
        this.middleLocation = middleLocation;
        this.spawnGenerator = spawnGenerator;
        this.radius = radius;
        this.databaseHandler = databaseHandler;
        readLocations();
    }

    private void readLocations() {
        for (String key : getConfiguration().getKeys(false)) {
            UUID uuid = UUID.fromString(key);
            startLocation.put(uuid, getConfiguration().getLocation(key));
        }
    }


    public void generateLocations() {
        this.startLocation = spawnGenerator.generateLocations(databaseHandler.getAllPlayers(orav.getId()), middleLocation, radius);
        for (Map.Entry<UUID, Location> entry : startLocation.entrySet()) {
            getConfiguration().set(entry.getKey().toString(), entry.getValue());
        }
        save();
    }


}
