package de.logilutions.orav.command;

import de.logilutions.orav.Orav;
import de.logilutions.orav.OravPlugin;
import de.logilutions.orav.player.OravPlayer;
import de.logilutions.orav.player.OravPlayerManager;
import de.logilutions.orav.session.SessionObserver;
import de.logilutions.orav.start.OravStart;
import de.logilutions.orav.util.MessageManager;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class OravCommand implements CommandExecutor, TabCompleter {
    private final MessageManager messageManager;
    private final OravPlayerManager oravPlayerManager;
    private final SessionObserver sessionObserver;
    private final OravStart oravStart;
    private final Orav orav;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(!(commandSender instanceof Player) || !commandSender.isOp()){
            return true;
        }
        Player player = (Player) commandSender;
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if(oravPlayer == null){
            messageManager.sendMessage(player,"Du bist kein Orav Player!");
            return true;
        }
        if(args.length == 1){
            switch (args[0].toLowerCase()){
                case "admin":
                    oravPlayerManager.getPlayer(player.getUniqueId()).setHasValidSession(true);
                    player.setGameMode(GameMode.CREATIVE);
                    messageManager.sendMessage(player,"Du bist jetzt im Admin Mode!");
                    break;
                case "invalidatesession":
                    sessionObserver.endSession(oravPlayerManager.getPlayer(player.getUniqueId()));
                    messageManager.sendMessage(player,"Du has deine Session beendet!");
                    break;
                case "start":
                    if(orav.getState() != Orav.State.PREPARATION){
                        messageManager.sendMessage(player,"%ec%Orav wurde bereits gestartet!");
                        return true;
                    }
                    oravStart.startOrav();
                    messageManager.sendMessage(player,"Orav wurde gestartet!");
                    break;
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> list = new ArrayList<>();
        if(!(commandSender instanceof Player) || !commandSender.isOp()){
            return list;
        }
        Player player = (Player) commandSender;
        OravPlayer oravPlayer = oravPlayerManager.getPlayer(player.getUniqueId());
        if(oravPlayer == null){
            messageManager.sendMessage(player,"Du bist kein Orav Player!");
            return list;
        }
        if(strings.length == 1){
            list.add("admin");
            list.add("invalidateSession");
            list.add("start");
        }

        return list;
    }
}
