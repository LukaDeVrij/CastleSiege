package me.lifelessnerd.castlesiege.managers;

import me.lifelessnerd.castlesiege.CastleSiege;
import me.lifelessnerd.castlesiege.commands.StartCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    ArrayList<Subcommand> subcommands = new ArrayList<>();
    GameManager gameManager;

    public CommandManager(CastleSiege plugin, GameManager manager) {
        this.gameManager = manager;
        subcommands.add(new StartCommand(plugin, manager));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        
        if (!(sender instanceof Player)){
            sender.sendMessage("The console cannot perform CastleSiege commands.");
            return false;
        }
        Player player = (Player) sender;

        if (args.length < 1){
            player.sendMessage("Please specify what function of CastleSiege to use.");
            return false;
        }

        //Check for names of subcommands in arg
        for (int i = 0; i < getSubcommands().size(); i++){
            if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())){
                boolean result = getSubcommands().get(i).perform(player, args);
                return true; // All help dialogs are done in-class with player.sendMessage
            }
        }
        return false;
    }


    public ArrayList<Subcommand> getSubcommands(){
        return subcommands;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1){
            List<String> arguments = new ArrayList<>();
            for (int i = 0; i < getSubcommands().size(); i++){
                arguments.add(getSubcommands().get(i).getName());
            }
            return arguments;
        }

        return new ArrayList<>();
    }
}
