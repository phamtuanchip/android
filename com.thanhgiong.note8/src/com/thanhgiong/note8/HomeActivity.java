package com.thanhgiong.note8;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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

public class HomeActivity extends Activity implements OnClickListener, SearchView.OnQueryTextListener {
	public static List<Note> data_;
	public final static String EXTRA_MESSAGE = "MESSAGE";
	ImageButton add;
	ListView list;
	ImageButton lock;
	SearchView search;
	SharedPreferences settings;
	ImageButton sw;

	// Hàm load dữ liệu đã lưu trong cơ sở dữ liệu SQLite
	private List<Note> getData() {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb", null);
		data_ = new ArrayList<Note>();
		if (cs.moveToFirst()) {
			do {
				// Duyệt qua toàn bộ doanh sách, tạo đối tượng để hiện dữ liệu
				// trong list view
				Note n = new Note(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6), cs.getString(7));
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
		// Cập nhật sự kiện chuyển dạng view
		case R.id.btnSwitch: {
			Toast.makeText(this, "Change view", Toast.LENGTH_SHORT).show();
			list = (ListView) findViewById(R.id.listView);
			SharedPreferences.Editor editor = settings.edit();
			if (list.getAdapter() instanceof NoteAdapter) {
				list.setAdapter(new NoteListAdapter(this, getData()));
				// Lưu dạng view vào sharedpreference
				editor.putString(LoginActivity.PREFS_VIEW, "list");
			} else {
				list.setAdapter(new NoteAdapter(this, getData()));
				// Lưu dạng view vào sharedpreference
				editor.putString(LoginActivity.PREFS_VIEW, "block");
			}
			// save dữ liệu vào sharedpreference
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
		// Kiểm tra kiểu view đã lưu lần trước
		settings = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
		String viewType = settings.getString(LoginActivity.PREFS_VIEW, null);
		// Cài đặt filter để search trên list view
		list.setTextFilterEnabled(true);
		if (viewType == null) {
			// Nếu chưa lưu kiểu view lần nào thì chọn view theo kiểu khối
			list.setAdapter(new NoteAdapter(this, getData()));
		} else {
			// Nếu đã lưu kiểu view lần nào thì chọn view tương ứng kiểu view đã
			// lưu
			if ("list".equals(viewType)) {
				// view theo dạng list
				list.setAdapter(new NoteListAdapter(this, getData()));
			} else {
				// view theo dạng khối
				list.setAdapter(new NoteAdapter(this, getData()));
			}
		}

	}

	// Hàm filter để thực hiện search trên list view đã có
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