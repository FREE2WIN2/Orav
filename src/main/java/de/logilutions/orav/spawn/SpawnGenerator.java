package de.logilutions.orav.spawn;

import de.logilutions.orav.util.MessageManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpawnGenerator implements CommandExecutor {

    private MessageManager messageManager;

    public SpawnGenerator(MessageManager messageManager) {
        this.messageManager = messageManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            return false;
        }
        Player player = (Player) commandSender;
        if (!player.isOp()) {
            this.messageManager.sendMessage(player, "%ec%Du bist nicht berechtigt, diesen Befehl auszuführen.");
            return false;
        }

        this.messageManager.sendMessage(player, "Spawnbereich wird generiert!");

        return false;
    }
}
