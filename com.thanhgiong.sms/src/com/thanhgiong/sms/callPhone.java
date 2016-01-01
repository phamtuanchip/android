package com.thanhgiong.sms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class callPhone extends Activity {

	Button gui ;
	Button huy ;
	EditText guiden;
	EditText noidung;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call);
		gui = (Button) findViewById(R.id.send);
		huy = (Button) findViewById(R.id.cancel);
		guiden = (EditText) findViewById(R.id.guiden);
		gui.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(guiden.getText())){
            		guiden.setError("Số điện thoại cần phải có!");
            		guiden.requestFocus();
            		return;
            	}
				 Uri uri = Uri.parse("tel:"+guiden.getText().toString());
				startActivity(new  Intent( Intent.ACTION_CALL,   uri)); 
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
