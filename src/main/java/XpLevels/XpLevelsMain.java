package XpLevels;

import XpLevels.Commands.RegisterCommands;
import XpLevels.ConfigUtils.PerLevelConfigUtils;
import XpLevels.Listeners.RegisterListeners;
import org.bukkit.plugin.java.JavaPlugin;

public class XpLevelsMain extends JavaPlugin{

    public void onEnable() {
        load();
    }

    public void onDisable() {

    }

    private void load() {

        //Register Commands
        RegisterCommands();

        //RegisterListeners
        RegisterListeners();

        //Save default Config
        saveDefaultConfig();

        //Create Per Level Configs
        createLevelConfigs();

    }

    private void RegisterCommands() {
        RegisterCommands re = new RegisterCommands(this);
        re.registerCommands();
    }

    private void RegisterListeners() {
        RegisterListeners rl = new RegisterListeners(this);
        rl.registerListeners();
    }

    private void createLevelConfigs() {
        PerLevelConfigUtils plcu = new PerLevelConfigUtils();
        for (int i = 1; i <= 36; i++ ) {
            plcu.setLevelConfig(i, this);
        }
        saveResource("LevelTemplate.yml", false);
    }

}
