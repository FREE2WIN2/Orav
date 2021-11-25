package de.logilutions.orav.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class Config {
    private final FileConfiguration configuration;
    private final File file;

    public Config(File file) {
        this.file = file;
        if (!this.file.exists()) {
            this.file.getParentFile().mkdirs();
            try {
                this.file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration = new YamlConfiguration();
        try {
            configuration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            getConfiguration().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
