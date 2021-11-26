package de.logilutions.orav.teamchest;

import de.logilutions.orav.config.Config;
import de.logilutions.orav.team.OravTeam;
import org.bukkit.Location;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TeamChestManager extends Config {
    private final Map<Long, Location> teamChestLocation = new HashMap<>();

    public TeamChestManager(File file) {
        super(file);
        for(String key: getConfiguration().getKeys(false)){
            Long id = Long.parseLong(key);
            teamChestLocation.put(id,getConfiguration().getLocation(key));
        }

    }

    public Location getChestLocation(OravTeam team) {
        return teamChestLocation.get(team.getId());
    }


    public void removeChest(OravTeam team) {
        removeChest(team.getId());
    }

    public void removeChest(Long id) {
        if (id == null) {
            return;
        }
        teamChestLocation.remove(id);
        getConfiguration().set(id + "", null);
        save();
    }

    public void removeChest(Location location) {
        Long id = getTeamOfChest(location);
        removeChest(id);
    }

    public boolean setChest(OravTeam oravTeam, Location location) {
        if (getChestLocation(oravTeam) != null) {
            return false;
        }
        teamChestLocation.put(oravTeam.getId(), location);

        getConfiguration().set(oravTeam.getId() + "", location);
        save();
        return true;
    }

    public Long getTeamOfChest(Location location) {
        for (Map.Entry<Long, Location> locationEntry : teamChestLocation.entrySet()) {
            if (locationEntry.getValue().equals(location)) {
                return locationEntry.getKey();
            }
        }
        return null;
    }

}
