package com.thanhgiong.member;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener {
	public static List<Member> data_;
	public final static String EXTRA_MESSAGE = "MESSAGE";
	public static int ACTION_TYPE_ADDNEW = 3;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_VIEW = 1;
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS membertb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB, ntime varchar(5) )";
	public static String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS usertb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB, ntime varchar(5) )";
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
	Member n_;
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
	
	ListView list;

	// HÃ m load dá»¯ liá»‡u Ä‘Ã£ lÆ°u trong cÆ¡ sá»Ÿ dá»¯ liá»‡u SQLite
	private List<Member> getData() {
		SQLiteDatabase db = openOrCreateDatabase("memberdb", MODE_PRIVATE, null);
		db.execSQL(CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM membertb", null);
		data_ = new ArrayList<Member>();
		
		if (cs.moveToFirst()) {
			do {
				// Duyá»‡t qua toÃ n bá»™ doanh sÃ¡ch, táº¡o Ä‘á»‘i tÆ°á»£ng Ä‘á»ƒ hiá»‡n dá»¯ liá»‡u
				// trong list view
				Member n = new Member(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6), cs.getString(7));
				data_.add(n);
			} while (cs.moveToNext());
		}
		db.close();
		return data_;
		
	}

	// Xử lý sự kiện khi có touch vào các nút
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddnew: {
			Intent i = new Intent(this, MemberEdit.class);
			i.putExtra("type", ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
			break;
		  
		case R.id.btnLock: {
			Intent i = new Intent(this, LoginActivity.class);
			v.getContext().startActivity(i);
		}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		lock = (ImageButton) findViewById(R.id.btnLock);
		add.setOnClickListener(this);
		lock.setOnClickListener(this);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new MemberListAdapter(this, getData()));
	}

	  
 
}