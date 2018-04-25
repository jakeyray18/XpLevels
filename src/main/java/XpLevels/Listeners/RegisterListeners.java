package XpLevels.Listeners;

import XpLevels.XpLevelsMain;
import org.bukkit.plugin.PluginManager;

public class RegisterListeners {

    XpLevelsMain plugin;
    public RegisterListeners(XpLevelsMain instance) {
        plugin = instance;
    }

    public void registerListeners() {
        PluginManager pm = plugin.getServer().getPluginManager();

        pm.registerEvents(new JoinCreatePlayerConfig(plugin), plugin);
        pm.registerEvents(new InventoryClickListener(plugin), plugin);
    }

}
