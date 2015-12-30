package com.thanhgiong.member;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomeActivity extends Activity {
	public static List<Member> data_;
	public final static String EXTRA_MESSAGE = "MESSAGE";
	public static int ACTION_TYPE_ADDNEW = 3;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_VIEW = 1;
	int current_action;
	ListView list;
	Button add;

	private List<Member> getData() {
		SQLiteDatabase db = openOrCreateDatabase(DbUtil.DB_NAME, MODE_PRIVATE, null);
		db.execSQL(DbUtil.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM " + DbUtil.TB_MEMBER_NAME, null);
		data_ = new ArrayList<Member>();

		if (cs.moveToFirst()) {
			do {
				Member n = new Member(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6));
				data_.add(n);
			} while (cs.moveToNext());
		}
		db.close();
		return data_;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new MemberListAdapter(this, getData()));
		add = (Button) findViewById(R.id.btnAdd);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MemberEdit.class);
				i.putExtra("type", ACTION_TYPE_ADDNEW);
				startActivity(i);
			}
		});
	}

}