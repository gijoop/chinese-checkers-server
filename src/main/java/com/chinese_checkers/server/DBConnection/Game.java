package com.chinese_checkers.server.DBConnection;

import java.sql.Date;

import com.chinese_checkers.comms.Player.Corner;
import com.chinese_checkers.server.Game.Ruleset.Ruleset;

public class Game {
    private int id;
    private Date last_save_date;
    private int numPlayers;
    private Ruleset.type ruleset;
    private Corner currentTurn;
    private int boardSize;

    public Game() {
    }

    public Game(int numPlayers, Ruleset.type ruleset, Corner currentTurn, int boardSize) {
        this.numPlayers = numPlayers;
        this.ruleset = ruleset;
        this.currentTurn = currentTurn;
        this.boardSize = boardSize;
    }

    public Game(int id, Date last_save_date, int numPlayers, Ruleset.type ruleset, Corner currentTurn, int boardSize) {
        this.id = id;
        this.last_save_date = last_save_date;
        this.numPlayers = numPlayers;
        this.ruleset = ruleset;
        this.currentTurn = currentTurn;
        this.boardSize = boardSize;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return last_save_date;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Ruleset.type getRuleset() {
        return ruleset;
    }

    public Corner getCurrentTurn() {
        return currentTurn;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date last_save_date) {
        this.last_save_date = last_save_date;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setRuleset(Ruleset.type ruleset) {
        this.ruleset = ruleset;
    }

    public void setCurrentTurn(Corner currentTurn) {
        this.currentTurn = currentTurn;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    @Override
    public String toString() {
        return  "ID: " + id +
                ", last_save_date=" + last_save_date +
                ", numPlayers=" + numPlayers +
                ", ruleset=" + ruleset +
                ", currentTurn=" + currentTurn +
                ", boardSize=" + boardSize;
    }
}

