package de.logilutions.orav.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@AllArgsConstructor
@Getter
public class OravTeam {
    private Long id;
    private String name;
    private TeamColor teamColor;

    public OravTeam(Long id, String name, TeamColor teamColor) {
        this.id = id;
        this.name = name;
        this.teamColor = teamColor;
    }
}
