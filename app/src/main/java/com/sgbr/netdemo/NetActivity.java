package com.sgbr.netdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class NetActivity extends AppCompatActivity {
    private Switch mSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net);

        MyOnClick myOnClick = new MyOnClick();
        Button btnServer = findViewById(R.id.btn_server);
        Button btnClient = findViewById(R.id.btn_client);

        mSwitch = findViewById(R.id.switch_test);

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSwitch.setText("允许被发现");
                } else {
                    mSwitch.setText("不许被发现");
                }
            }
        });

        btnServer.setOnClickListener(myOnClick);
        btnClient.setOnClickListener(myOnClick);
    }

    class MyOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                // 1、服务器启动
                case R.id.btn_server:
                    intent = new Intent(NetActivity.this, FindClientActivity.class);
                    break;

                // 2、客户端
                case R.id.btn_client:
                    intent = new Intent(NetActivity.this, FindServerActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }
}
