package com.thoxia.odin.skyblock.api.module;

import com.thoxia.odin.skyblock.api.SkyBlock;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.StandardCopyOption;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

@Getter @Setter
public abstract class Module {

    private String name, author, version;
    private File jarFile;
    private File dataFolder;
    private File configFile;
    private YamlConfiguration config;
    private Logger logger;

    public abstract void onEnable();

    public abstract void onDisable();

    public void onLoad() {}

    public JavaPlugin getPlugin() {
        return SkyBlock.getInstance();
    }

    public void registerListeners(Listener... listeners) {
        SkyBlock.getInstance().getModuleManager().registerListeners(this, listeners);
    }

    public YamlConfiguration getConfig() {
        if (config == null) {
            config = new YamlConfiguration();
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                throw new RuntimeException(e);
            }
        }

        return config;
    }

    public void saveDefaultConfig() {
        saveResource("config.yml", false);
        this.config = getConfig();
    }

    public void saveResource(String path, boolean replace) {
        saveResource(path, dataFolder, replace);
    }

    public void saveResource(String path, File destinationFolder, boolean replace) {
        path = path.replace('\\', '/');
        try (JarFile jar = new JarFile(jarFile)) {
            JarEntry entry = jar.getJarEntry(path);
            try (InputStream in = jar.getInputStream(entry)) {
                File outFile = new File(destinationFolder, path);
                outFile.getParentFile().mkdirs();
                if (!outFile.exists() || replace)
                    java.nio.file.Files.copy(in, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
