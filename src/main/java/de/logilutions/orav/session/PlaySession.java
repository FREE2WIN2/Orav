package de.logilutions.orav.session;

import de.logilutions.orav.player.OravPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PlaySession {

    private final Long id;
    private final OravPlayer player;
    private final LocalDateTime start;

    public PlaySession(OravPlayer oravPlayer) {
       this.id = null;
       this.player = oravPlayer;
       this.start = LocalDateTime.now();
    }
}
