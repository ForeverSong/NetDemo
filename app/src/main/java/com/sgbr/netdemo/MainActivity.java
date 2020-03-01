package com.sgbr.netdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyOnClick myOnClick = new MyOnClick();
        Button btnPeople = findViewById(R.id.btn_people);
        Button btnNet = findViewById(R.id.btn_net);

        btnPeople.setOnClickListener(myOnClick);
        btnNet.setOnClickListener(myOnClick);
    }

    class MyOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                // 1、现场版
                case R.id.btn_people:
                    intent = new Intent(MainActivity.this, PeopleActivity.class);
                    break;

                // 2、网络版
                case R.id.btn_net:
                    intent = new Intent(MainActivity.this, NetActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

}
