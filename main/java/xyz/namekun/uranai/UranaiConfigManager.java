package xyz.namekun.uranai;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class UranaiConfigManager {

    private Uranai plugin = Uranai.getPlugin(Uranai.class);

    //config.ymlのメソッド
    public File configFile;
    public static FileConfiguration config;

    //players.ymlのメソッド
    public File playerFile;
    public static FileConfiguration playerConf;

    public void createFiles() {
        configFile = new File(plugin.getDataFolder(), "config.yml");
        playerFile = new File(plugin.getDataFolder(), "players.yml");

        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("config.yml", false);
        }
        config = new YamlConfiguration();

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if (!playerFile.exists()) {
            playerFile.getParentFile().mkdirs();
            plugin.saveResource("players.yml", false);
        }
        playerConf = new YamlConfiguration();

        try {
            playerConf.load(playerFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
