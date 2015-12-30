package com.thanhgiong.member;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	private View mLoginFormView;
	private EditText mIdView;
	private EditText mPasswordView;
	private Button signInButton;
	private String uid;
	private String upwd;
	
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS membertb (id integer primary key autoincrement, name varchar(125), dob varchar(30), add varchar(125), gt varchar(10), nimage varchar (125), nbinary BLOB)";
	public static String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS usertb (id integer primary key autoincrement, uid varchar(30), upwd varchar(30))";
	public void attemptLogin() {
		Intent i = new Intent(this, HomeActivity.class);			 
		startActivity(i);
		
//		if(uid.equalsIgnoreCase(mIdView.getText().toString()) && upwd.equalsIgnoreCase(mPasswordView.getText().toString())) {
//			
//		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SQLiteDatabase db = openOrCreateDatabase("usertb", MODE_PRIVATE, null);
		db.execSQL(CREATE_TABLE_USER);
		Cursor cs = db.rawQuery("SELECT * FROM usertb", null);
		if(cs.moveToNext()) {
		uid =	cs.getString(1);
		upwd = 	cs.getString(2);
		} else {			
		ContentValues values = new ContentValues();
		values.put("uid", "admin");
		values.put("upwd", "12345");
		db.insert("usertb", null, values);
		}
		db.close();

		mLoginFormView = findViewById(R.id.login_form);
		mLoginFormView.setVisibility(View.VISIBLE);
		mPasswordView = (EditText) findViewById(R.id.password);
		signInButton = (Button) findViewById(R.id.sign_in_button);
		signInButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				attemptLogin();
			}
		});

	}




}
