package de.logilutions.orav.player;

import de.logilutions.orav.session.PlaySession;
import de.logilutions.orav.team.OravTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class OravPlayer {
    private final Long id;
    private final OravTeam oravTeam;
    private final UUID uuid;
    @Setter
    private PlaySession currentSession;
    @Setter
    private boolean droppedOut;
    @Setter
    private boolean hasValidSession;
    @Setter
    private boolean inFight;
    public OravPlayer(Long id, OravTeam oravTeam, UUID uuid, boolean droppedOut) {
        this.id = id;
        this.oravTeam = oravTeam;
        this.uuid = uuid;
        this.droppedOut = droppedOut;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
}
