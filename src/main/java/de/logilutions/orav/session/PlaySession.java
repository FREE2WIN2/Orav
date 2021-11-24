package de.logilutions.orav.session;

import de.logilutions.orav.player.OravPlayer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class PlaySession {

    @Setter
    private  Long id;
    private final OravPlayer player;
    private final LocalDateTime start;
    @Setter
    private LocalDateTime end;
    public PlaySession(OravPlayer oravPlayer) {
       this.id = null;
       this.player = oravPlayer;
       this.start = LocalDateTime.now();
    }

    public long getPlayedTime() {
        if(start == null || end == null){
            return 0;
        }
        return Duration.between(start,end).toMillis();
    }

}
