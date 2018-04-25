package XpLevels.Commands;

import XpLevels.GUI.GuiManager;
import XpLevels.XpLevelsMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LevelsCommand implements CommandExecutor {

    private XpLevelsMain plugin;
    public LevelsCommand(XpLevelsMain instance) {
        plugin = instance;
    }

    private GuiManager gm = new GuiManager();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (isLevelsCommand(command)) {
            if (canExecute(sender, command)) {
                Player player = (Player) sender;
                player.openInventory(gm.createinventory(player, plugin));
                return true;
            }
        }
        return true;

    }

    private boolean isLevelsCommand(Command command) {
        return command.getName().equalsIgnoreCase("levels");
    }

    private boolean canExecute(CommandSender sender, Command command) {

        if (!(hasPermission(sender, command))) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.no-permissions")));
            return false;
        }
        return isPlayer(sender);

    }

    private boolean hasPermission(CommandSender sender, Command command) {

        return isLevelsCommand(command) && sender.hasPermission("levels.use");

    }

    private boolean isPlayer(CommandSender sender) {
        return sender instanceof Player;
    }

}
