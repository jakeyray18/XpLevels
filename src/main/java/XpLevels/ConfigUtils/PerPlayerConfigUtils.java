package XpLevels.ConfigUtils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class PerPlayerConfigUtils {

    public void setPlayerConfig(Player player, Plugin plugin) {

        String filename = player.getUniqueId().toString();
        File playerConfig = new File(plugin.getDataFolder() + File.separator + "/users/" + filename + ".yml");

        if (!playerConfig.exists()) {
            YamlConfiguration yamlPlayerConfig = YamlConfiguration.loadConfiguration(playerConfig);
            yamlPlayerConfig.addDefault("Name", player.getName());
            yamlPlayerConfig.addDefault("Unlocked-Levels", 0);
            yamlPlayerConfig.options().copyDefaults(true);
            try {
                yamlPlayerConfig.save(playerConfig);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void reloadPlayerConfig(Player player, Plugin plugin) {

        String filename = player.getUniqueId().toString();

        File playerConfig = new File(plugin.getDataFolder() + File.separator + "/users/" + filename + ".yml");
        if (playerConfig.exists()) {
            YamlConfiguration yamlPlayerConfig = YamlConfiguration.loadConfiguration(playerConfig);
        }
    }

    public YamlConfiguration getPlayerConfig(Player player, Plugin plugin) {

        String filename = player.getUniqueId().toString();
        String name = player.getName();
        YamlConfiguration yamlPlayerConfig = null;

        File playerConfig = new File(plugin.getDataFolder() + File.separator + "/users/" + filename + ".yml");
        if (playerConfig.exists()) {
            yamlPlayerConfig = YamlConfiguration.loadConfiguration(playerConfig);
            if (!yamlPlayerConfig.get("Name").toString().equals(name)) {
                yamlPlayerConfig.set("Name", name);
                savePlayerConfig(player, yamlPlayerConfig, plugin);
            }
            reloadPlayerConfig(player, plugin);
        } else {
            setPlayerConfig(player, plugin);
        }
        return yamlPlayerConfig;
    }

    public void savePlayerConfig(Player player, YamlConfiguration config, Plugin plugin) {

        String filename = player.getUniqueId().toString();
        File playerConfig = new File(plugin.getDataFolder() + File.separator + "/users/" + filename + ".yml");

        try {
            config.save(playerConfig);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
