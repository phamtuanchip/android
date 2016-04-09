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

	// Hàm lấy dữ liệu từ cơ sở dữ liệu sqlite và tạo danh sách đối tượng ghi chú
	private List<Note> getData() {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb", null);
		data_ = new ArrayList<Note>();
		if (cs.moveToFirst()) {
			do {
				// Duyệt qua toàn bộ kết quả của câu truy vấn và lấy giữ liệu từ các cộ tương ứng 
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
		// Thêm mới ghi chú mở form tạo ghi chú notedit activity
		case R.id.btnAddnew: {
			Intent i = new Intent(this, NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
			break;
		// Thay đổi các dạng view và load adapter tương ứng
		case R.id.btnSwitch: {
			Toast.makeText(this, "Change view", Toast.LENGTH_SHORT).show();
			list = (ListView) findViewById(R.id.listView);
			SharedPreferences.Editor editor = settings.edit();
			if (list.getAdapter() instanceof NoteAdapter) {
				list.setAdapter(new NoteListAdapter(this, getData()));
				// ghán giá trị vào biến số shared preferences
				editor.putString(LoginActivity.PREFS_VIEW, "list");
			} else {
				list.setAdapter(new NoteAdapter(this, getData()));
				// ghán giá trị vào biến số shared preferences
				editor.putString(LoginActivity.PREFS_VIEW, "block");
			}
			// lưu kiểu view tương ứng vào sharedprefernces
			editor.commit();
		}
			break;
			// Khóa ứng dụng quay về màn hình login
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
	// hàm load đầu tiên của mọi activity, lấy ra các object tương ứng gán vào biến số, cài đặt sự kiện
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
		search.setVisibility((data_ != null) && (data_.size() > 0) ? View.VISIBLE : View.GONE);
		add.setOnClickListener(this);
		sw.setOnClickListener(this);
		lock.setOnClickListener(this);

		list = (ListView) findViewById(R.id.listView);
		// kiếm tra giữ liệu đẫ lưu vào preferences hay chưa
		settings = getSharedPreferences(LoginActivity.PREFS_NAME, Context.MODE_PRIVATE);
		String viewType = settings.getString(LoginActivity.PREFS_VIEW, null);
		// lấy ra kiểu view đã lưu lần trước 
		list.setTextFilterEnabled(true);
		if (viewType == null) {
			// nếu chưa luu thì set sang dạng list view
			list.setAdapter(new NoteAdapter(this, getData()));
		} else {
			//  nếu đã lưu thì so sách giá trị
			if ("list".equals(viewType)) {
				// view theo dạng list
				list.setAdapter(new NoteListAdapter(this, getData()));
			} else {
				// view theo dạng khối
				list.setAdapter(new NoteAdapter(this, getData()));
			}
		}

	}

	// hàm xử lý sự kiên khi ghõ vào ô tìm kiếm sử dụng filter lọc dữ liệu hiện có trên danh sách nghi chú mà không cần gọi lại sqlite
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