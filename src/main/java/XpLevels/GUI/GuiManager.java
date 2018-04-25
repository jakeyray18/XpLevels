package XpLevels.GUI;

import XpLevels.ConfigUtils.PerLevelConfigUtils;
import XpLevels.ConfigUtils.PerPlayerConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class GuiManager {

    private PerLevelConfigUtils plcu = new PerLevelConfigUtils();
    private PerPlayerConfigUtils cu = new PerPlayerConfigUtils();

    public Inventory createinventory(Player player, Plugin plugin) {
        int invSize = 45;
        Inventory inv = Bukkit.createInventory(null, invSize, ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GUI.Title")));

        inv = populateInv(inv, player, plugin);

        return inv;
    }

    private Inventory populateInv(Inventory inv, Player player, Plugin plugin) {

        int uAmount = getUnlockedAmount(player, plugin);
        //Slots 1-34
        for (int i = 0; i < 35; i++) {
            int iToPass = i + 1;
            inv.setItem(i, levelDisplayStack(iToPass, isUnlocked(i, getUnlockedAmount(player, plugin)), plugin));
        }

        //Slot 35
        inv.setItem(35, createFinalItem(isUnlocked(35, uAmount), plugin));


        //Slots 36-44
        for (int i = 36; i < 45; i++) {
            if (i == 40) {
                inv.setItem(i, eButton());
            } else {
                inv.setItem(i, blank());
            }
        }

        return inv;
    }

    private boolean isUnlocked(int pos, int unlockedAmount) {

        return (unlockedAmount > 0) && (unlockedAmount - 1 >= pos);

    }

    private int getUnlockedAmount(Player player, Plugin plugin) {
        return cu.getPlayerConfig(player, plugin).getInt("Unlocked-Levels");
    }

    private ItemStack eButton() {
        Wool wool = new Wool(DyeColor.RED);
        ItemStack eButton = wool.toItemStack();
        ItemMeta eMeta = eButton.getItemMeta();
        eMeta.setDisplayName(ChatColor.RED + "Exit");
        eButton.setItemMeta(eMeta);

        return eButton;
    }

    private ItemStack blank() {
        ItemStack item = new ItemStack(Material.STAINED_GLASS_PANE);
        ItemMeta mItem = item.getItemMeta();
        mItem.setDisplayName(" ");
        item.setItemMeta(mItem);

        return item;
    }

    private ItemStack createFinalItem(boolean isUnlocked, Plugin plugin) {
        ItemStack item = new ItemStack(Material.BEDROCK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plcu.getLevelConfig(36, plugin).getString("Name")));
        if (!(isUnlocked)) {
            item.setType(Material.getMaterial(plugin.getConfig().getString("GUI.Locked-Final")));
        } else {
            item.setType(Material.getMaterial(plugin.getConfig().getString("GUI.Unlocked-Final")));
        }
        item.setItemMeta(setLevelStackLore(isUnlocked, itemMeta, 36, plugin));

        return item;
    }

    private ItemStack levelDisplayStack(int level, boolean isUnlocked, Plugin plugin) {
        ItemStack item = new ItemStack(Material.BEDROCK);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', plcu.getLevelConfig(level, plugin).getString("Name")));
        if (!(isUnlocked)) {
            item.setType(Material.getMaterial(plugin.getConfig().getString("GUI.Locked-Level")));
            item.setItemMeta(setLevelStackLore(false, itemMeta, level, plugin));
        } else {
            item.setType(Material.getMaterial(plugin.getConfig().getString("GUI.Unlocked-Level")));
            item.setItemMeta(setLevelStackLore(true, itemMeta, level, plugin));
        }

        return item;
    }

    private ItemMeta setLevelStackLore(boolean isUnlocked, ItemMeta meta, int level, Plugin plugin) {
        List<String> lore = new ArrayList<String>();
        if (!(isUnlocked)) {
            lore.add(ChatColor.RED + "Locked");
            lore.add(ChatColor.GOLD + "XP: " + ChatColor.AQUA + plcu.getLevelConfig(level, plugin).getInt("Xp-Cost"));
            lore.addAll(addRewardLore(level, plugin));

        } else {
            lore.add(ChatColor.GREEN + "Unlocked!");
        }
        meta.setLore(lore);

        return meta;
    }

    private List<String> addRewardLore(int level, Plugin plugin) {
        List<String> lore = new ArrayList<String>();
        if (!(plcu.getLevelConfig(level, plugin).getBoolean("Custom-Lore.Enabled"))) {
            ConfigurationSection configSection = plcu.getLevelConfig(level, plugin).getConfigurationSection("Items");
            if (configSection != null && (!configSection.getKeys(false).isEmpty())) {
                lore.add(ChatColor.WHITE + "----------");
                lore.add(ChatColor.GOLD + "Items: ");
                for (String itemNumber : configSection.getKeys(false)) {
                    lore.add(ChatColor.translateAlternateColorCodes('&', configSection.getString(itemNumber + ".Lore-Name")));
                }
                lore.add(ChatColor.WHITE + "----------");
            }
        } else {

            for (String s : plcu.getLevelConfig(level, plugin).getStringList("Custom-Lore.Lore")) {
                lore.add(ChatColor.translateAlternateColorCodes('&', s));
            }
        }


        return lore;
    }

}
