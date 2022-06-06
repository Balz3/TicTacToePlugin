package com.balz3.tictactoe.commands;

import com.balz3.tictactoe.CommandBase;
import com.balz3.tictactoe.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TicTacToe {
    private char[][] gameBoard = null;

    String player1, player2;

    //Player who initializes game will go first
    Boolean player1Turn = true;
    int numMoves = 0;

    public TicTacToe(){
            //Start the game
            new CommandBase("startGame",1, false) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args){
                //Check if a game is already active
                if(gameBoard != null){
                    Message.send(sender, "&cThere is already an active game!");
                }

                //If the sender is the console
                if(!(sender instanceof Player)){
                    //If the challenged player is a player and is not operator
                    if(Bukkit.getServer().getPlayerExact(args[0]) != null
                            && !args[0].equalsIgnoreCase("operator")){
                        player1 = "operator";
                        player2 = args[0];

                        Message.send(player1, "You have challenged " + player2 + " to a game!");
                        Message.send(player2, "The operator has challenged you to a game!");

                    //Else the challenged player is invalid
                    } else {
                        Message.send("operator", "You are challenging an invalid player!");

                        return true;
                    }

                //Else if the sender is a player and challenges the operator
                } else if(args[0].equalsIgnoreCase("operator")){
                    player1 = sender.getName();
                    player2 = "operator";

                    Message.send("operator", player1 + " has started a game with you!");
                    Message.send(sender, "You have challenged the operator to a game!");

                //Else if the sender is a player and challenges another player
                } else if(Bukkit.getServer().getPlayerExact(args[0]) != null && !args[0].equalsIgnoreCase(sender.getName())){
                    player1 = sender.getName();
                    player2 = Bukkit.getServer().getPlayerExact(args[0]).getName();

                    Message.send(sender, "You have challenged " + player2 + " to a game!");
                    Message.send(Bukkit.getServer().getPlayerExact(player2), player1 + " has challenged you to a game!");

                //Else the sender is a player and challenges an invalid player
                } else {
                    Message.send(sender, "&cYou are challenging an invalid player!");
                    return true;
                }

                //Initialize the board in any case
                gameBoard = new char[3][3];

                return true;
            }

            @Override
            public String getUsage() {
                return "/startGame playerName";
            }
        }.enableDelay(2);

        //Make a move if a player
        new CommandBase("makeMove",2, false) {
            @Override
            public boolean onCommand(CommandSender sender, String[] args){
                String senderName = sender instanceof Player ? sender.getName() : "operator";
                String playerName = getPlayer(senderName);
                char currMove;
                String winner;

                //Check for game being active
                if(gameBoard == null){
                    Message.send(sender, "&cThere are no active games at the moment!");
                    return true;
                } else {
                    //Check if the command sender is a player of the current game
                    if(playerName == null){
                        Message.send(sender, "&cYou are not a current game player!");
                        return true;
                    }
                }

                //Here we know there is an active game and that

                //Check to see if it's the sender's turn
                if((player1Turn && playerName != player1) || (!player1Turn && playerName != player2)){
                    Message.send(sender, "&cIt's not your turn to make a move!");
                    return true;
                }

                //check if their position is valid
                if((Integer.parseInt(args[0]) > 2 || Integer.parseInt(args[0]) < 0)
                || (Integer.parseInt(args[1]) > 2 || Integer.parseInt(args[1]) < 0)){
                    Message.send(sender, "&cThat is an invalid game position!");
                    return true;
                }

                //If it is player1's and the game doesnt involve an operator
                //OR If it is an operatorGame and it is the operators turn
                if(player1Turn) {
                    currMove = 'X';
                } else {
                    currMove = 'O';
                }

                //Process their game move
                if(gameBoard[Integer.parseInt(args[0])][Integer.parseInt(args[1])] == 0){
                    gameBoard[Integer.parseInt(args[0])][Integer.parseInt(args[1])] = currMove;
                    numMoves++;

                    //Flip the turn indicator
                    player1Turn = !player1Turn;

                } else {
                    Message.send(sender, "&cThat is an invalid game position!");
                    return true;
                }

                printBoard(player1, player2, "&a", "&b");

                winner = checkForWin(Integer.parseInt(args[0]), Integer.parseInt(args[1]), 3, currMove);


                //Check for game end condition


                if(winner == "Win"){
                    Message.send(playerName, "You have won the game!");
                    Message.send(player2, playerName + " has won the game!");
                    gameBoard = null;
                    numMoves = 0;
                    player1 = "";
                    player2 = "";
                } else if(winner == "Draw") {
                    Message.send(player1, "The game was a draw!");
                    Message.send(player2, "The game was a draw!");
                    gameBoard = null;
                    numMoves = 0;
                    player1 = "";
                    player2 = "";
                }


                return true;
            }

            @Override
            public String getUsage() {
                return "/ttt row col";
            }
        }.enableDelay(2);

    }

    private void printBoard(String player1, String player2, String colorCode1, String colorCode2){
        String message = "\n";
        char val;

        for(int x = 0; x < 3; x++){
            for(int y = 0; y < 3; y++){
                if(gameBoard[x][y] == 0){
                    val = '-';
                } else {
                    val = gameBoard[x][y];
                }

                message += val + "  ";

            }

            message += "\n";

        }
        Message.send(player1, message);
        Message.send(player2, message);
    }

    private String getPlayer(String playerName){
        if(playerName.equalsIgnoreCase(player1)){
            return player1;
        } else if(playerName.equalsIgnoreCase(player2)){
            return player2;
        }

        return null;
    }

    private String checkForWin(int x, int y, int n, char s){


        //check col
        for(int i = 0; i < n; i++){
            if(gameBoard[x][i] != s)
                break;
            if(i == n-1){
                return "Win";
            }
        }

        //check row
        for(int i = 0; i < n; i++){
            if(gameBoard[i][y] != s)
                break;
            if(i == n-1){
                return "Win";
            }
        }

        //check diag
        if(x == y){
            //we're on a diagonal
            for(int i = 0; i < n; i++){
                if(gameBoard[i][i] != s)
                    break;
                if(i == n-1){
                    return "Win";
                }
            }
        }

        //check anti diag (thanks rampion)
        if(x + y == n - 1){
            for(int i = 0; i < n; i++){
                if(gameBoard[i][(n-1)-i] != s)
                    break;
                if(i == n-1){
                    return "Win";
                }
            }
        }

        //check draw
        if(numMoves == (Math.pow(n, 2) - 1)){
            return "Draw";
        }

        return "Neither";
    }
}

