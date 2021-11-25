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
    private final Long id;
    private final String name;
    private final String shortName;
    private final TeamColor teamColor;
}
