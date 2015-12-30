package com.thanhgiong.member;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

public class HomeActivity extends Activity {
	public static List<Member> data_;
	public final static String EXTRA_MESSAGE = "MESSAGE";
	public static int ACTION_TYPE_ADDNEW = 3;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_VIEW = 1;
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS membertb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB, ntime varchar(5) )";
	public static String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS usertb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB, ntime varchar(5) )";
	int current_action;
	ListView list;

	private List<Member> getData() {
		SQLiteDatabase db = openOrCreateDatabase("memberdb", MODE_PRIVATE, null);
		db.execSQL(CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM membertb", null);
		data_ = new ArrayList<Member>();

		if (cs.moveToFirst()) {
			do {
				Member n = new Member(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6));
				data_.add(n);
			} while (cs.moveToNext());
		}
		db.close();
		if (data_.size() == 0) {
			for (int i = 0; i < 10; i++) {
				data_.add(new Member("test"));
			}
		}
		return data_;

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new MemberListAdapter(this, getData()));
	}

}