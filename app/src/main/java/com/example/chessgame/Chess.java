package com.example.chessgame;

public class Chess {
    public static final int SIDE = 8;
    private int turn;
    private Piece piece1;
    private int [][] game;
    public Chess() {
        game = new int[SIDE][SIDE];
        resetGame();
    }
    public int[][] play(int row, int col, int piece) {
        if (row >=0 && col>=0 &&row<SIDE &&col<SIDE && game[row][col] !=0)
        {
            piece1 = new Piece(row, col, piece, game);
            if(turn==1) {

                if(piece>6){
                    return piece1.possibleMoves();
                }


            }
            else{

                if(piece<=6&&piece>0){
                    return piece1.possibleMoves();
                }


            }


        }
        return new int[SIDE][SIDE];
    }
    public int[][] getGame() {
        return game;
    }
    public int[][] upgradeBlackPawn(int row, int col){
        game[row][col]=5;
        return game;
    }
    public int[][] upgradeWhitePawn(int row, int col){
        game[row][col]=11;
        return game;
    }
    public int updateTurn(){
        int currentTurn = turn;
        if(turn==1)
            turn=2;
        else
            turn=1;
        return currentTurn;
    }
    public int whoWon(){
        int winner =0;
        for(int r=0;r<Chess.SIDE;r++)
            for(int c=0;c<Chess.SIDE;c++){
                if(game[r][c]==6){
                    winner = 2;
                    break;
                }
            }
        if(winner!=2)
            return 2;
        winner =0;
        for(int r=0;r<Chess.SIDE;r++)
            for(int c=0;c<Chess.SIDE;c++){
                if(game[r][c]==12){
                    winner = 1;
                    break;
                }
            }
        if(winner!=1)
            return 1;
        return 0;
    }
    public String result(){
       if(whoWon() ==1)
           return "Black won";
        if(whoWon()==2)
            return "White won";
        else if(turn ==1)
            return "White's turn";
        else if(turn==2)
            return "Black's turn";
        else
            return "If you're seeing this, something's gone wrong.";
    }
    public void resetGame() {
        for (int row = 0; row < SIDE; row++) {
            if(row==0) {
                game[row][0] = 2;
                game[row][1] = 3;
                game[row][2] = 4;
                game[row][3] = 5;
                game[row][4] = 6;
                game[row][5] = 4;
                game[row][6] = 3;
                game[row][7] = 2;
            }
            else if (row == 7) {
                game[row][0] = 8;
                game[row][1] = 9;
                game[row][2] = 10;
                game[row][3] = 11;
                game[row][4] = 12;
                game[row][5] = 10;
                game[row][6] = 9;
                game[row][7] = 8;
            }
            else {
                for (int col = 0; col<SIDE;col++) {
                    if (row==1)
                        game[row][col] = 1;
                    else if (row==6)
                        game[row][col] = 7;
                    else
                        game[row][col] = 0;
                }
            }
        }
        turn = 1;
    }
    public int getTurn(){
        return turn;
    }
    public void updateBlackCastleRight(boolean castle){
        piece1.updateBlackCastleRight(castle);
    }
    public void updateBlackCastleLeft(boolean castle){
        piece1.updateBlackCastleLeft(castle);
    }
    public void updateWhiteCastleRight(boolean castle){
        piece1.updateWhiteCastleRight(castle);
    }
    public void updateWhiteCastleLeft(boolean castle){
        piece1.updateWhiteCastleLeft(castle);
    }
}
