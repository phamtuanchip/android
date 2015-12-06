package com.thanhgiong.note8;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class HomeActivity extends Activity implements OnClickListener {
	public final static String EXTRA_MESSAGE = "MESSAGE";
	private ListView list;
	public static List<Note> data_;
	ImageButton add;
	ImageButton sw;
	ImageButton lock;
	SharedPreferences settings;
	private PendingIntent pendingIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		sw = (ImageButton) findViewById(R.id.btnSwitch);
		lock = (ImageButton) findViewById(R.id.btnLock);

		add.setVisibility(View.VISIBLE);
		sw.setVisibility(View.VISIBLE);
		lock.setVisibility(View.VISIBLE);

		add.setOnClickListener(this);
		sw.setOnClickListener(this);
		lock.setOnClickListener(this);

		list = (ListView) findViewById(R.id.listView);
		settings = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
		String viewType = settings.getString(LoginActivity.PREFS_VIEW, null);
		if (viewType == null) {
			list.setAdapter(new NoteAdapter(this, getData()));
		} else {
			if ("list".equals(viewType)) {
				list.setAdapter(new NoteListAdapter(this, getData()));
			} else {
				list.setAdapter(new NoteAdapter(this, getData()));
			}
		}
		
		Calendar calendar = Calendar.getInstance();
	     
	      calendar.set(Calendar.MONTH, 6);
	      calendar.set(Calendar.YEAR, 2013);
	      calendar.set(Calendar.DAY_OF_MONTH, 13);
	 
	      calendar.set(Calendar.HOUR_OF_DAY, 20);
	      calendar.set(Calendar.MINUTE, 48);
	      calendar.set(Calendar.SECOND, 0);
	      calendar.set(Calendar.AM_PM,Calendar.PM);
	     
	      Intent myIntent = new Intent(HomeActivity.this, MyReceiver.class);
	      pendingIntent = PendingIntent.getBroadcast(HomeActivity.this, 0, myIntent,0);
	     
	      AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
	      alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
	    


	}

	private List<Note> getData() {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb", null);
		data_ = new ArrayList<Note>();
		if (cs.moveToFirst()) {
			do {
				Note n = new Note(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6));
				data_.add(n);
			} while (cs.moveToNext());
		}
		db.close();
		return data_;
	}

	private List<Note> searchData(String key) {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb where nwhat like '?'", new String[] { key });
		data_ = new ArrayList<Note>();
		if (cs.isBeforeFirst()) {
			do {
				Note n = new Note(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6));
				data_.add(n);
			} while (cs.moveToNext());
		}
		db.close();
		return data_;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnAddnew: {
			Intent i = new Intent(this, NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
		break;
		case R.id.btnSwitch: {
			Toast.makeText(this, "Change view", Toast.LENGTH_SHORT).show();
			list = (ListView) findViewById(R.id.listView);
			SharedPreferences.Editor editor = settings.edit();
			if (list.getAdapter() instanceof NoteAdapter) {
				list.setAdapter(new NoteListAdapter(this, getData()));
				editor.putString(LoginActivity.PREFS_VIEW, "list");
			} else {
				list.setAdapter(new NoteAdapter(this, getData()));
				editor.putString(LoginActivity.PREFS_VIEW, "block");
			}
			editor.commit();

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
}