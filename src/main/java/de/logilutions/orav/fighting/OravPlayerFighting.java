package de.logilutions.orav.fighting;

import de.logilutions.orav.player.OravPlayer;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

@Getter
public class OravPlayerFighting {
    private final OravPlayer oravPlayer;
    private final OravPlayer otherPlayer;
    private LocalDateTime lastHitTime;

    public OravPlayerFighting(OravPlayer oravPlayer, OravPlayer otherPlayer) {
        this.oravPlayer = oravPlayer;
        this.otherPlayer = otherPlayer;
        lastHitTime = LocalDateTime.now();

        this.oravPlayer.setInFight(true);
        this.otherPlayer.setInFight(true);
    }

    public void addHit() {
        lastHitTime = LocalDateTime.now();
    }

    public boolean isOver() {
        return oravPlayer.isDroppedOut() || otherPlayer.isDroppedOut() ||LocalDateTime.now().get(ChronoField.MILLI_OF_DAY) - lastHitTime.get(ChronoField.MILLI_OF_DAY) > 5 * 60 * 1000;
    }

    public void endFighting() {
        this.oravPlayer.setInFight(false);
        this.otherPlayer.setInFight(false);
    }

    public boolean contains(OravPlayer oravPlayer1, OravPlayer oravPlayer2) {
        return (oravPlayer.equals(oravPlayer1) && otherPlayer.equals(oravPlayer2))
                || (otherPlayer.equals(oravPlayer1) && oravPlayer.equals(oravPlayer2));
    }

    public boolean contains(OravPlayer oravPlayer) {
        return this.oravPlayer.equals(oravPlayer) || otherPlayer.equals(oravPlayer);
    }

    public OravPlayer getOtherPart(OravPlayer oravPlayer){
        if(this.oravPlayer.equals(oravPlayer)){
            return this.otherPlayer;
        }
        if(this.otherPlayer.equals(oravPlayer)){
            return this.oravPlayer;
        }
        return null;
    }
}
