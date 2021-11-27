package de.logilutions.orav.player;

import de.logilutions.orav.fighting.OravPlayerFighting;
import de.logilutions.orav.session.PlaySession;
import de.logilutions.orav.team.OravTeam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
public class OravPlayer {
    private final Long id;
    private final OravTeam oravTeam;
    private final UUID uuid;

    private PlaySession currentSession;
    private boolean droppedOut;
    private boolean hasValidSession;
    private boolean inFight;
    private boolean fightProtected = false;
    private boolean joined = true;
    private boolean oravAdmin = false;
    public OravPlayer(Long id, OravTeam oravTeam, UUID uuid, boolean droppedOut) {
        this.id = id;
        this.oravTeam = oravTeam;
        this.uuid = uuid;
        this.droppedOut = droppedOut;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OravPlayer that = (OravPlayer) o;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
