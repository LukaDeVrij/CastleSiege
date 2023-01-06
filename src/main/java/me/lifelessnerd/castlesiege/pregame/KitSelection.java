package me.lifelessnerd.castlesiege.pregame;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
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
        //

    }

}
