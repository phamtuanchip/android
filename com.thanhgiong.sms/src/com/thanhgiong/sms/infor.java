package com.thanhgiong.sms;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class infor extends Activity {

	Button gui ;
	Button huy ;
	EditText guiden;
	EditText noidung;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.danhsachtv);
		huy = (Button) findViewById(R.id.cancel);
		huy.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
	}

}
