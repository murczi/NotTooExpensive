package me.shuji.notTooExpensive;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public final class Main extends JavaPlugin implements Listener {

	FileConfiguration config = getConfig();
	Logger console = getLogger();
	int repairCost = 0;

	@Override
	public void onEnable() {
		setConfig();

		if (config.getBoolean("enabled")) {
			onDisable();
		}

		getServer().getPluginManager().registerEvents(this, this);
	}

	@EventHandler
	public void onAnvilUse(PrepareAnvilEvent e)	{
		AnvilInventory anvil = e.getInventory();
		Player player = (Player) e.getView().getPlayer();
		ItemStack resultItem = e.getResult();
		anvil.setMaximumRepairCost(10000);
		if (player.getGameMode() == GameMode.SURVIVAL | player.getGameMode() == GameMode.ADVENTURE) {
			if (resultItem != null && resultItem.getType() != Material.AIR) {
				if (anvil.getRepairCost() >= 40) {
					int exp = player.getLevel();

					repairCost = anvil.getRepairCost();
					if (anvil.getRepairCost() <= exp) {
						anvil.setRepairCost(0);
					}
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		if (player.getGameMode() == GameMode.SURVIVAL | player.getGameMode() == GameMode.ADVENTURE) {
			if (e.getInventory().getType() == InventoryType.ANVIL) {
				Inventory clickedInventory = e.getClickedInventory();
				if (clickedInventory instanceof AnvilInventory) {
					ItemStack currentItem = e.getCurrentItem();

					if (e.getSlotType() == InventoryType.SlotType.RESULT && currentItem != null && currentItem.getType() != Material.AIR) {
						int playerLevel = player.getLevel();
						if (playerLevel >= repairCost && repairCost >= 40) {
							int resultLevel = playerLevel - repairCost;
							console.info("playerlevel: " + playerLevel + " repaircost: " + repairCost + "result: " + resultLevel);
							player.setLevel(resultLevel);
						} else {
							player.sendMessage("LvL needed: " + repairCost);
							e.setCancelled(true);
						}
					}
				}
			}
		}
	}

	private void setConfig() {
		config.addDefault("enabled", true);
		config.options().copyDefaults(true);
		saveConfig();
	}

}
