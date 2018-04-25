package XpLevels.Listeners;

import XpLevels.ConfigUtils.PerLevelConfigUtils;
import XpLevels.ConfigUtils.PerPlayerConfigUtils;
import XpLevels.ExperienceManager.ExperienceManager;
import XpLevels.Rewards.RewardsManager;
import XpLevels.XpLevelsMain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.Plugin;

import java.util.logging.Level;

public class InventoryClickListener implements Listener {

    XpLevelsMain plugin;
    public InventoryClickListener(XpLevelsMain instance) {
        plugin = instance;
    }

    private PerLevelConfigUtils plcu = new PerLevelConfigUtils();
    private PerPlayerConfigUtils configUtils = new PerPlayerConfigUtils();
    private RewardsManager rewardsManager = new RewardsManager();

    @EventHandler
    public void onGuiClickEvent(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getInventory().getName().equals(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI.Title")))) {
            Player player = (Player) event.getWhoClicked();
            ItemStack item;
            if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                item = event.getCurrentItem();
            } else {
                return;
            }
            event.setCancelled(true);
            if (isCloseButton(item)) {
                player.closeInventory();
                return;
            }
            if (isBlankGlass(item)) {
                return;
            }
            if (item != null) {
                if (!(isAlreadyUnlocked(event.getSlot(), player))) {
                    if (isUnlockable(event.getSlot(), player, plugin)) {
                        if (canAfford(player, event.getSlot() + 1, plugin)) {
                            takeCost(player, event.getSlot() + 1, plugin);
                            incrementUnlocked(player, plugin);
                            player.closeInventory();
                            String message = ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("Messages.level-up").replace("%L", String.valueOf(event.getSlot() + 1)));
                            player.sendMessage(message);
                            rewardsManager.doRewardCommands(event.getSlot() + 1, player, plugin);
                            rewardsManager.doRewardItems(event.getSlot() + 1, player, plugin);
                        }
                    }
                }
            }
        }
    }

    private boolean isCloseButton(ItemStack item) {
        return getItemInfoString(item).equalsIgnoreCase("Exit,Wool,14");
    }

    private boolean isBlankGlass(ItemStack item) {
        return getItemInfoString(item).equalsIgnoreCase(" ,stained_glass_pane,0");
    }

    private String getItemInfoString(ItemStack item) {
        Material material = item.getType();
        String name = ChatColor.stripColor(item.getItemMeta().getDisplayName());
        MaterialData itemData = item.getData();
        String[] splitData = itemData.toString().split(".+\\(");
        int data = Integer.parseInt(splitData[1].replace(")", ""));

        return name + "," + material + "," + data;
    }

    private boolean isAlreadyUnlocked(int pos, Player player) {
        int unlockedAmount = getUnlockedAmount(player, plugin);
        return (unlockedAmount > 0) && (unlockedAmount - 1 >= pos);

    }

    private int getUnlockedAmount(Player player, Plugin plugin) {
        int i = configUtils.getPlayerConfig(player, plugin).getInt("Unlocked-Levels");
        return i;
    }

    private boolean isUnlockable(int slot, Player player, Plugin plugin) {
        return slot == getUnlockedAmount(player, plugin);
    }

    private int getLevelCost(int level, Plugin plugin) {
        return plcu.getLevelConfig(level, plugin).getInt("Xp-Cost");
    }

    private boolean canAfford(Player player, int level, Plugin plugin) {
        int cost = getLevelCost(level, plugin);
        ExperienceManager experienceManager = new ExperienceManager(player);
        return experienceManager.getCurrentExp() >= cost;
    }

    private void takeCost(Player player, int level, Plugin plugin) {

        ExperienceManager experienceManager = new ExperienceManager(player);
        experienceManager.changeExp(-getLevelCost(level, plugin));

    }

    private void incrementUnlocked(Player player, Plugin plugin) {
        YamlConfiguration playerConfig = configUtils.getPlayerConfig(player, plugin);
        int currentLevel = playerConfig.getInt("Unlocked-Levels");
        int incLevel = currentLevel + 1;
        playerConfig.set("Unlocked-Levels", incLevel);
        configUtils.savePlayerConfig(player, playerConfig, plugin);
    }

}
