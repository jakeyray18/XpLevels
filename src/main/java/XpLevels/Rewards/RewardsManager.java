package XpLevels.Rewards;

import XpLevels.ConfigUtils.PerLevelConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class RewardsManager {

    private PerLevelConfigUtils plcu = new PerLevelConfigUtils();

    public void doRewardCommands(int level, Player player, Plugin plugin) {
        List<String> commands = plcu.getLevelConfig(level, plugin).getStringList("Commands");
        for (String s: commands) {
            String command = s.replace("%P", player.getName());
            plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }
    }

    public void doRewardItems(int level, Player player, Plugin plugin) {
        ConfigurationSection configSection = plcu.getLevelConfig(level, plugin).getConfigurationSection("Items");
        if (configSection != null && (!configSection.getKeys(false).isEmpty())) {
            for (String itemNumber : configSection.getKeys(false)) {
                player.getInventory().addItem(createItem(configSection, Integer.parseInt(itemNumber)));
            }
        }
    }

    private ItemStack createItem(ConfigurationSection configSection, int itemNumber) {
        ItemStack item = new ItemStack(Material.BEDROCK);
        item.setType(Material.getMaterial(configSection.getString(itemNumber + ".Type")));
        item.setDurability((short) configSection.getInt(itemNumber + ".Data-Value"));
        item.setAmount(configSection.getInt(itemNumber + ".Amount"));
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', configSection.getString(itemNumber + ".Name")));
        item.setItemMeta(itemMeta);

        ConfigurationSection enchantSection = configSection.getConfigurationSection(itemNumber + ".Enchants");
        if (enchantSection != null && !(enchantSection.getKeys(false).isEmpty())) {
            for(String enchant : enchantSection.getKeys(false)) {
                int enchantLevel = enchantSection.getInt("." + enchant);
                item.addUnsafeEnchantment(Enchantment.getByName(enchant), enchantLevel);
            }
        }
        return item;
    }

}
