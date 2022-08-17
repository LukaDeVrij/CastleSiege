package me.lifelessnerd.castlesiege.managers;

import me.lifelessnerd.castlesiege.CastleSiege;
import me.lifelessnerd.castlesiege.pregame.CountdownTimer;
import me.lifelessnerd.castlesiege.utils.MyUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class GameManager {

    private final CastleSiege plugin;
        public GameState gameState = GameState.PREGAME;
    public ArrayList<Player> teamOne = new ArrayList<>();
    public ArrayList<Player> teamTwo = new ArrayList<>();
    ArrayList<Player> gamePlayers;


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
                if (!(gameState == GameState.STARTING)){ return; }
                // Pregame > Starting code
                CountdownTimer timer = new CountdownTimer(plugin, 10,
                        () -> MyUtils.broadcastGroup(gamePlayers, "&eGame starts in 10 seconds!"),
                        () -> {
                            //Check if there are enough players
                            if (checkStartRequirements()){
                                MyUtils.broadcastGroup(gamePlayers, "&eGame starting!");
                                setGameState(GameState.RUNNING); // Does this nested thing work?
                                startGameLogic();
                            }
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
        // Not sure if this is even necessary
        // Might add stuff here later

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
            MyUtils.broadcastGroup(gamePlayers, "Teams: \n" + teamOne.toString() + "\n" + teamTwo.toString());
            return true;
        }
        else {
            MyUtils.broadcastGroup(gamePlayers, "&cNot enough players! Try again.");
            return false;
        }
    }

}
