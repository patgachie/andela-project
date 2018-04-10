package com.example.android.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThreeActivity extends AppCompatActivity implements View.OnClickListener{
    //the player making the click
    int player_turn = 1;

    //turn for computer to play
    int computer_player_turn = 2;

    //array to store clicks made
    int[][] played = {{0,0,0},{0,0,0},{0,0,0}};

    //array to store the textviews representing slots in our tictactoe game
    TextView[] textViews = new TextView[9];

    //textView for showing the player playing and who won
    TextView txtPlaying;

    //array to store possible marks
    String[] marks = {" ","X","O"};

    //playing against computer or human
    int playersNo = 1;

    //number of plays made
    int plays_count = 0;

    //Buttons
    Button btnOnePlayer;
    Button btnTwoPlayers;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3by3);

        //initialize the text views by relating them to the textviews in the layout
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                textViews[3*i+j] = (TextView) findViewById(getResources().getIdentifier("txtB"+Integer.toString(i)+Integer.toString(j), "id", getPackageName()));
                textViews[3*i+j].setOnClickListener(this);
            }
        }

        //initialize other views
        txtPlaying = (TextView) findViewById(R.id.txtPlaying);
        btnOnePlayer = (Button) findViewById(R.id.btn_one_player);
        btnTwoPlayers = (Button) findViewById(R.id.btn_two_player);
        btnReset = (Button) findViewById(R.id.btn_reset);

        txtPlaying.setText(getString(R.string.playing,marks[player_turn]));

        btnOnePlayer.setOnClickListener(this);
        btnTwoPlayers.setOnClickListener(this);
        btnReset.setOnClickListener(this);

        if(!MainActivity.playAsX){
            computer_player_turn = 1;
            computerPlay();
        }
    }

    @Override
    public void onClick(View view) {
        //get id of the clicked view as string
        String viewIDString = view.getResources().getResourceEntryName(view.getId());
        if(viewIDString.substring(0,4).contentEquals("txtB")){

            //player has started playing so disable number of players buttons
            btnOnePlayer.setEnabled(false);
            btnTwoPlayers.setEnabled(false);

            //get row of textview
            int i = Integer.parseInt(viewIDString.substring(4,5));

            //get column of textview
            int j = Integer.parseInt(viewIDString.substring(viewIDString.length() - 1));

            markBox(i,j);

            //mark the box as played and by who in the clicks array
            played[i][j] = player_turn;

            //increment number of plays made
            plays_count += 1;

            //check if a player has won
            int winner = 0;
            winner = checkBoardForWin();
            if(winner==3){
                endGame(getString(R.string.draw));
                saveWin(3);
            }
            else if(winner > 0){
                endGame(getString(R.string.won, marks[winner]));
                saveWin(winner);
            }
            else{
                showNextPlayer();
            }

            if(playersNo == 1 && player_turn==computer_player_turn){
                computerPlay();
            }

            //check if a player has won
            winner = 0;
            winner = checkBoardForWin();
            if(winner==3){
                endGame(getString(R.string.draw));
                saveWin(3);
            }
            else if(winner > 0){
                endGame(getString(R.string.won, marks[winner]));
                saveWin(winner);
            }
        }
        else {
            switch (view.getId()) {
                case R.id.btn_reset:
                    reset();
                    break;

                case R.id.btn_one_player:
                    btnOnePlayer.setText(getString(R.string.one_player));
                    btnTwoPlayers.setText(getString(R.string.change_to_two_players));
                    playersNo = 1;
                    break;

                case R.id.btn_two_player:
                    btnTwoPlayers.setText(getString(R.string.two_players));
                    btnOnePlayer.setText(getString(R.string.change_to_one_player));
                    playersNo = 2;
                    break;

                default:
                    break;
            }
        }
    }

    /**
     *
     */
    void markBox(int i, int j){
        //mark clicked textview with either X or O depending on the player playing
        textViews[3*i+j].setText(marks[player_turn]);

        //mark clicked textview as unclickable
        textViews[3*i+j].setClickable(false);
        textViews[3*i+j].setLongClickable(false);
    }

    /**
     *
     */
    void showNextPlayer(){
        //set next player as the one to play next i.e. if player = 1 set next player as 2 and vice versa
        //can also use 0 - player_turn + 3 instead of (player_turn % 2) + 1
        player_turn = (player_turn % 2) + 1;

        //show which player is playing next
        txtPlaying.setText(getString(R.string.playing,marks[player_turn]));
    }
    /**
     * Checks if any of the player has won by checking the played array,
     * if any has won, it returns the value of the winner
     * @param
     * @return the value of the winning player i.e. 1 for Player X and 2 for Player O
     * @see
     */
    private int checkBoardForWin(){
        int winner = 0;
        if((winner = checkRowsWin()) > 0){
            return winner;
        }
        else if((winner=checkColumnsWin()) > 0){
            return winner;
        }
        else if((winner=checkFirstDiagonalWin()) > 0){
            return winner;
        }
        else if((winner=checkSecondDiagonalWin()) > 0){
            return winner;
        }
        else if((winner=checkDraw()) > 0){
            return winner;
        }
        return winner;
    }

    /**
     * checks if any row has similar values
     * @return winner the value in the similar row
     */
    int checkRowsWin(){
        //check rows win
        int winner = 0;
        for(int i=0;i<3;i++){
            for(int j=1;j<3;j++){
                if(played[i][j-1] != played[i][j]){
                    winner = 0;
                    break;
                }
                if(j==2) {
                    winner = played[i][0];
                    return winner;
                }
            }
        }
        return winner;
    }

    /**
     * checks if any column has similar values
     * @return winner the value in the similar column
     */
    int checkColumnsWin(){
        //check columns win
        int winner = 0;
        for(int j=0;j<3;j++){
            for(int i=1;i<3;i++){
                if(played[i-1][j] != played[i][j]){
                    winner = 0;
                    break;
                }
                if(i==2) {
                    winner = played[0][j];
                    return winner;
                }
            }
        }
        return winner;
    }

    /**
     * checks if the first diagonal has similar values
     * @return winner the value in the first diagonal
     */
    int checkFirstDiagonalWin(){
        //check first diagonal for win
        int winner = 0;
        if (played[0][0] == played[1][1] && played[1][1] == played[2][2]) {
            winner = played[0][0];
        }
        return winner;
    }

    /**
     * checks if the second diagonal has similar values
     * @return winner the value in the second diagonal
     */
    int checkSecondDiagonalWin(){
        //check second diagonal for win
        int winner = 0;
        if (played[0][2] == played[1][1] && played[1][1] == played[2][0]) {
            winner = played[0][2];
        }
        return winner;
    }

    /**
     * checks if nine moves have been done
     * @return 3 if a draw is found and 0 if not
     */
    int checkDraw(){
        //check draw
        int winner = 0;
        if(plays_count == 9){
            winner = 3;
        }
        return winner;
    }

    /**
     * Ends game by displaying who has won or the draw,
     * Also, disables the boxes from being clicked
     * @param message
     * @return
     * @see
     */
    private void endGame(String message){
        txtPlaying.setText(message);
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                textViews[3*i+j].setClickable(false);
                textViews[3*i+j].setLongClickable(false);
            }
        }
    }

    /**
     * Resets all boxes to clickable and blank
     * resets the played array to all 0s
     * Enables selection of number of players
     * @param
     * @return
     * @see
     */
    private void reset(){
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                textViews[3*i+j].setText(marks[0]);
                textViews[3*i+j].setClickable(true);
                played[i][j] = 0;
            }
        }
        btnOnePlayer.setEnabled(true);
        btnTwoPlayers.setEnabled(true);
        player_turn = 1;
        plays_count = 0;
        txtPlaying.setText(getString(R.string.playing,marks[player_turn]));
        if(playersNo == 1 && computer_player_turn == 1){
            computerPlay();
        }
    }

    /**
     * calculates the best move for the computer
     * Marks the relevant box as played
     * @param
     * @return
     * @see
     */
    private void computerPlay(){
        //if 9 moves have been played, display Draw
        if(checkDraw() == 3){
            endGame(getString(R.string.draw));
            saveWin(3);
            return;
        }
        //loop through all boxes, if any box leads to a win for the computer, take it
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(played[i][j] == 0) {
                    played[i][j] = player_turn;
                    int winner = checkBoardForWin();
                    if (winner == player_turn) {
                        markBox(i,j);
                        endGame(getString(R.string.won, marks[winner]));
                        saveWin(winner);
                        return;
                    }
                    else if (winner==3){
                        markBox(i,j);
                        endGame(getString(R.string.draw));
                        saveWin(3);
                        return;
                    }
                    else {
                        played[i][j] = 0;
                    }
                }
            }
        }

        //loop through all boxes, if any box leads to a win for the opponent, block it by taking it
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(played[i][j] == 0) {
                    played[i][j] = (player_turn % 2) + 1;
                    int winner = checkBoardForWin();
                    if (winner == ((player_turn % 2) + 1)) {
                        played[i][j] = player_turn;
                        markBox(i,j);
                        plays_count += 1;
                        showNextPlayer();
                        return;
                    } else {
                        played[i][j] = 0;
                    }
                }
            }
        }

        //loop through all boxes, if any box leads to a fork for the computer, take it
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(played[i][j] == 0) {
                    played[i][j] = player_turn;
                    int fork = checkBoardForFork();
                    if (fork == player_turn) {
                        markBox(i,j);
                        plays_count += 1;
                        showNextPlayer();
                        return;
                    } else {
                        played[i][j] = 0;
                    }
                }
            }
        }

        //loop through all boxes, if any box leads to a fork for the opponent, block it by taking it
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(played[i][j] == 0) {
                    played[i][j] = (player_turn % 2) + 1;
                    int fork = checkBoardForFork();
                    if (fork == ((player_turn % 2) + 1)) {
                        played[i][j] = player_turn;
                        markBox(i,j);
                        plays_count += 1;
                        showNextPlayer();
                        return;
                    } else {
                        played[i][j] = 0;
                    }
                }
            }
        }


        //play center if it is blank
        if(played[1][1] == 0){
            played[1][1] = player_turn;
            markBox(1,1);
            plays_count += 1;
            showNextPlayer();
            return;
        }

        //play any corner if it is blank
        int[][] corners = {{0,0},{0,2},{2,0},{2,2}};
        for(int k=0;k<3;k++){
            if(played[corners[k][0]][corners[k][1]] == 0){
                played[corners[k][0]][corners[k][1]] = player_turn;
                markBox(corners[k][0],corners[k][1]);
                plays_count += 1;
                showNextPlayer();
                return;
            }
        }

        //play any center if it is blank
        int[][] centers = {{0,1},{1,0},{1,2},{2,1}};
        for(int k=0;k<3;k++){
            if(played[centers[k][0]][centers[k][1]] == 0){
                played[centers[k][0]][centers[k][1]] = player_turn;
                markBox(centers[k][0],centers[k][1]);
                plays_count += 1;
                showNextPlayer();
                return;
            }
        }
    }

    /**
     * Checks if any of the players has a fork,
     * @param
     * @return the value of the player with a fork i.e. 1 for Player X and 2 for Player O
     * @see
     */
    int checkBoardForFork(){
        int forksCount = 0;
        int playerWithFork = 0;
        int l =0;
        if ((l = checkRowsFork()) > 0){
            forksCount += 1;
            playerWithFork = l;
        }
        if ((l = checkColumnsFork()) > 0){
            forksCount += 1;
            playerWithFork = l;
        }
        if ((l = checkFirstDiagonalFork()) > 0){
            forksCount += 1;
            playerWithFork = l;
        }
        if ((l = checkSecondDiagonalFork()) > 0){
            forksCount += 1;
            playerWithFork = l;
        }
        if(forksCount > 1){
            return playerWithFork;
        }
        else{
            return 0;
        }
    }

    /**
     * checks if any row has 2 similar values and one blank
     * @return the value to be put to make row ***forky***
     */
    int checkRowsFork(){
        for(int i=0;i<3;i++){
            //check if any column in the row has a blank
            int blankFoundAtColumn = -1;
            for(int j=0;j<3;j++){
                if(played[i][j] == 0){
                    blankFoundAtColumn = j;
                    break;
                }
                if(j==2) {
                    break;
                }
            }

            if(blankFoundAtColumn >= 0){
                //array containing columns to check for equality depending on the blank column
                int[][] toCheck = {{1,2},{0,2},{0,1}};
                if(played[i][toCheck[blankFoundAtColumn][0]] == played[i][toCheck[blankFoundAtColumn][1]]){
                    return played[i][toCheck[blankFoundAtColumn][0]];
                }
                return 0;
            }
        }
        return 0;
    }

    /**
     * checks if any column has 2 similar values and one blank
     * @return the value to be put to make column ***forky***
     */
    int checkColumnsFork(){
        for(int j=0;j<3;j++){
            //check if any row in the column has a blank
            int blankFoundAtRow = -1;
            for(int i=0;i<3;i++){
                if(played[i][j] == 0){
                    blankFoundAtRow = i;
                    break;
                }
                if(i==2) {
                    break;
                }
            }

            if(blankFoundAtRow >= 0){
                //array containing columns to check for equality depending on the blank column
                int[][] toCheck = {{1,2},{0,2},{0,1}};
                if(played[toCheck[blankFoundAtRow][0]][j] == played[toCheck[blankFoundAtRow][0]][j]){
                    return played[toCheck[blankFoundAtRow][0]][j];
                }
                return 0;
            }
        }
        return 0;
    }

    /**
     * checks if the first diagonal has 2 similar values and one blank
     * @return the value to be put to make first diagonal ***forky***
     */
    int checkFirstDiagonalFork(){
        int[][] boxes = {{0,0},{1,1},{2,2}};
        int[][][] toCheck = {{{1,1},{2,2}},{{0,0},{2,2}},{{0,0},{1,1}}};

        //loop through the three boxes
        for(int k=0;k<3;k++){
            //check if box is empty
            if(played[boxes[k][0]][boxes[k][1]] == 0){
                if(played[toCheck[k][0][0]][toCheck[k][0][1]] == played[toCheck[k][1][0]][toCheck[k][1][1]]){
                    return played[toCheck[k][0][0]][toCheck[k][0][1]];
                }
            }
        }
        return 0;
    }

    /**
     * checks if the second diagonal has 2 similar values and one blank
     * @return the value to be put to make second diagonal ***forky***
     */
    int checkSecondDiagonalFork(){
        int[][] boxes = {{0,2},{1,1},{2,0}};
        int[][][] toCheck = {{{1,1},{2,0}},{{0,2},{2,0}},{{1,1},{2,0}}};

        //loop through the three boxes
        for(int k=0;k<3;k++){
            //check if box is empty
            if(played[boxes[k][0]][boxes[k][1]] == 0){
                if(played[toCheck[k][0][0]][toCheck[k][0][1]] == played[toCheck[k][1][0]][toCheck[k][1][1]]){
                    return played[toCheck[k][0][0]][toCheck[k][0][1]];
                }
            }
        }
        return 0;
    }

    /**
     * Saves data to the scoreboard
     * @param winner did x win, did O win or was it a draw?
     */
    void saveWin(int winner){
        Context context = getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.example.android.tictactoe", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        switch (winner) {
            case 3:
                int draws = sp.getInt("draws3X3", 0);
                draws += 1;
                editor.putInt("draws3X3", draws);
                break;
            case 2:
                int Owins = sp.getInt("Owins3X3", 0);
                Owins += 1;
                editor.putInt("Owins3X3", Owins);
                break;
            case 1:
                int Xwins = sp.getInt("Xwins3X3", 0);
                Xwins += 1;
                editor.putInt("Xwins3X3", Xwins);
                break;
            default:
                break;
        }
        editor.commit();
    }
}
