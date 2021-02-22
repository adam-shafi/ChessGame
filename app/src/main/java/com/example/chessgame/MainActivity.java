package com.example.chessgame;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Chess chessGame;
    private Button[][] buttons;
    private int[][] game;
    private int[][] currentGreen;
    private int oldRow;
    private int oldCol;
    private TextView scorekeeper;
    private TextView status;
    private TextView pawn1;
    private TextView pawn2;
    private int whiteWins;
    private int blackWins;
    private int[] whitePassant;
    private int[] blackPassant;
    private boolean canBlackCastle;
    private boolean canWhiteCastle;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        chessGame = new Chess();
        game = chessGame.getGame();
        whitePassant = new int[Chess.SIDE];
        blackPassant = new int[Chess.SIDE];
        for(int i=0;i<Chess.SIDE;i++){
            whitePassant[i]=0;
            blackPassant[i]=0;
        }
        whiteWins=0;
        blackWins=0;
        canBlackCastle=true;
        canWhiteCastle=true;
        buildGuiByCode();

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void buildGuiByCode() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        int w = size.x / Chess.SIDE;
        currentGreen = new int[Chess.SIDE][Chess.SIDE];
        GridLayout gridLayout = new GridLayout(this);
        gridLayout.setColumnCount(Chess.SIDE);
        gridLayout.setRowCount(Chess.SIDE+2);
        buttons = new Button[Chess.SIDE][Chess.SIDE];
        ButtonHandler bh = new ButtonHandler();
        for (int row = 0; row < Chess.SIDE; row++) {
            for (int col = 0; col < Chess.SIDE; col++) {
                buttons[row][col] = new Button(this);
                buttons[row][col].setOnClickListener(bh);
                gridLayout.addView(buttons[row][col], w, w);
                movePieces(row,col);
            }
        }
        pawn1 = new TextView(this);
        GridLayout.Spec rowSpec1 = GridLayout.spec(Chess.SIDE,2);
        GridLayout.Spec colSpec1 = GridLayout.spec(0,2);
        GridLayout.LayoutParams lpPawn1 = new GridLayout.LayoutParams(rowSpec1,colSpec1);
        pawn1.setLayoutParams(lpPawn1);
        pawn1.setWidth(2*w);
        pawn1.setHeight(2*w);
        pawn1.setGravity(Gravity.CENTER);
        //pawn1.setBackgroundColor(Color.GREEN);
        pawn1.setForeground(getDrawable(R.mipmap.white_pawn));
        gridLayout.addView(pawn1);

        pawn2 = new TextView(this);
        GridLayout.Spec rowSpec2 = GridLayout.spec(Chess.SIDE,2);
        GridLayout.Spec colSpec2 = GridLayout.spec(6,2);
        GridLayout.LayoutParams lpPawn2 = new GridLayout.LayoutParams(rowSpec2,colSpec2);
        pawn2.setLayoutParams(lpPawn2);
        pawn2.setWidth(2*w);
        pawn2.setHeight(2*w);
        pawn2.setGravity(Gravity.CENTER);
        //pawn2.setBackgroundColor(Color.GREEN);
        pawn2.setForeground(getDrawable(R.mipmap.white_pawn));
        gridLayout.addView(pawn2);


        status =new TextView(this);
        GridLayout.Spec rowSpec = GridLayout.spec(Chess.SIDE,2);
        GridLayout.Spec colSpec = GridLayout.spec(2,4);
        GridLayout.LayoutParams lpStatus = new GridLayout.LayoutParams(rowSpec,colSpec);
        status.setLayoutParams(lpStatus);
        status.setWidth(Chess.SIDE*w/2);
        status.setHeight(2*w);
        status.setGravity(Gravity.CENTER_HORIZONTAL);
        //status.setBackgroundColor(Color.GREEN);
        status.setTextColor(Color.BLACK);
        status.setTextSize((int)(w*.2));
        gridLayout.addView(status);
        status.setText(chessGame.result()+"\n"+"Black Wins: " +blackWins+"\n"+"White Wins: "+whiteWins);

       /* scorekeeper =new TextView(this);
        GridLayout.Spec rowSpec3 = GridLayout.spec(Chess.SIDE+1,1);
        GridLayout.Spec colSpec3 = GridLayout.spec(2,4);
        GridLayout.LayoutParams lpScoreKeeper = new GridLayout.LayoutParams(rowSpec3,colSpec3);
        scorekeeper.setLayoutParams(lpScoreKeeper);
        scorekeeper.setWidth(Chess.SIDE*w/2);
        scorekeeper.setHeight(w*2);
        scorekeeper.setGravity(Gravity.CENTER);
        //status.setBackgroundColor(Color.GREEN);
        scorekeeper.setTextColor(Color.BLACK);
        scorekeeper.setTextSize((int)(w*.15));
        gridLayout.addView(scorekeeper);
        scorekeeper.setText("Black Wins: " +blackWins+"\n"+"White Wins: "+whiteWins);*/

        resetBackgrounds();
        setContentView(gridLayout);
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void update(int row, int col) {
        int[][] possibleMoves = chessGame.play(row, col, game[row][col]);
        for(int i=0;i<Chess.SIDE;i++){
            if(whitePassant[i]==1&&chessGame.getTurn()==2)
                whitePassant[i]=2;
            if(blackPassant[i]==1&&chessGame.getTurn()==1)
                blackPassant[i]=2;
        }
        if (game[row][col] > 0 && currentGreen[row][col]!=3&&currentGreen[row][col]!=4&&currentGreen[row][col]!=5) {
            resetBackgrounds();
            if(oldRow==row &&oldCol==col) {
                oldRow=-1;
                oldCol=-1;
            }
            else {
                oldRow = row;
                oldCol = col;
                for (int r = 0; r < Chess.SIDE; r++) {
                    for (int c = 0; c < Chess.SIDE; c++) {
                        if (possibleMoves[r][c] == 2 || possibleMoves[r][c]==4|| possibleMoves[r][c]==5) {
                            buttons[r][c].setBackground(getDrawable(R.drawable.green_box));
                        }
                        if(possibleMoves[r][c]==3 || (possibleMoves[r][c]==7&&whitePassant[c]==1&&chessGame.getTurn()==1||possibleMoves[r][c]==7&blackPassant[c]==1&&chessGame.getTurn()==2))
                            buttons[r][c].setBackground(getDrawable(R.drawable.red_box));


                    }
                }
            }
            currentGreen = possibleMoves;
        }
        else {
            if(currentGreen!=null &&oldRow>=0 &&oldCol>=0) {
                if (currentGreen[row][col] == 2) {
                    resetBackgrounds();
                    if(oldRow==1 && row ==3 && game[oldRow][oldCol]==1 &&whitePassant[col]==0) {
                        whitePassant[col]=1;
                    }
                    if(oldRow==6 && row ==4  && game[oldRow][oldCol]==7&&blackPassant[col]==0) {
                        blackPassant[col]=1;
                    }
                    game[row][col] = game[oldRow][oldCol];
                    game[oldRow][oldCol] = 0;
                    movePieces(row, col);
                    movePieces(oldRow, oldCol);

                    chessGame.updateTurn();
                    currentGreen = possibleMoves;
                    if((game[row][col] == 2&&oldCol==0) || game[row][col] ==6)
                        chessGame.updateBlackCastleLeft(false);
                    if((game[row][col] == 2&&oldCol==Chess.SIDE-1) || game[row][col] ==6)
                        chessGame.updateBlackCastleRight(false);
                    if((game[row][col] == 2&&oldCol==0) || game[row][col] ==12)
                        chessGame.updateWhiteCastleLeft(false);
                    if((game[row][col] == 2&&oldCol==Chess.SIDE-1) || game[row][col] ==12)
                        chessGame.updateWhiteCastleRight(false);
                }
                if (currentGreen[row][col] == 3) {
                    resetBackgrounds();
                    game[row][col] = game[oldRow][oldCol];
                    game[oldRow][oldCol] = 0;
                    movePieces(row, col);
                    movePieces(oldRow, oldCol);
                    currentGreen = possibleMoves;

                    chessGame.updateTurn();
                    if((game[row][col] == 2&&oldCol==0) || game[row][col] ==6)
                        chessGame.updateBlackCastleLeft(false);
                    if((game[row][col] == 2&&oldCol==Chess.SIDE-1) || game[row][col] ==6)
                        chessGame.updateBlackCastleRight(false);
                    if((game[row][col] == 2&&oldCol==0) || game[row][col] ==12)
                        chessGame.updateWhiteCastleLeft(false);
                    if((game[row][col] == 2&&oldCol==Chess.SIDE-1) || game[row][col] ==12)
                        chessGame.updateWhiteCastleRight(false);
                }
                if (currentGreen[row][col] == 4) {
                    resetBackgrounds();
                    game[row][6] = game[row][4];
                    game[row][5] = game[row][4]==6?2:8;
                    game[row][4]=0;
                    game[row][Chess.SIDE-1]=0;
                    movePieces(row, 6);
                    movePieces(row,5);
                    movePieces(row, 4);
                    movePieces(row, Chess.SIDE-1);
                    currentGreen = possibleMoves;
                    if(chessGame.getTurn()==1){
                        chessGame.updateWhiteCastleLeft(false);
                        chessGame.updateWhiteCastleRight(false);
                    }
                    else{
                        chessGame.updateBlackCastleLeft(false);
                        chessGame.updateBlackCastleRight(false);
                    }
                    chessGame.updateTurn();
                }
                if (currentGreen[row][col] == 5) {
                    resetBackgrounds();
                    game[row][2] = game[row][4];
                    game[row][3] = game[row][4]==6?2:8;
                    game[row][4]=0;
                    game[row][0]=0;
                    movePieces(row, 2);
                    movePieces(row,3);
                    movePieces(row, 4);
                    movePieces(row,0);
                    currentGreen = possibleMoves;
                    if(chessGame.getTurn()==1){
                        chessGame.updateWhiteCastleLeft(false);
                        chessGame.updateWhiteCastleRight(false);
                    }
                    else{
                        chessGame.updateBlackCastleLeft(false);
                        chessGame.updateBlackCastleRight(false);
                    }
                    chessGame.updateTurn();
                }
                if (currentGreen[row][col] == 7 && ((chessGame.getTurn()==1 && whitePassant[col]==1) || (chessGame.getTurn()==2 && blackPassant[col]==1))) {
                    resetBackgrounds();
                    game[row][col] = game[oldRow][oldCol];
                    game[oldRow][oldCol] = 0;
                    game[oldRow][col]=0;
                    movePieces(row, col);
                    movePieces(oldRow, oldCol);
                    movePieces(oldRow,col);
                    currentGreen = possibleMoves;
                    if(chessGame.getTurn()==1)
                        whitePassant[col]=2;
                    else
                        blackPassant[col]=2;
                    chessGame.updateTurn();
                }
            }
        }
        if(game[row][col]==1&&row==7){
            game = chessGame.upgradeBlackPawn(row,col);
            movePieces(row,col);
        }
        if(game[row][col]==7&&row==0){
            game = chessGame.upgradeWhitePawn(row,col);
            movePieces(row,col);
        }
        if(chessGame.whoWon()!=0){
            enableButtons(false);
            showNewGameDialog();
            if(chessGame.whoWon()==1)
                blackWins++;
            if(chessGame.whoWon()==2)
                whiteWins++;
            //scorekeeper.setText("Black Wins:" +blackWins+"\n"+"White Wins"+whiteWins);
        }
        status.setText(chessGame.result()+"\n"+"Black Wins: " +blackWins+"\n"+"White Wins: "+whiteWins);
        if( chessGame.whoWon()==1){
            pawn1.setForeground(getDrawable(R.mipmap.black_pawn));
            pawn2.setForeground(getDrawable(R.mipmap.black_pawn));
        }
        else if(chessGame.whoWon()==2){
            pawn1.setForeground(getDrawable(R.mipmap.white_pawn));
            pawn2.setForeground(getDrawable(R.mipmap.white_pawn));
        }
        else if (chessGame.getTurn()==2){
            pawn1.setForeground(getDrawable(R.mipmap.black_pawn));
            pawn2.setForeground(getDrawable(R.mipmap.black_pawn));
        }
        else {
            pawn1.setForeground(getDrawable(R.mipmap.white_pawn));
            pawn2.setForeground(getDrawable(R.mipmap.white_pawn));
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void resetButtons(){
        for (int row = 0; row < Chess.SIDE; row++)
            for (int col = 0; col < Chess.SIDE; col++)
                movePieces(row,col);


    }

    public void enableButtons(boolean enabled){
        for(int r=0;r<Chess.SIDE;r++)
            for(int c=0; c<Chess.SIDE;c++)
                buttons[r][c].setEnabled(enabled);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void resetBackgrounds(){
        int i = 0;
        int p = 1;
        for (int r = 0; r < Chess.SIDE - 1; r += 2) {
            for (int c = 0; c < Chess.SIDE; c++) {
                buttons[r + i][c].setBackground(getDrawable(R.drawable.white_box));
                buttons[r + p][c].setBackground(getDrawable(R.drawable.black_box));
                i = i == 0 ? 1 : 0;
                p = p == 0 ? 1 : 0;
            }
        }
    }
    public void showNewGameDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(chessGame.result());
        alert.setMessage("Play again?");
        PlayDialog playAgain = new PlayDialog();
        alert.setPositiveButton("YES",playAgain);
        alert.setNegativeButton("NO",playAgain);
        alert.show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void movePieces(int row, int col){
       if (game[row][col] == 1)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_pawn));
        if (game[row][col] == 2)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_castle));
        if (game[row][col] == 3)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_horse));
        if (game[row][col] == 4)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_bishop));
        if (game[row][col] == 5)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_queen));
        if (game[row][col] == 6)
            buttons[row][col].setForeground(getDrawable(R.mipmap.black_king));
        if (game[row][col] == 7)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_pawn));
        if (game[row][col] == 8)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_castle));
        if (game[row][col] == 9)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_horse));
        if (game[row][col] == 10)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_bishop));
        if (game[row][col] == 11)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_queen));
        if (game[row][col] == 12)
            buttons[row][col].setForeground(getDrawable(R.mipmap.white_king));
        if (game[row][col] == 0) {
            buttons[row][col].setForeground(null);
        }
    }
    private class ButtonHandler implements View.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onClick(View v) {
            for (int row = 0; row < Chess.SIDE; row++)
                for (int column = 0; column < Chess.SIDE; column++)
                    if (v == buttons[row][column]) {
                        update(row, column);
                    }
        }
    }
    private class PlayDialog implements DialogInterface.OnClickListener {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onClick (DialogInterface dialog, int id){
            if(id==-1) {
                chessGame.resetGame();
                enableButtons (true);
                resetButtons();
                status.setText(chessGame.result()+"\n"+"Black Wins: " +blackWins+"\n"+"White Wins: "+whiteWins);
                pawn1.setForeground(getDrawable(R.mipmap.white_pawn));
                pawn2.setForeground(getDrawable(R.mipmap.white_pawn));
            }
            else if(id==-2)
                MainActivity.this.finish();
        }
    }
}