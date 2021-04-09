package no.birdygolf.gruppe19;

import java.util.List;

import no.birdygolf.gruppe19.levels.Level;

public class GameManager {
    public static final GameManager INSTANCE = new GameManager();


    private final no.birdygolf.gruppe19.levels.Level[] levels = Level.values();
    public int currentLevel = 0;

    public int playerTurn = 0;
    public List<String> playerNames;
    public List<Integer> playerHits;


    private GameManager() {
    }

    public void nextPlayer() {
        playerTurn++;
        playerTurn %= playerNames.size();
    }

    public void increaseHits() {
        playerHits.set(playerTurn, playerHits.get(playerTurn) + 1);
    }

}