package me.lifelessnerd.castlesiege.managers;

import me.lifelessnerd.castlesiege.CastleSiege;
import me.lifelessnerd.castlesiege.files.KitItemsConfig;
import me.lifelessnerd.castlesiege.pregame.CountdownTimer;
import me.lifelessnerd.castlesiege.utils.MyUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

public class GameManager {

    private final CastleSiege plugin;
    public GameState gameState = GameState.PREGAME;
    public ArrayList<Player> teamOne = new ArrayList<>();
    public ArrayList<Player> teamTwo = new ArrayList<>();
    ArrayList<Player> gamePlayers;
    Player team1King = null;
    Player team2King = null;


    public GameManager(CastleSiege plugin) {
        this.plugin = plugin;
    }

    public void setGameState(GameState gameState) {
        GameState previous = this.gameState;
        this.gameState = gameState;

        gamePlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.getWorld().getName().equalsIgnoreCase(plugin.getConfig().getString("gameWorld"))){
                gamePlayers.add(player);
            }
        }

        switch(previous){

            case PREGAME:
                if (!(gameState == GameState.STARTING)){
                    return; }
                // Pregame > Starting code
                // TODO: Give kit selection item, Allow Kit selection
                CountdownTimer timer = new CountdownTimer(plugin, 10,
                        () -> MyUtils.broadcastGroup(gamePlayers, "&eGame starts in 10 seconds!"),
                        () -> {
                            //Check if there are enough players
                            if (checkStartRequirements()){
                                MyUtils.broadcastGroup(gamePlayers, "&eGame starting!");
                                setGameState(GameState.RUNNING); // Does this nested thing work?

                                startGameLogic();
                            } else this.gameState = previous;
                        });
                timer.scheduleTimer();

            case RUNNING:
                if (!(gameState == GameState.POSTGAME)){ return; }
                // RUNNING > Postgame code, win condition checking elsewhere

            case POSTGAME:
                if (!(gameState == GameState.RESTARTING)){ return; }
                // Postgame > kick en restart als nodig

            case RESTARTING:
                if (!(gameState == GameState.LOBBY)){ return; }
                //
        }
    }

    private void startGameLogic() {

        //Locations from config to teleport to
        World gameWorld = null;
        try {
            gameWorld = Bukkit.getWorld(plugin.getConfig().getString("gameWorld"));

            Location teamOneSpawn = new Location(gameWorld,
                    plugin.getConfig().getDouble("teamOneSpawn.x"),
                    plugin.getConfig().getDouble("teamOneSpawn.y"),
                    plugin.getConfig().getDouble("teamOneSpawn.z"));

            Location teamTwoSpawn = new Location(gameWorld,
                    plugin.getConfig().getDouble("teamTwoSpawn.x"),
                    plugin.getConfig().getDouble("teamTwoSpawn.y"),
                    plugin.getConfig().getDouble("teamTwoSpawn.z"));

            // Givekit  items to the players and reset all properties of players
            for (Player player : gamePlayers){

                // Kit Item
                ItemStack kitItem = new ItemStack(Material.BOW, 1);
                ItemMeta kitItemMeta = kitItem.getItemMeta();
                kitItemMeta.displayName(Component.text("Kit Selection").color(TextColor.color(100, 255, 100)));
                kitItemMeta.addEnchant(Enchantment.DURABILITY, 1, true);
                kitItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                kitItemMeta.getPersistentDataContainer().set(new NamespacedKey(plugin, "Kit Selection Item"), PersistentDataType.STRING, "true");
                kitItem.setItemMeta(kitItemMeta);

                player.setHealth(20);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 1, 20));
                for (PotionEffect effect : player.getActivePotionEffects())
                    player.removePotionEffect(effect.getType());

                player.playSound(player, Sound.ENTITY_ENDER_DRAGON_GROWL, 1, 1);

            }

            //Teleport players to their locations provided in config
            for (Player teamOnePlayer : teamOne){
                teamOnePlayer.teleport(teamOneSpawn);


            }
            for (Player teamTwoPlayer : teamTwo){
                teamTwoPlayer.teleport(teamTwoSpawn);

            }
            //Get random kings or kings provided in config
            Collections.shuffle(teamOne);
            Collections.shuffle(teamTwo);
            if (plugin.getConfig().getBoolean("kingPrefer")){
                try {
                    team1King = Bukkit.getPlayerExact(plugin.getConfig().getString("kingPreferPlayers.teamOneKing"));
                    team2King = Bukkit.getPlayerExact(plugin.getConfig().getString("kingPreferPlayers.teamTwoKing"));
                } catch (Exception e){
                    plugin.getLogger().log(Level.WARNING, "Cannot find players submitted in config.yml! Using random players!");
                    team1King = teamOne.get(0);
                    team2King = teamTwo.get(0);
                }
            } else {
                team1King = teamOne.get(0);
                team2King = teamTwo.get(0);
            }
            //All UX for player
            MyUtils.broadcastGroup(gamePlayers, "&cThe teams are as follows:\n&aTeam 1 (Attackers): \n");
            for (Player team1Player : teamOne) MyUtils.broadcastGroup(gamePlayers, "&r" + team1Player.getName() + "\n");
            MyUtils.broadcastGroup(gamePlayers, "&aTeam 2 (Defenders): \n");
            for (Player team2Player : teamTwo) MyUtils.broadcastGroup(gamePlayers, "&r" + team2Player.getName() + "\n");
            MyUtils.broadcastGroup(teamOne, String.format("""
                    
                    &aCastle Siege - by LifelessNerd
                    &eSiege game is starting!
                    &eYou are an &cATTACKER
                    &eYour king is &c%s
                    &6As an attacker, kill &c%s &6at all costs!
                    
                    """,  team1King.getName(), team2King.getName()));
            MyUtils.broadcastGroup(teamTwo, String.format("""
                    
                    &aCastle Siege - by LifelessNerd
                    &eSiege game is starting!
                    &eYou are an &cDEFENDER
                    &eYour king is &c%s
                    &6As a defender, defend your king at all costs!
                    
                    """, team2King.getName()));

        } catch (Exception e){
            plugin.getLogger().log(Level.WARNING, e.getMessage());
            plugin.getLogger().log(Level.SEVERE, "Something went wrong! Check config.yml and kititems.yml!");
        }


    }

    public boolean checkStartRequirements(){

        int amount = gamePlayers.size();
        if (amount >= plugin.getConfig().getInt("minPlayerAmount")){

            int index = 0;
            for (Player player : gamePlayers){
                if (index % 2 == 0) teamOne.add(player);
                else teamTwo.add(player); // this looks cursed
                index++;
            }

            return true;
        }
        else {
            MyUtils.broadcastGroup(gamePlayers, "&cNot enough players! Try again.");
            return false;
        }
    }

}
