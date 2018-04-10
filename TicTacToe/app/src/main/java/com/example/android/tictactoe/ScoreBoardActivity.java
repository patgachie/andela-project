package com.example.android.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class ScoreBoardActivity extends AppCompatActivity implements View.OnClickListener{
    //Button
    Button btnReset;

    //TextViews
    TextView threeby3Xwins;
    TextView threeby3Owins;
    TextView threeby3Draws;
    TextView fourby4Xwins;
    TextView fourby4Owins;
    TextView fourby4Draws;
    TextView fiveby5Xwins;
    TextView fiveby5Owins;
    TextView fiveby5Draws;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //initialize the text views by relating them to the textviews in the layout
        threeby3Xwins = (TextView) findViewById(R.id.threeby3Xwins);
        threeby3Owins = (TextView) findViewById(R.id.threeby3Owins);
        threeby3Draws = (TextView) findViewById(R.id.threeby3Draws);
        fourby4Xwins = (TextView) findViewById(R.id.fourby4Xwins);
        fourby4Owins = (TextView) findViewById(R.id.fourby4Owins);
        fourby4Draws = (TextView) findViewById(R.id.fourby4Draws);
        fiveby5Xwins = (TextView) findViewById(R.id.fiveby5Xwins);
        fiveby5Owins = (TextView) findViewById(R.id.fiveby5Owins);
        fiveby5Draws = (TextView) findViewById(R.id.fiveby5Draws);

        //initialize the button
        btnReset = (Button) findViewById(R.id.btn_reset_scoreboard);

        btnReset.setOnClickListener(this);

        Context context = getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.example.android.tictactoe", context.MODE_PRIVATE);
        int threeby3XwinsI = sp.getInt("Xwins3X3", 0);
        threeby3Xwins.setText(Integer.toString(threeby3XwinsI));
        int threeby3OwinsI = sp.getInt("Owins3X3", 0);
        threeby3Owins.setText(Integer.toString(threeby3OwinsI));
        int threeby3DrawsI = sp.getInt("draws3X3", 0);
        threeby3Draws.setText(Integer.toString(threeby3DrawsI));
        int fourby4XwinsI = sp.getInt("Xwins4X4", 0);
        fourby4Xwins.setText(Integer.toString(fourby4XwinsI));
        int fourby4OwinsI = sp.getInt("Owins4X4", 0);
        fourby4Owins.setText(Integer.toString(fourby4OwinsI));
        int fourby4DrawsI = sp.getInt("draws4X4", 0);
        fourby4Draws.setText(Integer.toString(fourby4DrawsI));
        int fiveby5XwinsI = sp.getInt("Xwins5X5", 0);
        fiveby5Xwins.setText(Integer.toString(fiveby5XwinsI));
        int fiveby5OwinsI = sp.getInt("Owins5X5", 0);
        fiveby5Owins.setText(Integer.toString(fiveby5OwinsI));
        int fiveby5DrawsI = sp.getInt("draws5X5", 0);
        fiveby5Draws.setText(Integer.toString(fiveby5DrawsI));
    }

    @Override
    public void onClick(View view) {
           switch (view.getId()) {
                case R.id.btn_reset_scoreboard:
                    reset();
                    break;

                default:
                    break;
            }
        }

    /**
     * Resets all scoreboard to 0
     * @param
     * @return
     * @see
     */
    private void reset(){
        Context context = getApplicationContext();
        SharedPreferences sp = context.getSharedPreferences("com.example.android.tictactoe", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("Xwins3X3", 0);
        threeby3Xwins.setText("0");
        editor.putInt("Owins3X3", 0);
        threeby3Owins.setText("0");
        editor.putInt("draws3X3", 0);
        threeby3Draws.setText("0");
        editor.putInt("Xwins4X4", 0);
        fourby4Xwins.setText("0");
        editor.putInt("Owins4X4", 0);
        fourby4Owins.setText("0");
        editor.putInt("draws4X4", 0);
        fourby4Draws.setText("0");
        editor.putInt("Xwins5X5", 0);
        fiveby5Xwins.setText("0");
        editor.putInt("Owins5X5", 0);
        fiveby5Owins.setText("0");
        editor.putInt("draws5X5", 0);
        fiveby5Draws.setText("0");
        editor.commit();
    }

}