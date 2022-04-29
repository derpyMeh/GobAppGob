package com.example.gogobgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BestActivity extends AppCompatActivity {

    TextView tv_score,winStar;

    int lastScore;
    int starsWon;
    int best1, best2, best3;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_score = (TextView) findViewById(R.id.step_view);
        winStar = (TextView) findViewById(R.id.step_distance);

        //Load old scores
        SharedPreferences preferences = getSharedPreferences("PREFS", 0);
        lastScore = preferences.getInt("lastScore", 0);
        best1 = preferences.getInt("best1", 0);
        best2 = preferences.getInt("best2", 0);
        best3 = preferences.getInt("best3", 0);
        starsWon = preferences.getInt("starsWon",0);

        //Replaces highscore beats
        if(lastScore > best3){
            best3 = lastScore;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3",  best3);
            editor.apply();
        }
        if(lastScore > best2){
            int temp = best2;
            best2 = lastScore;
            best3 = temp;
           SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best3",  best3);
            editor.putInt("best2",  best2);
            editor.apply();
        }
        if(lastScore > best1){
            int temp = best1;
            best1 = lastScore;
            best2 = temp;
           SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("best2",  best2);
            editor.putInt("best1",  best1);
            editor.apply();
        }

        for(int starScore = 130;starScore<lastScore;starScore+=130){
            starsWon++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("starsWon", starsWon);
            editor.apply();
        }
//Display score
        tv_score.setText("LAST SCORE: "+ lastScore + "\n" +  "BEST1: " + best1 + "\n" +  "BEST2: " + best2 + "\n" +  "BEST3: " + best3);
        winStar.setText("Stars: "+starsWon);

    }
    @Override
    public void onBackPressed(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
}
