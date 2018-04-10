package com.example.android.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class FiveActivity extends AppCompatActivity implements View.OnClickListener {
    //the player making the click
    int player_turn = 1;

    //turn for computer to play
    int computer_player_turn = 2;

    //array to store clicks made
    int[][] played = {{0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}, {0, 0, 0, 0, 0}};

    //array to store the textviews representing slots in our tictactoe game
    TextView[] textViews = new TextView[25];

    //textView for showing the player playing and who won
    TextView txtPlaying;

    //array to store possible marks
    String[] marks = {" ", "X", "O"};

    //playing against computer or human
    int playersNo = 1;

    //number of plays made
    int plays_count = 0;

    //address of best move during computer play
    int[] bestMove = new int[2];

    //Buttons
    Button btnOnePlayer;
    Button btnTwoPlayers;
    Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_5by5);

        //initialize the text views by relating them to the textviews in the layout
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                textViews[5 * i + j] = (TextView) findViewById(getResources().getIdentifier("txtB" + Integer.toString(i) + Integer.toString(j), "id", getPackageName()));
                textViews[5 * i + j].setOnClickListener(this);
            }
        }

        //initialize other views
        txtPlaying = (TextView) findViewById(R.id.txtPlaying);
        btnOnePlayer = (Button) findViewById(R.id.btn_one_player);
        btnTwoPlayers = (Button) findViewById(R.id.btn_two_player);
        btnReset = (Button) findViewById(R.id.btn_reset);

        txtPlaying.setText(getString(R.string.playing, marks[player_turn]));

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
        if (viewIDString.substring(0, 4).contentEquals("txtB")) {

            //player has started playing so disable number of players buttons
            btnOnePlayer.setEnabled(false);
            btnTwoPlayers.setEnabled(false);

            //get row of textview
            int i = Integer.parseInt(viewIDString.substring(4, 5));

            //get column of textview
            int j = Integer.parseInt(viewIDString.substring(viewIDString.length() - 1));

            markBox(i, j);

            //mark the box as played and by who in the clicks array
            played[i][j] = player_turn;

            //increment number of plays made
            plays_count += 1;

            //check if a player has won
            int winner = 0;
            winner = checkBoardForWin(played);
            if (winner == 3) {
                endGame(getString(R.string.draw));
                saveWin(winner);
            } else if (winner > 0) {
                endGame(getString(R.string.won, marks[winner]));
                saveWin(winner);
            } else {
                showNextPlayer();
            }

            if(playersNo == 1 && player_turn==computer_player_turn){
                computerPlay();
            }
        } else {
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
    void markBox(int i, int j) {
        //mark clicked textview with either X or O depending on the player playing
        textViews[5 * i + j].setText(marks[player_turn]);

        //mark clicked textview as unclickable
        textViews[5 * i + j].setClickable(false);
        textViews[5 * i + j].setLongClickable(false);
    }

    /**
     *
     */
    void showNextPlayer() {
        //set next player as the one to play next i.e. if player = 1 set next player as 2 and vice versa
        //can also use 0 - player_turn + 3 instead of (player_turn % 2) + 1
        player_turn = (player_turn % 2) + 1;

        //show which player is playing next
        txtPlaying.setText(getString(R.string.playing, marks[player_turn]));
    }

    /**
     * Checks if any of the player has won by checking the played array,
     * if any has won, it returns the value of the winner
     *
     * @param
     * @return the value of the winning player i.e. 1 for Player X and 2 for Player O
     * @see
     */
    private int checkBoardForWin(int[][] played) {
        int winner = 0;
        if ((winner = checkRowsWin(played)) > 0) {
            return winner;
        } else if ((winner = checkColumnsWin(played)) > 0) {
            return winner;
        } else if ((winner = checkDiagonalWin(played)) > 0) {
            return winner;
        } else if ((winner = checkDraw(played)) > 0) {
            return winner;
        }
        return winner;
    }

    /**
     * checks if any row has similar values
     *
     * @return winner the value in the similar row
     */
    int checkRowsWin(int[][] played) {
        //check rows win
        int winner = 0;
        for (int i = 0; i < 5; i++) {
            int matchedCount = 0;
            int matching = 0;
            for (int j = 1; j < 5; j++) {
                if (played[i][j - 1] != played[i][j]) {
                    matchedCount = 0;
                } else {
                    matchedCount = matchedCount < 1 ? matchedCount + 2 : matchedCount + 1;
                    matching = played[i][j];
                }
                if (matchedCount >= 4 && matching != 0) {
                    winner = matching;
                    return winner;
                } else {
                    winner = 0;
                }
            }
        }
        return winner;
    }

    /**
     * checks if any column has similar values
     *
     * @return winner the value in the similar column
     */
    int checkColumnsWin(int[][] played) {
        int winner = 0;
        for (int j = 0; j < 5; j++) {
            int matchedCount = 0;
            int matching = 0;
            for (int i = 1; i < 5; i++) {
                if (played[i - 1][j] != played[i][j]) {
                    matchedCount = 0;
                } else {
                    matchedCount = matchedCount < 1 ? matchedCount + 2 : matchedCount + 1;
                    matching = played[i][j];
                }
                if (matchedCount >= 4 && matching != 0) {
                    winner = matching;
                    return winner;
                } else {
                    winner = 0;
                }
            }
        }
        return winner;
    }

    /**
     * checks if any diagonal has 4 similar values
     * @return winner the value in the inning diagonal
     */
    int checkDiagonalWin(int[][] played) {
        int winner = 0;
        //create an array list containing all the possible spots that can form a diagonal
        ArrayList<Integer[][]> possibleSpots = new ArrayList<Integer[][]>();
        possibleSpots.add(new Integer[][]{{1, 0}, {2, 1}, {3, 2}, {4, 3}});
        possibleSpots.add(new Integer[][]{{0, 0}, {1, 1}, {2, 2}, {3, 3}, {4, 4}});
        possibleSpots.add(new Integer[][]{{0, 1}, {1, 2}, {2, 3}, {3, 4}});
        possibleSpots.add(new Integer[][]{{0, 3}, {1, 2}, {2, 1}, {3, 0}});
        possibleSpots.add(new Integer[][]{{0, 4}, {1, 3}, {2, 2}, {3, 1}, {4, 0}});
        possibleSpots.add(new Integer[][]{{1, 4}, {2, 3}, {3, 2}, {4, 1}});

        Iterator it = possibleSpots.iterator();
        while (it.hasNext()) {
            Integer[][] possibleDiagonal = (Integer[][]) it.next();
            int matchedCount = 0;
            int matching = 0;
            for (int k = 1; k < possibleDiagonal.length; k++) {
                if (played[possibleDiagonal[k - 1][0]][possibleDiagonal[k - 1][1]] != played[possibleDiagonal[k][0]][possibleDiagonal[k][1]]) {
                    matchedCount = 0;
                } else {
                    matchedCount = matchedCount < 1 ? matchedCount + 2 : matchedCount + 1;
                    matching = played[possibleDiagonal[k][0]][possibleDiagonal[k][1]];
                }
                if (matchedCount >= 4 && matching != 0) {
                    winner = matching;
                    return winner;
                } else {
                    winner = 0;
                }
            }
        }
        return winner;
    }

    /**
     * checks if nine moves have been done
     *
     * @return 3 if a draw is found and 0 if not
     */
    int checkDraw(int[][] played) {
        //check draw
        int winner = 3;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (played[i][j] == 0) {
                    winner = 0;
                }
            }
        }
        return winner;
    }

    /**
     * Ends game by displaying who has won or the draw,
     * Also, disables the boxes from being clicked
     *
     * @param message
     * @return
     * @see
     */
    private void endGame(String message) {
        txtPlaying.setText(message);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                textViews[5 * i + j].setClickable(false);
                textViews[5 * i + j].setLongClickable(false);
            }
        }
    }

    /**
     * Resets all boxes to clickable and blank
     * resets the played array to all 0s
     * Enables selection of number of players
     *
     * @param
     * @return
     * @see
     */
    private void reset() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                textViews[5 * i + j].setText(marks[0]);
                textViews[5 * i + j].setClickable(true);
                played[i][j] = 0;
            }
        }
        btnOnePlayer.setEnabled(true);
        btnTwoPlayers.setEnabled(true);
        player_turn = 1;
        plays_count = 0;
        txtPlaying.setText(getString(R.string.playing, marks[player_turn]));
        if(playersNo == 1 && computer_player_turn == 1){
            computerPlay();
        }
    }

    /**
     * calculates the best move for the computer
     * Marks the relevant box as played
     *
     * @param
     * @return
     * @see
     */
    private void computerPlay() {
        //if 16 moves have been played, display Draw
        if (checkDraw(played) == 3) {
            endGame(getString(R.string.draw));
            saveWin(3);
            return;
        }

        /**if no draw, use minimax to calculate best move
         * Java arrays are passed by reference, therefore make a copy of the array
         */
        int[][] played2 = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                played2[i][j] = played[i][j];
            }
        }

        //call minimax function with the current board layout and the player whose turn it is to play
        minimax(played2, player_turn);

        //Mark the best move as played by the current player
        played[bestMove[0]][bestMove[1]] = player_turn;

        //check if we have a winner after the minimax move
        int winner = checkBoardForWin(played);

        markBox(bestMove[0], bestMove[1]);
        if (winner == player_turn) {
            endGame(getString(R.string.won, marks[winner]));
            saveWin(winner);
            return;
        } else if (winner == 3) {
            endGame(getString(R.string.draw));
            saveWin(winner);
            return;
        }
        plays_count += 1;
        showNextPlayer();
        return;
    }

    /**
     * minimax recursively calculates the best move for a player
     *
     * @param board  array containing the current status of the game
     * @param player integer indicating the current player
     * @return bestscore the score to be earned by the player if they make a certain move
     */
    int minimax(int[][] board, int player) {
        // check whether any player has won due to the most recent minimax move and return the value accordingly
        int winner = checkBoardForWin(board);
        if (winner == ((player_turn % 2) + 1)) {
            return -10;
        } else if (winner == player_turn) {
            return 10;
        } else if (winner == 3) {
            return 0;
        }

        // an array to store a move and its score
        ArrayList<Integer[]> moves = new ArrayList<Integer[]>();

        int loops = 0;
        // loop through all spots but consider available spots only
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (board[i][j] == 0) {
                    //array to contain current spot address and the score if the move will be made
                    Integer[] move = new Integer[3];
                    move[0] = i;
                    move[1] = j;

                    //set current slot as played by the current player
                    board[i][j] = player;

                    //Get the score
                    if (player == player_turn) {
                        int result = minimax(board, ((player_turn % 2) + 1));
                        move[2] = result;
                        if(result == 10 || result == -10 || result == 0){
                            j = 10;
                            i = 10;
                        }
                    } else {
                        int result = minimax(board, player_turn);
                        move[2] = result;
                        if(result == 10 || result == -10 || result == 0){
                            j = 10;
                            i = 10;
                        }
                    }
                    //board[i][j] = 0;
                    moves.add(move);
                }
            }
        }

        // if it is the computer's turn, loop over the moves and choose the move with the highest score
        int bestScore;
        if (player == player_turn) {
            bestScore = -5000;
            Iterator it = moves.iterator();
            while (it.hasNext()) {
                Integer[] move = new Integer[3];
                move = (Integer[]) it.next();
                if (move[2] > bestScore) {
                    bestScore = move[2];
                    bestMove[0] = move[0];
                    bestMove[1] = move[1];
                }
            }
            return bestScore;
        }
        // if it is the humans turn, loop over the moves and choose the move with the lowest score
        else {
            bestScore = 5000;
            Iterator it = moves.iterator();
            while (it.hasNext()) {
                Integer[] move = new Integer[3];
                move = (Integer[]) it.next();
                if (move[2] < bestScore) {
                    bestScore = move[2];
                    bestMove[0] = move[0];
                    bestMove[1] = move[1];
                }
            }
            return bestScore;
        }
        // return the best score
        //return bestScore;
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
                int draws = sp.getInt("draws5X5", 0);
                draws += 1;
                editor.putInt("draws5X5", draws);
                break;
            case 2:
                int Owins = sp.getInt("Owins5X5", 0);
                Owins += 1;
                editor.putInt("Owins5X5", Owins);
                break;
            case 1:
                int Xwins = sp.getInt("Xwins5X5", 0);
                Xwins += 1;
                editor.putInt("Xwins5X5", Xwins);
                break;
            default:
                break;
        }
        editor.commit();
    }
}