package com.example.android.tictactoe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    //Buttons
    Button btn3by3;
    Button btn4by4;
    Button btn5by5;
    Button btnScoreBoard;

    //when playing against computer, playing as X means you start, not playing as X means computer starts
    public static boolean playAsX = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize buttons
        btn3by3 = (Button) findViewById(R.id.btn3by3);
        btn4by4 = (Button) findViewById(R.id.btn4by4);
        btn5by5 = (Button) findViewById(R.id.btn5by5);
        btnScoreBoard = (Button) findViewById(R.id.btnscoreboard);

        //set on click listener for the buttons
        btn3by3.setOnClickListener(this);
        btn4by4.setOnClickListener(this);
        btn5by5.setOnClickListener(this);
        btnScoreBoard.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn3by3:
                    Intent i = new Intent(getApplicationContext(), ThreeActivity.class);
                    startActivity(i);
                    break;

                case R.id.btn4by4:
                    Intent i2 = new Intent(getApplicationContext(), FourActivity.class);
                    startActivity(i2);
                    break;

                case R.id.btn5by5:
                    Intent i3 = new Intent(getApplicationContext(), FiveActivity.class);
                    startActivity(i3);
                    break;

                case R.id.btnscoreboard:
                    Intent i4 = new Intent(getApplicationContext(), ScoreBoardActivity.class);
                    startActivity(i4);
                    break;

                default:
                    break;
            }
    }

    public void playAsXClicked(View view) {
        playAsX = ((CheckBox) view).isChecked();
    }
}
