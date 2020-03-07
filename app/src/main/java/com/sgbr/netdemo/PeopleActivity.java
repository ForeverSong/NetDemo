package com.sgbr.netdemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.sgbr.netdemo.myview.ChessView;

public class PeopleActivity extends AppCompatActivity {


    private ChessView myChessView;
    private Button btnRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        myChessView = findViewById(R.id.chess_view);
        btnRestart = findViewById(R.id.restart);
        btnRestart.setOnClickListener(v -> myChessView.reStart());
    }
}
