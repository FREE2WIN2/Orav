package de.logilutions.orav.player;

import de.logilutions.orav.Orav;
import de.logilutions.orav.database.DatabaseHandler;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class OravPlayerManager {

    private final Map<UUID, OravPlayer> playerMap = new HashMap<>();
    private final DatabaseHandler databaseHandler;
    private final Orav orav;

    public OravPlayer getPlayer(UUID uuid) {
        OravPlayer oravPlayer = playerMap.get(uuid);
        if (oravPlayer == null) {
            oravPlayer = databaseHandler.readOravPlayer(uuid, orav.getId());
            if(oravPlayer == null){
                return null;
            }
            if(orav.getState() == Orav.State.PROTECTION){
                oravPlayer.setFightProtected(true);
            }
            playerMap.put(uuid, oravPlayer);
        }
        return oravPlayer;

    }

    public void removePlayer(UUID uuid) {
        playerMap.remove(uuid);
    }

    public Collection<OravPlayer> getAll(){
        return playerMap.values();
    }
}
