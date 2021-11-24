package de.logilutions.orav.spawn;

import de.logilutions.orav.util.MessageManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnGenerator implements CommandExecutor {

    private MessageManager messageManager;
    private World world;

    public SpawnGenerator(MessageManager messageManager) {
        this.messageManager = messageManager;
        this.world = Bukkit.getWorlds().get(0);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            this.messageManager.sendMessage(player, "%ec%Du bist nicht berechtigt, diesen Befehl auszuführen.");
            return true;
        }

        this.messageManager.sendMessage(player, "Spawnbereich wird generiert...");

        Location center = player.getLocation();

        int team_amount = 0;
        try {
            team_amount = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            this.messageManager.sendMessage(player, "%ec%Teamanzahl war keine Zahl!");
            return true;
        }

        if (generate(center, team_amount)) {
            this.messageManager.sendMessage(player, "Generierung erfolgreich!");
        } else {
            this.messageManager.sendMessage(player, "%ec%Generierung fehlgeschlagen! Teamzahl nicht implementiert.");
        }

        return true;
    }

    public boolean generate(Location center, int team_amount) {
        if (team_amount == 4) {
            center.add(7, 0, 0);
            placeTeamSpawn(center, 1);
            center.add(-14, 0, 0);
            placeTeamSpawn(center, 1);
            center.add(7, 0, 7);
            placeTeamSpawn(center, 0);
            center.add(0, 0, -14);
            placeTeamSpawn(center, 0);

            return true;
        } else {
            return false;
        }
    }

    public void placeTeamSpawn(Location center, int rotation) {
        int x = center.getBlockX();
        int y = center.getBlockY();
        int z = center.getBlockZ();

        if (rotation == 0) {
            this.world.getBlockAt(x-1, y, z).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x-2, y, z+1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x-2, y, z-1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x-3, y, z).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+1, y, z).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+2, y, z+1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+2, y, z-1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+3, y, z).setType(Material.OAK_SLAB);

            this.world.getBlockAt(x+2, y-1, z).setType(Material.AIR);
            this.world.getBlockAt(x-2, y-1, z).setType(Material.AIR);
        }
        if (rotation == 1) {
            this.world.getBlockAt(x, y, z-1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x-1, y, z-2).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+1, y, z-2).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x, y, z-3).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x, y, z+1).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x-1, y, z+2).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x+1, y, z+2).setType(Material.OAK_SLAB);
            this.world.getBlockAt(x, y, z+3).setType(Material.OAK_SLAB);

            this.world.getBlockAt(x, y-1, z+2).setType(Material.AIR);
            this.world.getBlockAt(x, y-1, z-2).setType(Material.AIR);
        }
    }
}
