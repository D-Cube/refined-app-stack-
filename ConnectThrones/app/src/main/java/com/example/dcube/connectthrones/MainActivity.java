package com.example.dcube.connectthrones;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    MediaPlayer sp;


    //starting state of the game
    private Gameplay mGame;

    //image view on board for game
    private Button mBoardButtons[];
    private Button mNewGame;

    //counter for wins and ties
    private int mPlayerOneCounter = 0;
    private int mTieCounter = 0;
    private int mPlayerTwoCounter = 0;

    //game state check
    private boolean mPlayerOneFirst = true;
    private boolean mIsSinglePlayer = false;
    private boolean mIsPlayerOneTurn = true;
    private boolean mGameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = MediaPlayer.create(MainActivity.this, R.raw.sword);

        boolean mGameType = getIntent().getExtras().getBoolean("gameType");

        mBoardButtons = new Button[mGame.getBOARD_SIZE()];
        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);
        addListenerOnButton();

        // create a new game object
        mGame = new Gameplay();

        // start a new game
        startNewGame(mGameType);
    }

    private void addListenerOnButton() {

        mNewGame = (Button) findViewById(R.id.NewGame);

        mNewGame.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                startNewGame(mIsSinglePlayer);
            }
        });
    }
    private void startNewGame(boolean isSingle)
    {

        this.mIsSinglePlayer = isSingle;

        mGame.clearBoard();

        for (int i = 0; i < mBoardButtons.length; i++)
        {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i));
            mBoardButtons[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.blank));
        }


        if (mIsSinglePlayer)
        {

            if (mPlayerOneFirst)
            {
                Toast.makeText(this, "YOUR TURN", Toast.LENGTH_SHORT).show();
                mPlayerOneFirst = false;
            }
            else
            {
                int move = mGame.getComputerMove();
                setMove(mGame.PLAYER_TWO, move);
                mPlayerOneFirst = true;
            }
        }
        else
        {
            Toast.makeText(this, "2 PLAYER MODE", Toast.LENGTH_SHORT).show();

            if (mPlayerOneFirst)
            {
                //Toast.makeText(this, "PLAYER 1 TURN", Toast.LENGTH_SHORT).show();
                mPlayerOneFirst = false;
            }
            else
            {
                //Toast.makeText(this, "PLAYER 2 TURN", Toast.LENGTH_SHORT).show();
                mPlayerOneFirst = true;
            }
        }

        mGameOver = false;
    }

    private class ButtonClickListener implements View.OnClickListener
    {
        int location;

        public ButtonClickListener(int location)
        {
            this.location = location;
        }

        public void onClick(View view)
        {
            if (!mGameOver)
            {
                if (mBoardButtons[location].isEnabled())
                {
                    if (mIsSinglePlayer)
                    {
                        setMove(mGame.PLAYER_ONE, location);

                        int winner = mGame.checkForWinner();

                        if (winner == 0)
                        {

                            int move = mGame.getComputerMove();
                            setMove(mGame.PLAYER_TWO, move);
                            winner = mGame.checkForWinner();
                        }

                        if (winner == 0)
                            Toast.makeText(MainActivity.this, "YOUR TURN", Toast.LENGTH_SHORT).show();

                        else if (winner == 1)
                        {
                            //Toast.makeText(MainActivity.this, "BATTLE TIED", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("BATTLE TIED")
                                    .setPositiveButton("Play Again", null)
                                    .show();

                            mTieCounter++;

                            mGameOver = true;
                        }
                        else if (winner == 2)
                        {
                            //Toast.makeText(MainActivity.this, "YOU WIN", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("YOU WIN")
                                    .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    })
                                    .show();

                            mPlayerOneCounter++;
                            mGameOver = true;
                        }
                        else
                        {
                            //Toast.makeText(MainActivity.this, "YOU LOOSE", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("YOU LOOSE")
                                    .setPositiveButton("Play Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            startNewGame(mIsSinglePlayer);
                                        }
                                    })
                                    .show();

                            mPlayerTwoCounter++;
                            mGameOver = true;
                        }
                    }
                    else
                    {
                        if (mIsPlayerOneTurn)
                            setMove(mGame.PLAYER_ONE, location);
                        else
                            setMove(mGame.PLAYER_TWO, location);

                        int winner = mGame.checkForWinner();

                        if (winner == 0)
                        {
                            if (mIsPlayerOneTurn)
                            {
                                //Toast.makeText(getApplicationContext(), "PLAYER 1 TURN", Toast.LENGTH_SHORT).show();
                                mIsPlayerOneTurn = false;
                            }
                            else
                            {
                                //Toast.makeText(getApplicationContext(), "PLAYER 2 TURN", Toast.LENGTH_SHORT).show();
                                mIsPlayerOneTurn = true;
                            }
                        }
                        else if (winner == 1)
                        {
                            //Toast.makeText(MainActivity.this, "BATTLE TIED", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("BATTLE TIED")
                                    .setPositiveButton("Play Again", null)
                                    .show();

                            mTieCounter++;

                            mGameOver = true;
                        }
                        else if (winner == 2)
                        {
                            //Toast.makeText(MainActivity.this, "PLAYER 1 WINS", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("PLAYER 1 WINS")
                                    .setPositiveButton("Play Again", null)
                                    .show();
                            mPlayerOneCounter++;

                            mGameOver = true;
                            mIsPlayerOneTurn = false;
                        }
                        else
                        {
                            //Toast.makeText(MainActivity.this, "PLAYER  2 WINS", Toast.LENGTH_SHORT).show();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setIcon(android.R.drawable.btn_star)
                                    .setTitle("VALAR MORGHULIS")
                                    .setMessage("PLAYER 2 WINS")
                                    .setPositiveButton("Play Again", null)
                                    .show();

                            mPlayerTwoCounter++;
                            mGameOver = true;
                            mIsPlayerOneTurn = true;
                        }
                    }
                }
            }
        }
    }


    // set move for the current player
    private void setMove(char player, int location)
    {

        int playerId = getIntent().getIntExtra("player" , -1);
        mGame.setMove(player, location);
        mBoardButtons[location].setEnabled(false);

        if (player == mGame.PLAYER_ONE){

            switch (playerId){
                case 0:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.stark));
                    break;
                case 1:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.lanister));
                    break;
                case 2:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.targarian));
                    break;
                case 3:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.bolton));
                    break;
                case 4:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.martel));
                    break;
                case 5:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.tyrell));
                    break;
                case 6:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.greyjoy));
                    break;
                case 7:
                    mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.boratheon));
                    break;
            }
            sp.start(); //sound for player 1

        }else{
            mBoardButtons[location].setBackgroundDrawable(getResources().getDrawable(R.drawable.boratheon));
            sp.start(); //sound for player 2
        }
    }
}
