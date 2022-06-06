package com.balz3.tictactoe;

import com.balz3.tictactoe.commands.TicTacToe;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        System.out.println("TicTacToe is loading up!");
        new TicTacToe();
    }

    @Override
    public void onDisable() {
        System.out.println("TicTacToe is shutting down!");
    }

    public static Main getInstance(){
        return instance;
    }
}
