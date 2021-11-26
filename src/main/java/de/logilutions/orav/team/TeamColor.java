package de.logilutions.orav.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.ChatColor;


@AllArgsConstructor
@Getter
public class TeamColor {
   private final Long id;
   private final String hex;
   private final String name;

    public ChatColor getChatColor() {
        return ChatColor.of("#"+hex);
    }
}
