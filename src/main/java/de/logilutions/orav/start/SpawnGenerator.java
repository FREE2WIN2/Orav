package de.logilutions.orav.start;

import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.team.OravTeam;
import de.logilutions.orav.util.MessageManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class SpawnGenerator {

    public Map<UUID, Location> generateLocations(List<OravPlayer> players, Location center, int radius) {
        Map<UUID, Location> locationMap = new HashMap<>();
        Map<OravTeam, List<OravPlayer>> teamMember = new HashMap<>();
        for (OravPlayer oravPlayer : players) {
            List<OravPlayer> oravPlayers = teamMember.getOrDefault(oravPlayer.getOravTeam(), new ArrayList<>());
            oravPlayers.add(oravPlayer);
            teamMember.put(oravPlayer.getOravTeam(), oravPlayers);
        }
        double angle = 360.0 / (double) teamMember.size();
        double currentAngle = 0;
        for (Map.Entry<OravTeam, List<OravPlayer>> entry : teamMember.entrySet()) {
            for (OravPlayer oravPlayer : entry.getValue()) {
                int x = (int) (0.5 + radius * Math.cos(currentAngle));
                int z = (int) (0.5 + radius * Math.sin(currentAngle));
                Location middleTeamSpawn = center.clone().add(x, 0, z);
                System.out.println(middleTeamSpawn);
                placeTeamSpawn(middleTeamSpawn);
                locationMap.put(oravPlayer.getUuid(), middleTeamSpawn);
                currentAngle += angle;
            }
        }

        return locationMap;
    }

    public void placeTeamSpawn(Location center) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        center.getWorld().getBlockAt(x, y, z - 1).setType(Material.OAK_SLAB);
        center.getWorld().getBlockAt(x, y, z + 1).setType(Material.OAK_SLAB);
        center.getWorld().getBlockAt(x + 1, y, z).setType(Material.OAK_SLAB);
        center.getWorld().getBlockAt(x - 1, y, z).setType(Material.OAK_SLAB);
        center.getWorld().getBlockAt(x, y - 1, z).setType(Material.AIR);
    }
}
