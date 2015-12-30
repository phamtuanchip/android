package com.thanhgiong.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button sendSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gioithieu);
        sendSms = (Button) findViewById(R.id.sendSms);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSendSms = new Intent(MainActivity.this, sendSms.class);
                startActivity(viewSendSms);
            }
        });
    }
}
