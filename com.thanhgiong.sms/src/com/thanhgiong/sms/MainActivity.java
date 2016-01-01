package com.thanhgiong.sms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    Button sendSms;
    Button member;
    Button call;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gioithieu);
        member = (Button) findViewById(R.id.member);
        call= (Button) findViewById(R.id.call);
        sendSms = (Button) findViewById(R.id.sendSms);
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewSendSms = new Intent(MainActivity.this, sendSms.class);
                startActivity(viewSendSms);
            }
        });
        call.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent viewCall = new Intent(MainActivity.this, callPhone.class);
	                startActivity(viewCall);
				
			}
		});
        member.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 Intent viewMember = new Intent(MainActivity.this, infor.class);
	                startActivity(viewMember);
				
			}
		});
    }
}
