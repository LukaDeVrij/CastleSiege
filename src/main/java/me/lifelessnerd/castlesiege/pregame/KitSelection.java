package me.lifelessnerd.castlesiege.pregame;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

public class KitSelection implements Listener {

    Plugin plugin;

    public KitSelection(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void kitItemClick(PlayerInteractEvent e){

        Player player = e.getPlayer();

        if (!(player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("gameWorld")))) {
            return;
        }
        if (!(e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)) {
            return;
        }
        if (!(player.getInventory().getItemInMainHand().getType() == Material.BOW)) {
            return;
        }
        ItemMeta meta = player.getInventory().getItemInMainHand().getItemMeta();
        PersistentDataContainer data = meta.getPersistentDataContainer();
        if(!(data.has(new NamespacedKey(plugin, "Kit Selection Item")))){
            return;
        }
        //Open Kit Menu and stuff

        //Create inventory GUI
        TextComponent invTitle = Component.text("Kits Menu").color(TextColor.color(255, 150, 20));
        Inventory kitSelectionGUI = Bukkit.createInventory(null, 54, invTitle);

        //TODO: steal code from PureKitPVP




    }

}
