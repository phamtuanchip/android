package com.thanhgiong.note8;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NoteActivity extends Activity {
	public static int ACTION_TYPE_ADDNEW = 3;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_VIEW = 1;
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS note8tb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB, ntime varchar(5) )";
	int current_action;

	ImageButton del;
	ImageButton edit;
	ImageButton home;
	ImageButton add;
	ImageButton cancel;
	ImageButton save;
	ImageButton lock;
	ImageButton sw;

	ImageView img;
	ImageView img_frame;
	String image;
	LinearLayout l1;
	Note n_;
	TextView remindT;
	TextView whatT;
	TextView whenT;
	TextView whereT;

	CheckBox reminder;
	TextView remindTime;
	EditText whatE;
	ImageView whenI;
	EditText whenE;
	ImageView whereI;
	EditText whereE;
	PendingIntent pendingIntent;

	MyReceiver mr = new MyReceiver();

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		IntentFilter ift = new IntentFilter();
		Intent intent = new Intent(getApplicationContext(), MyReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	protected static boolean isVisible = false;

	@Override
	public void onResume()
	{
		super.onResume();
		setVisible(true);
		Log.e("Currrent -=========",this.getClass().toString()+ isVisible);
	}


	@Override
	public void onPause()
	{
		super.onPause();
		setVisible(false);
		Log.e("pouse -=========",this.getClass().toString());
	}



}
