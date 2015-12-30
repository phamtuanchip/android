package com.thanhgiong.member;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private EditText uidE;
	private EditText upwdE;
	private Button signInButton;
	private String uid;
	private String upwd;

	public void attemptLogin() {
		Intent i = new Intent(this, HomeActivity.class);
		// startActivity(i);
		if (TextUtils.isEmpty(uidE.getText())) {
			uidE.setError("user name is requirded!");
			uidE.requestFocus();
			return;
		}
		if (TextUtils.isEmpty(upwdE.getText())) {
			upwdE.setError("password is requirded!");
			upwdE.requestFocus();
			return;
		}
		if (uid.equalsIgnoreCase(uidE.getText().toString()) && upwd.equalsIgnoreCase(upwdE.getText().toString())) {
			startActivity(i);
		} else {
			Toast.makeText(this, "User name owr passowrd is incorrect", Toast.LENGTH_SHORT).show();
			return;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		uidE = (EditText) findViewById(R.id.uid);
		upwdE = (EditText) findViewById(R.id.password);
		signInButton = (Button) findViewById(R.id.sign_in_button);
		SQLiteDatabase db = openOrCreateDatabase(DbUtil.DB_NAME, MODE_PRIVATE, null);
		db.execSQL(DbUtil.CREATE_TABLE_USER);
		Cursor cs = db.rawQuery("SELECT * FROM " + DbUtil.TB_USER_NAME, null);
		if (cs.moveToNext()) {
			uid = cs.getString(1);
			upwd = cs.getString(2);
		} else {
			ContentValues values = new ContentValues();
			values.put("uid", "admin");
			values.put("upwd", "12345");
			db.insert("usertb", null, values);
		}
		db.close();
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

	}

}
