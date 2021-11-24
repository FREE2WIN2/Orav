package de.logilutions.orav.util;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.scheduler.BukkitTask;

@Setter
public class Scheduler {
    private BukkitTask bukkitTask;

    public void cancel(){
        if(bukkitTask != null){
            bukkitTask.cancel();
        }
    }

}
