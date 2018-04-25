package XpLevels.ConfigUtils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.*;

public class PerLevelConfigUtils {

    public void setLevelConfig(int levelNumber, Plugin plugin) {

        String filename = "Level-" + levelNumber;
        File levelConfig = new File(plugin.getDataFolder() + File.separator + "/levels/" + filename + ".yml");

        if (!levelConfig.exists()) {
            Reader defConfigStream = new InputStreamReader(plugin.getResource("levelDefaultsInternal.yml"));
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            defConfig.set("Name", "&bLevel " + levelNumber);
            defConfig.set("Xp-Cost", levelNumber * 10000);
            YamlConfiguration yamlLevelConfig = YamlConfiguration.loadConfiguration(levelConfig);
            yamlLevelConfig.setDefaults(defConfig);

            yamlLevelConfig.options().copyDefaults(true);
            try {
                yamlLevelConfig.save(levelConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void reloadLevelConfig(int levelNumber, Plugin plugin) {

        String filename = "Level-" + levelNumber;

        File levelConfig = new File(plugin.getDataFolder() + File.separator + "/levels/" + filename + ".yml");
        if (levelConfig.exists()) {
            YamlConfiguration yamlLevelConfig = YamlConfiguration.loadConfiguration(levelConfig);
        }
    }

    public YamlConfiguration getLevelConfig(int levelNumber, Plugin plugin) {

        String filename = "Level-" + levelNumber;
        YamlConfiguration yamlLevelConfig = null;

        File levelConfig = new File(plugin.getDataFolder() + File.separator + "/levels/" + filename + ".yml");
        if (levelConfig.exists()) {
            yamlLevelConfig = YamlConfiguration.loadConfiguration(levelConfig);
            reloadLevelConfig(levelNumber, plugin);
        } else {
            setLevelConfig(levelNumber, plugin);
        }
        return yamlLevelConfig;
    }

    public void saveLevelConfig(int levelNumber, YamlConfiguration config, Plugin plugin) {

        String filename = "Level-" + levelNumber;
        File levelConfig = new File(plugin.getDataFolder() + File.separator + "/levels/" + filename + ".yml");

        try {
            config.save(levelConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
