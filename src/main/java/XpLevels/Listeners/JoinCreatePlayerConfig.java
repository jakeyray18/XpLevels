package XpLevels.Listeners;

import XpLevels.ConfigUtils.PerPlayerConfigUtils;
import XpLevels.XpLevelsMain;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinCreatePlayerConfig implements Listener{

    XpLevelsMain plugin;
    public JoinCreatePlayerConfig(XpLevelsMain instance) {
        plugin = instance;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PerPlayerConfigUtils cu = new PerPlayerConfigUtils();
        cu.setPlayerConfig(player, plugin);
    }

}
