package me.lifelessnerd.castlesiege.pregame;

import me.lifelessnerd.castlesiege.files.KitItemsConfig;
import me.lifelessnerd.castlesiege.utils.MyStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

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
        for (String key : KitItemsConfig.get().getConfigurationSection("kits").getKeys(false)) {

            ItemStack itemStack = new ItemStack(Material.BARRIER);
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

            //NPE GALORE, TODO: idiot proof if these keys do not exist, instead of crashing entire plugin with an NPE
            //TODO: Actually i have no idea what this is referring to, I get no NPE's in the GUI ever
            //Set block

            itemStack.setType(Material.getMaterial(KitItemsConfig.get().getString("kits." + key + ".guiitem")));

            //Set lore

            String loreText = KitItemsConfig.get().getString("kits." + key + ".guilore");
            loreText = ChatColor.translateAlternateColorCodes('&', loreText);
            ArrayList<String> lore = new ArrayList<>();
            lore.add(loreText);

            // Item lore that consists of contents of kit
            FileConfiguration fileConfiguration = KitItemsConfig.get();
            List<ItemStack> kitContent = (List<ItemStack>) fileConfiguration.get("kits." + key + ".contents");
            lore.add(ChatColor.BLUE + "Weapons:");
            for (int index = 0; index < kitContent.size(); index++) {

                ItemStack item = kitContent.get(index);

                switch (index) {
                    case 3 -> lore.add(ChatColor.BLUE + "Items:");
                    case 36 -> {
                        if(kitContent.get(36) != null | kitContent.get(37) != null | kitContent.get(38) != null | kitContent.get(39) != null) {
                            lore.add(ChatColor.BLUE + "Armor:");
                        } else {
                            lore.add(ChatColor.BLUE + "No Armor");
                        }
                    }
                    case 40 -> {
                        if (item != null){
                            lore.add(ChatColor.BLUE + "Offhand:");
                        }
                    }
                }



                if (item == null) {
                    item = new ItemStack(Material.AIR);
                } else if (item.getType().toString().equalsIgnoreCase("DIAMOND")){
                    //Diamond check; do nothing

                } else if (item.getType().toString().equalsIgnoreCase("SPLASH_POTION")){
                    //Do stuff with potions
                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    lore.add("    " + ChatColor.GRAY + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()));

                } else if (item.getType().toString().equalsIgnoreCase("POTION")){
                    //Do stuff with potions
                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    lore.add("    " + ChatColor.GRAY + MyStringUtils.itemMetaToEffects(item.getItemMeta().toString()));

                } else if (item.getType().toString().equalsIgnoreCase("PLAYER_HEAD")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase("golden_head"));
                    lore.add(ChatColor.GRAY + "    " + item.getItemMeta().getLore().get(0));
                } else if (item.getType().toString().equalsIgnoreCase("CHEST")){

                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase("random_loot_chest"));
                    lore.add(ChatColor.GRAY + "    " + item.getItemMeta().getLore().get(0));

                } else {
                    String amount = String.valueOf(item.getAmount());
                    lore.add(ChatColor.GRAY + amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(item.getType().toString()));
                    //If it has enchants, view them
                    if (!(item.getEnchantments().isEmpty())){
                        lore.add("    " + ChatColor.GRAY + MyStringUtils.mapStringToEnchantment(item.getEnchantments().toString()));
                    }

                }
            }
            //Set KillItem as lore
            ItemStack killItem = (ItemStack) KitItemsConfig.get().get("kits." + key + ".killitem");

            if (killItem.getType().toString().equalsIgnoreCase("PLAYER_HEAD")) {
                lore.add(ChatColor.WHITE + "Item on Kill:");
                lore.add(ChatColor.GRAY + "1x " + ChatColor.YELLOW + "Golden Head"); //fixedI would just get the displayname to make this more dynamic but I cant because of fecking component
                lore.add(ChatColor.GRAY + "    " + killItem.getItemMeta().getLore().get(0));

            } else if (killItem.getType().toString().equalsIgnoreCase("CHEST")){
                lore.add(ChatColor.WHITE + "Item on Kill:");
                int amount = killItem.getAmount();
                lore.add(ChatColor.GRAY + "" +  amount + "x " + ChatColor.YELLOW + "Random Loot Chest");
                lore.add(ChatColor.GRAY + "    " + killItem.getItemMeta().getLore().get(0));
                //System.out.println(killItem.getItemMeta().getLore().get(0));

            } else if (killItem.getType().toString().equalsIgnoreCase("AIR")){
                lore.add(ChatColor.WHITE + "No Item on Kill");
            } else {
                lore.add(ChatColor.WHITE + "Item on Kill:");
                int amount = killItem.getAmount();
                lore.add(ChatColor.GRAY + "" +  amount + "x " + ChatColor.YELLOW + MyStringUtils.itemCamelCase(killItem.getType().toString()));

            }


            itemMeta.setLore(lore); //Heck you Component

            //Set meta
            String kitDisplayColor = KitItemsConfig.get().getString("kits." + key + ".displayname");
            kitDisplayColor = ChatColor.translateAlternateColorCodes('&', kitDisplayColor);
            itemMeta.setDisplayName(kitDisplayColor + key); //Heck you Component
            itemStack.setItemMeta(itemMeta);

            //Add items to inventory
            kitSelectionGUI.addItem(itemStack);

            player.openInventory(kitSelectionGUI);


        }

    }

}
