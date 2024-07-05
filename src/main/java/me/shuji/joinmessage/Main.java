package me.shuji.joinmessage;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;


public final class Main extends JavaPlugin implements Listener {

  FileConfiguration config = getConfig();
  Logger console = getLogger();

  @Override
  public void onEnable() {
    setConfig();

    if (config.getBoolean("enabled")) {
      onDisable();
    }

    getServer().getPluginManager().registerEvents(this, this);
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    Player player = e.getPlayer();
    e.joinMessage(Component.text(this.trans(String.format(config.getString("joinMessage").replace("{player}", player.getName())))));
    player.sendMessage(this.trans(String.format(config.getString("messageToPlayer").replace("{player}", player.getName()))));
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent e) {
    Player player = e.getPlayer();
    e.quitMessage(Component.text(this.trans(String.format(config.getString("quitMessage").replace("{player}", player.getName())))));
    console.info(player.getName() + " logged off at " + " X: " + player.getLocation().x() + " Y: " + player.getLocation().y() + " Z: " + player.getLocation().z());
  }

  @SuppressWarnings("deprecation")
  private String trans(String arg0) {
    return ChatColor.translateAlternateColorCodes('&', arg0);
  }

  private void setConfig() {
    config.addDefault("enabled", true);
    config.addDefault("messageToPlayer", "Hello {player}");
    config.addDefault("joinMessage", "{player} joined. o/");
    config.addDefault("quitMessage", "{player} left. :c");
    config.options().copyDefaults(true);
    saveConfig();
  }

}
