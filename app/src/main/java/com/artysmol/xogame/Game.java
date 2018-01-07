package com.artysmol.xogame;

import java.util.Random;

/**
 * Created by artys on 08.12.2017.
 */

public class Game {

    //Задаём статусы игры
    public static final int P1_TURN = 1;
    public static final int P2_TURN = 2;
    public static final int P1_WIN = 3;
    public static final int P2_WIN = 4;
    public static final int DRAW = 5;

    private int[][] gridState;  //Статус игрового поля
    private int gameState;      //Статус игры
    private int points_p1;      //Очки первого игрока
    private int points_p2;      //Очки второго игрока
    private int startPlayer;    //С какого игрока начиналась текущая игра
    private boolean vsBot;          //Признак игры с ботом

    Game() {
         this(P1_TURN, false);
    }

    Game(int startPlayer, boolean vsBot) {
        gridState = new int[3][3];
        this.startPlayer = startPlayer;
        gameState = startPlayer;
        this.vsBot=vsBot;
        clearGrid();
        clearPoints();
    }

    public void clearPoints() {
        points_p1 = 0;
        points_p2 = 0;
    }

    public void changePlayer() {
        if (gameState == P1_TURN) {
            gameState = P2_TURN;
        } else gameState = P1_TURN;
    }

    public void changeStartPlayer() {
        if (startPlayer == P1_TURN) {
            startPlayer = P2_TURN;
            gameState = P2_TURN;
        } else {
            startPlayer = P1_TURN;
            gameState = P1_TURN;
        }
    }

    public int getPoints(int player) {
        if (player == 1) {
            return points_p1;
        } else {
            return points_p2;
        }
    }

    public boolean getVsBot() {return vsBot;}

    /**
     * Обнуление игрового поля
     */
    public void clearGrid() {
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                gridState[i][j] = 0;
            }

        }
        gameState = startPlayer;
    }

    /**
     * Возвращает текущий статус игрового поля
     *
     * @return
     */
    public int[][] getGridState() {
        return gridState;
    }

    /**
     * Возвращает текущий статус игры
     *
     * @return текущий статус игры
     */
    public int getGameState() {
        return gameState;
    }

    /**
     * Обновляет статус доски.
     *
     * @param row
     * @param col
     * @return возвращает статус игры после хода. Возвращает 0, если в клетку уже ходили.
     */
    public int makeTurn(int row, int col) {

        if ((gridState[row][col] == 0) && (gameState==P1_TURN || gameState==P2_TURN) ) {       //Проверяем, что клетка не занята и сейчас ход одного из игроков
            gridState[row][col] = gameState;  //Проставляем в клетку отметку текущего игрока
            // Проверяем нет ли выигрыша или ничьей
            int checkWinResult = checkWin(gridState);
            if (checkWinResult != 0) {
                gameState = checkWinResult;
            } else { // Если нет, то меняем игрока
                changePlayer();
            }
            return gameState;
        } else return 0;
    }

    public int makeRandomTurn() {
        int result = 0;
        boolean validTurn=false;
        while (!validTurn) {
            int row = (int) (Math.random() * 3);
            int col = (int) (Math.random() * 3);
            if (gridState[row][col] == 0) {
                result = makeTurn(row, col);
                validTurn = true;
            }
        }
        return result;
    }

    /**
     * Проверяет, есть ли выигрышная комбинация на доске
     *
     * @param grid статус игрового поля
     * @return статус игры
     */
    private int checkWin(int[][] grid) {
        int result = DRAW;

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                if (grid[i][j] == 0) {
                    result = 0;
                }
            }
        }
        if ((grid[0][0] == 1 && grid[0][1] == 1 && grid[0][2] == 1) ||
                (grid[1][0] == 1 && grid[1][1] == 1 && grid[1][2] == 1) ||
                (grid[2][0] == 1 && grid[2][1] == 1 && grid[2][2] == 1) ||
                (grid[0][0] == 1 && grid[1][0] == 1 && grid[2][0] == 1) ||
                (grid[0][1] == 1 && grid[1][1] == 1 && grid[2][1] == 1) ||
                (grid[0][2] == 1 && grid[1][2] == 1 && grid[2][2] == 1) ||
                (grid[0][0] == 1 && grid[1][1] == 1 && grid[2][2] == 1) ||
                (grid[0][2] == 1 && grid[1][1] == 1 && grid[2][0] == 1)) {
            points_p1++;
            result = P1_WIN;
        }

        if ((grid[0][0] == 2 && grid[0][1] == 2 && grid[0][2] == 2) ||
                (grid[1][0] == 2 && grid[1][1] == 2 && grid[1][2] == 2) ||
                (grid[2][0] == 2 && grid[2][1] == 2 && grid[2][2] == 2) ||
                (grid[0][0] == 2 && grid[1][0] == 2 && grid[2][0] == 2) ||
                (grid[0][1] == 2 && grid[1][1] == 2 && grid[2][1] == 2) ||
                (grid[0][2] == 2 && grid[1][2] == 2 && grid[2][2] == 2) ||
                (grid[0][0] == 2 && grid[1][1] == 2 && grid[2][2] == 2) ||
                (grid[0][2] == 2 && grid[1][1] == 2 && grid[2][0] == 2)) {
            points_p2++;
            result = P2_WIN;
        }

        return result;
    }
}
