package me.lifelessnerd.castlesiege.commands;

import me.lifelessnerd.castlesiege.managers.GameManager;
import me.lifelessnerd.castlesiege.managers.GameState;
import me.lifelessnerd.castlesiege.managers.Subcommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class StartCommand extends Subcommand {
    Plugin plugin;
    GameManager gameManager;
    public StartCommand(Plugin plugin, GameManager manager) {
        this.gameManager = manager;
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "start";
    }

    @Override
    public String[] getAliases() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Starts the game via command";
    }

    @Override
    public String getSyntax() {
        return "/siege start";
    }

    @Override
    public boolean perform(Player player, String[] args) {

        player.sendMessage("Attempting to start the game...");
        gameManager.setGameState(GameState.STARTING);

        return true;
    }
}
