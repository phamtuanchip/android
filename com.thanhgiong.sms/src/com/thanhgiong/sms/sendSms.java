package com.thanhgiong.sms;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sendSms extends Activity {

    Button gui ;
    Button huy ;
    EditText guiden;
    EditText noidung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sms);
        gui = (Button) findViewById(R.id.send);
        huy = (Button) findViewById(R.id.cancel);
        guiden = (EditText) findViewById(R.id.guiden);
        noidung = (EditText) findViewById(R.id.noidung);
        gui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	if(TextUtils.isEmpty(guiden.getText())){
            		guiden.setError("Số điện thoại cần phải có!");
            		guiden.requestFocus();
            		return;
            	}
            	if(TextUtils.isEmpty(noidung.getText())){
            		noidung.setError("Nội dung cần phải có!");
            		noidung.requestFocus();
            		return;
            	}
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(guiden.getText().toString(),null,noidung.getText().toString(),null,null);
            }
        });
        huy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
    }

}
