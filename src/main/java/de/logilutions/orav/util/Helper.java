package de.logilutions.orav.util;

import de.logilutions.orav.database.DatabaseHandler;
import de.logilutions.orav.player.OravPlayer;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class Helper {
    private final DatabaseHandler databaseHandler;

    public boolean teamIsDroppedOut(OravPlayer oravPlayer) {
        if (oravPlayer.isDroppedOut()) {
            OravPlayer teamMate = databaseHandler.getTeamMate(oravPlayer);
            return teamMate == null || teamMate.isDroppedOut();
        }
        return false;
    }

    public boolean handleSpawn(OravPlayer oravPlayer){
        Player player = oravPlayer.getPlayer();
        if(player == null){
            return false;
        }
        if(!player.isOp() && oravPlayer.isDroppedOut()){
            if (teamIsDroppedOut(oravPlayer)){
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                player.kickPlayer("Du bist ausgeschieden! Du darfst erst spectaten, wenn dein Teammate getötet wurde!");
            }
            return false;
        }
        return true;
    }
}
