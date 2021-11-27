package de.logilutions.orav.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

@AllArgsConstructor
@Getter
public class OravTeam {
    private final Long id;
    private final String name;
    private final String shortName;
    private final TeamColor teamColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OravTeam oravTeam = (OravTeam) o;
        return Objects.equals(id, oravTeam.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
