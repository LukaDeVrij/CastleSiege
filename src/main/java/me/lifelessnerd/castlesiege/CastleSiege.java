package me.lifelessnerd.castlesiege;

import me.lifelessnerd.castlesiege.files.KitItemsConfig;
import me.lifelessnerd.castlesiege.managers.CommandManager;
import me.lifelessnerd.castlesiege.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class CastleSiege extends JavaPlugin {

    private GameManager gameManager;

    @Override
    public void onEnable() {

        getLogger().log(Level.FINE, "Siege Castle plugin started up. - By LifelessNerd");
        this.gameManager = new GameManager(this);

        KitItemsConfig.setup();
        KitItemsConfig.get().addDefault("items", "");
        KitItemsConfig.get().options().copyDefaults(true);
        KitItemsConfig.save();

        //Setup config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //getServer().getPluginManager().registerEvents();
        getCommand("siege").setExecutor(new CommandManager(this, gameManager));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
