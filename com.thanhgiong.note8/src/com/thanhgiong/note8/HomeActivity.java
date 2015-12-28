package com.thanhgiong.note8;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

public class HomeActivity extends NoteActivity implements OnClickListener, SearchView.OnQueryTextListener {
	public static List<Note> data_;
	public final static String EXTRA_MESSAGE = "MESSAGE";
	ListView list;
	SearchView search;
	SharedPreferences settings;

	// HÃ m load dá»¯ liá»‡u Ä‘Ã£ lÆ°u trong cÆ¡ sá»Ÿ dá»¯ liá»‡u SQLite
	private List<Note> getData() {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb", null);
		data_ = new ArrayList<Note>();
		if (cs.moveToFirst()) {
			do {
				// Duyá»‡t qua toÃ n bá»™ doanh sÃ¡ch, táº¡o Ä‘á»‘i tÆ°á»£ng
				// Ä‘á»ƒ hiá»‡n dá»¯ liá»‡u
				// trong list view
				Note n = new Note(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
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
			Intent i = new Intent(this, NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
			break;
		// Cáº­p nháº­t sá»± kiá»‡n chuyá»ƒn dáº¡ng view
		case R.id.btnSwitch: {
			Toast.makeText(this, "Change view", Toast.LENGTH_SHORT).show();
			list = (ListView) findViewById(R.id.listView);
			SharedPreferences.Editor editor = settings.edit();
			if (list.getAdapter() instanceof NoteAdapter) {
				list.setAdapter(new NoteListAdapter(this, getData()));
				// LÆ°u dáº¡ng view vÃ o sharedpreference
				editor.putString(LoginActivity.PREFS_VIEW, "list");
			} else {
				list.setAdapter(new NoteAdapter(this, getData()));
				// LÆ°u dáº¡ng view vÃ o sharedpreference
				editor.putString(LoginActivity.PREFS_VIEW, "block");
			}
			// save dá»¯ liá»‡u vÃ o sharedpreference
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		sw = (ImageButton) findViewById(R.id.btnSwitch);
		lock = (ImageButton) findViewById(R.id.btnLock);
		search = (SearchView) findViewById(R.id.searchView);
		add.setVisibility(View.VISIBLE);
		sw.setVisibility(View.VISIBLE);
		lock.setVisibility(View.VISIBLE);

		search.setOnQueryTextListener(this);
		add.setOnClickListener(this);
		sw.setOnClickListener(this);
		lock.setOnClickListener(this);

		list = (ListView) findViewById(R.id.listView);
		// Kiá»ƒm tra kiá»ƒu view Ä‘Ã£ lÆ°u láº§n trÆ°á»›c
		settings = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
		String viewType = settings.getString(LoginActivity.PREFS_VIEW, null);
		// CÃ i Ä‘áº·t filter Ä‘á»ƒ search trÃªn list view
		list.setTextFilterEnabled(true);
		if (viewType == null) {
			// Náº¿u chÆ°a lÆ°u kiá»ƒu view láº§n nÃ o thÃ¬ chá»�n view theo
			// kiá»ƒu khá»‘i
			list.setAdapter(new NoteAdapter(this, getData()));
		} else {
			// Náº¿u Ä‘Ã£ lÆ°u kiá»ƒu view láº§n nÃ o thÃ¬ chá»�n view tÆ°Æ¡ng
			// á»©ng kiá»ƒu view Ä‘Ã£
			// lÆ°u
			if ("list".equals(viewType)) {
				// view theo dáº¡ng list
				list.setAdapter(new NoteListAdapter(this, getData()));
			} else {
				// view theo dáº¡ng khá»‘i
				list.setAdapter(new NoteAdapter(this, getData()));
			}
		}

	}

	// HÃ m filter Ä‘á»ƒ thá»±c hiá»‡n search trÃªn list view Ä‘Ã£ cÃ³
	@Override
	public boolean onQueryTextChange(String key) {
		if (TextUtils.isEmpty(key)) {
			list.clearTextFilter();
		} else {
			list.setFilterText(key);
		}
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}
}