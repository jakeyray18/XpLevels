package XpLevels.Commands;

import XpLevels.XpLevelsMain;

public class RegisterCommands {

    private XpLevelsMain plugin;
    public RegisterCommands(XpLevelsMain instance) {
        plugin = instance;
    }

    public void registerCommands() {
        plugin.getCommand("levels").setExecutor(new LevelsCommand(plugin));
    }

}
