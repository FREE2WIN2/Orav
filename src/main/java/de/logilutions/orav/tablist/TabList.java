package de.logilutions.orav.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class TabList {

    private Map<Integer, String> messages = new HashMap<>(); //TODO config
    private int currentMessage = 0;
    private String header = "\n§7Minecraft §5ORAV 5\n";

    public TabList() {
        messages.put(0, "\n          §7Sponsored by FREE2WIN          \n");
        messages.put(1, "\n    §7Spawn by MPP and WGW Builder Team    \n");
        messages.put(2, "\n  §7Idea and Organisation by Coernerbrot  \n");
        messages.put(3, "\n  §7Developed by Coernerbrot and FREE2WIN  \n");
    }

    public void start(JavaPlugin javaPlugin) {
        Bukkit.getScheduler().runTaskTimer(javaPlugin, () -> {
            currentMessage = (currentMessage + 1) % messages.size();
            for (Player player : Bukkit.getOnlinePlayers()) {
                send(player);
            }
        }, 0, 5 * 20); //TODO config intervcal
    }

    public void send(Player player) {
        String footer = messages.get(currentMessage);
        player.setPlayerListHeaderFooter(header, footer);
    }
}
