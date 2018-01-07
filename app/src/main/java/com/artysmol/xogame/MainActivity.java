package com.artysmol.xogame;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    ImageView[][] mGrid;
    ImageView mP1Logo;
    ImageView mP2Logo;
    TextView mP1Score;
    TextView mP2Score;
    TextView mGameStatus;
    Button mNewGame;
    Button mNewGamePvP;
    Button mNewGamePvE;
    Button mNextGame;
    LinearLayout mGameGrid;
    ProgressBar mP2Progress;
    Game myGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Создаём массив ImageView и связываем с реальными элементами сетки
        mGrid = new ImageView[3][3];
        mGrid[0][0] = (ImageView) findViewById(R.id.mCell_00);
        mGrid[0][1] = (ImageView) findViewById(R.id.mCell_01);
        mGrid[0][2] = (ImageView) findViewById(R.id.mCell_02);
        mGrid[1][0] = (ImageView) findViewById(R.id.mCell_10);
        mGrid[1][1] = (ImageView) findViewById(R.id.mCell_11);
        mGrid[1][2] = (ImageView) findViewById(R.id.mCell_12);
        mGrid[2][0] = (ImageView) findViewById(R.id.mCell_20);
        mGrid[2][1] = (ImageView) findViewById(R.id.mCell_21);
        mGrid[2][2] = (ImageView) findViewById(R.id.mCell_22);

        mGameGrid = (LinearLayout) findViewById(R.id.game_grid);

        //Очки и иконки игроков
        mGameStatus= (TextView) findViewById(R.id.tv_game_status);
        mP1Score = (TextView) findViewById(R.id.tv_p1_points);
        mP2Score = (TextView) findViewById(R.id.tv_p2_points);
        mP1Logo= (ImageView) findViewById(R.id.iv_p1_logo);
        mP2Logo= (ImageView) findViewById(R.id.iv_p2_logo);
        mP2Progress= (ProgressBar) findViewById(R.id.pb_p2_progress);

        //Кнопки
        mNextGame = (Button) findViewById(R.id.bNextGame);
        mNewGame = (Button) findViewById(R.id.bNewGame);
        mNewGamePvP = (Button) findViewById(R.id.bNewGamePvP);
        mNewGamePvE = (Button) findViewById(R.id.bNewGamePvE);


    }

    public void gridClick(View view) {

            for (int i=0;i<=2;i++) {
                for (int j=0;j<=2;j++){
                    if (view.getId()==mGrid[i][j].getId()) {        //Ищем, на какую клетку нажали
                        int turnResult = myGame.makeTurn(i,j);      //Делаем ход в эту клетку
                        if (turnResult!=0) {                        //Если ход допустимый
                            redrawGrid(myGame.getGridState());      //Перерисовываем игровое поле
                            redrawState(myGame.getGameState());     //Перерисовываем текущий статус игры

                            if (myGame.getVsBot() && (myGame.getGameState()==Game.P1_TURN || myGame.getGameState()==Game.P2_TURN)) {
                                mP2Progress.setVisibility(View.VISIBLE);
                                new CountDownTimer(1000, 500) {

                                    public void onTick(long millisUntilFinished) {

                                    }

                                    public void onFinish() {
                                        myGame.makeRandomTurn();
                                        mP2Progress.setVisibility(View.INVISIBLE);
                                        redrawGrid(myGame.getGridState());      //Перерисовываем игровое поле
                                        redrawState(myGame.getGameState());     //Перерисовываем текущий статус игры
                                    }
                                }.start();


                            }

                        }
                    }
                }
            }



    }

    /**
     * Перерисовывает игровое поле
     * @param gridState текущий статус игрового поля
     */
    public void redrawGrid(int[][] gridState) {
        //TODO Зачёркивать выирышную комбинацию
        for (int i=0;i<=2;i++){
            for (int j=0;j<=2;j++) {
                if (gridState[i][j]==1) mGrid[i][j].setImageResource(R.drawable.pig);
                if (gridState[i][j]==2) mGrid[i][j].setImageResource(R.drawable.rabbit);
                if (gridState[i][j]==0) mGrid[i][j].setImageResource(0);
            }
        }

    }

    public void redrawState(int gameState) {
          //TODO Прыгающая анимация выигравшего игрока

        switch (gameState)  {
            case Game.P1_TURN: //Ход перого игрока
                mGameStatus.setText("");
                mP1Logo.setImageResource(R.drawable.pig);
                mP2Logo.setImageResource(R.drawable.rabbit_bw75);
                break;
            case Game.P2_TURN: //Ход второго игрока
                mGameStatus.setText("");
                mP1Logo.setImageResource(R.drawable.pig_bw75);
                mP2Logo.setImageResource(R.drawable.rabbit);
                break;
            case Game.P1_WIN:
                mGameStatus.setText(R.string.p1_wins);
                mNewGame.setVisibility(View.VISIBLE);
                mNextGame.setVisibility(View.VISIBLE);
                break;
            case Game.P2_WIN:
                mGameStatus.setText(R.string.p2_wins);
                mNewGame.setVisibility(View.VISIBLE);
                mNextGame.setVisibility(View.VISIBLE);
                break;
            case Game.DRAW:
                mGameStatus.setText(R.string.draw_wins);
                mNewGame.setVisibility(View.VISIBLE);
                mNextGame.setVisibility(View.VISIBLE);
                break;
        }

        mP1Score.setText(String.valueOf(myGame.getPoints(1)));
        mP2Score.setText(String.valueOf(myGame.getPoints(2)));

    }

    /**
     * Нажатие кнопок "Между собой" и "Против ИИ"
     * @param view
     */
    public void newGame (View view) {

        //Создаём новый объект игры
        if (view.getId()==mNewGamePvP.getId()) {
            int rndPlayer=(int) (Math.random() * 2) + 1;
            myGame = new Game(rndPlayer,false);
        } else {
            myGame = new Game(Game.P1_TURN,true);
        }

        redrawState(myGame.getGameState());
        redrawGrid(myGame.getGridState());

        if (myGame.getGameState()==Game.P1_TURN) {
            mGameStatus.setText("Начинает Свинья");
        } else {
            mGameStatus.setText("Начинает Зайчик");
        }

        mNewGamePvP.setVisibility(View.GONE);
        mNewGamePvE.setVisibility(View.GONE);
        mGameGrid.setVisibility(View.VISIBLE);
    }

    /**
     * Нажатие кнопки "Продолжить"
     * @param view
     */
    public void nextGame (View view) {
        myGame.clearGrid();
        myGame.changeStartPlayer();

        redrawState(myGame.getGameState());
        redrawGrid(myGame.getGridState());

        mNextGame.setVisibility(View.INVISIBLE);
        mNewGame.setVisibility(View.INVISIBLE);

        if (myGame.getGameState()==Game.P1_TURN) {
            mGameStatus.setText("Начинает Свинья");
        } else {
            mGameStatus.setText("Начинает Зайчик");
        }
    }

    /**
     * Нажатие кнопки "Новая игра"
     * @param view
     */
    public void startMenu (View view) {
        myGame.clearPoints();
        redrawState(myGame.getGameState());

        mP1Logo.setImageResource(R.drawable.pig);
        mP2Logo.setImageResource(R.drawable.rabbit);
        mGameStatus.setText("Новая игра");

        mNewGamePvP.setVisibility(View.VISIBLE);
        mNewGamePvE.setVisibility(View.VISIBLE);
        mNewGame.setVisibility(View.GONE);
        mNextGame.setVisibility(View.GONE);
        mGameGrid.setVisibility(View.GONE);

    }
}
