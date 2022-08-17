package me.lifelessnerd.castlesiege.managers;

import me.lifelessnerd.castlesiege.CastleSiege;

public class GameManager {

    private final CastleSiege plugin;
    public GameState gameState = GameState.LOBBY;


    public GameManager(CastleSiege plugin) {
        this.plugin = plugin;
    }

    public void setGameState(GameState gameState) {


        this.gameState = gameState;
        switch(gameState) {
            //Only from previous being LOBBY
            case PREGAME:





        }

    }
}
