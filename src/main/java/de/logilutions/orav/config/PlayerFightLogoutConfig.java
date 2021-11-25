package de.logilutions.orav.config;

import de.logilutions.orav.fighting.OravPlayerFighting;
import de.logilutions.orav.player.OravPlayer;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PlayerFightLogoutConfig extends Config {

    public PlayerFightLogoutConfig(File file) {
        super(file);
    }

    public void addLogoutInFight(OravPlayer oravPlayer, Collection<OravPlayerFighting> oravPlayerFightings) {
        FileConfiguration fileConfiguration = getConfiguration();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH:mm");
        String date = LocalDateTime.now().format(dateTimeFormatter);
        String uuid = oravPlayer.getUuid().toString();
        List<String> stringArrayList = new ArrayList<>();
        for (OravPlayerFighting oravPlayerFighting : oravPlayerFightings) {
            String stringBuilder = oravPlayerFighting.getLastHitTime().format(dateTimeFormatter) +
                    " against " + oravPlayerFighting.getOtherPart(oravPlayer).getUuid();
            stringArrayList.add(stringBuilder);
        }
        fileConfiguration.set(uuid + "." + date, stringArrayList);
        save();
    }
}
