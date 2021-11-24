package de.logilutions.orav.spawn;

import de.logilutions.orav.util.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnCycleGenerator implements CommandExecutor {
    private MessageManager messageManager;
    private World world;

    public SpawnCycleGenerator(MessageManager messageManager) {
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


        Location center = player.getLocation().getBlock().getLocation();
        center.add(0.5, 0, 0.5);
        this.messageManager.sendMessage(player, "Kreis wird generiert bei [" + center.getX() + ", " + center.getY() + ", " + center.getZ() + "]");

        int radius = 0;
        try {
            radius = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            this.messageManager.sendMessage(player, "%ec%Radius war keine Zahl!");
            return true;
        }

        drawCycle(center, radius);
        this.messageManager.sendMessage(player, "Generierung erfolgreich!");

        return true;
    }

    public void drawCycle(Location center, int r) {
        int x;
        int y = center.getBlockY();
        int z;

        for (double i = 0.0; i < 360.0; i += 0.1) {
            double angle = i * Math.PI / 180;
            x = (int)(center.getBlockX() + 0.5 + r * Math.cos(angle));
            z = (int)(center.getBlockZ() + 0.5 + r * Math.sin(angle));

            this.world.getBlockAt(x, y, z).setType(Material.GOLD_BLOCK);
        }
    }
}
