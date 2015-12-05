package com.thanhgiong.note8;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetail extends Activity implements OnClickListener, OnTouchListener {
	Note n_;
	TextView what;
	TextView when;
	TextView where;
	TextView remind;
	ImageView img;
	ImageButton edit;
	ImageButton add;
	ImageButton del;
	ImageButton save;
	ImageButton sw;
	ImageButton lock;
	ImageButton home;
	LinearLayout l1;
	public static int ACTION_TYPE_VIEW = 1;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_ADDNEW = 3;
	int current_action;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_detail);
		Bundle b = this.getIntent().getExtras();
		l1 = (LinearLayout)findViewById(R.id.l2);
		img = (ImageView) findViewById(R.id.image);
		what = (TextView) findViewById(R.id.txtWhat);
		when = (TextView) findViewById(R.id.textwhen);
		where = (TextView) findViewById(R.id.textWhere);
		remind = (TextView) findViewById(R.id.textRemind);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");
		what.setTypeface(tf);
		when.setTypeface(tf);
		where.setTypeface(tf);
		remind.setTypeface(tf);
		if (b != null) {
			n_ = (Note) b.get("note");
			if (n_ != null) {
				if (n_.binary != null) {
					Bitmap bm = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
					img.setImageBitmap(bm);
				} else {
					Resources res = getResources();
					int id = R.drawable.bg_default;
					Bitmap bm = BitmapFactory.decodeResource(res, id);
					img.setImageBitmap(bm);
				}
				if (Boolean.parseBoolean(n_.remind))
					remind.setVisibility(View.VISIBLE);
				else
					remind.setVisibility(View.GONE);
				when.setText(n_.getFormatedDate());
				where.setText(n_.where);
				what.setText(n_.what);
			}
		}

		int displayButton[] = { View.VISIBLE, View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.VISIBLE,
				View.VISIBLE };
		edit = (ImageButton) findViewById(R.id.btnEdit);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		del = (ImageButton) findViewById(R.id.btnDel);
		save = (ImageButton) findViewById(R.id.btnSave);
		sw = (ImageButton) findViewById(R.id.btnSwitch);
		lock = (ImageButton) findViewById(R.id.btnLock);
		home = (ImageButton) findViewById(R.id.btnHome);

		edit.setVisibility(displayButton[0]);
		add.setVisibility(displayButton[1]);
		del.setVisibility(displayButton[2]);
		save.setVisibility(displayButton[3]);
		sw.setVisibility(displayButton[4]);
		lock.setVisibility(displayButton[5]);
		home.setVisibility(displayButton[6]);

		edit.setOnClickListener(this);
		lock.setOnClickListener(this);
		add.setOnClickListener(this);
		home.setOnClickListener(this);
		del.setOnClickListener(this);
		l1.setOnTouchListener(this);
		current_action = ACTION_TYPE_VIEW;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEdit: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("note", n_);
			i.putExtra("type", NoteEdit.ACTION_TYPE_EDIT);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnAddnew: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
			current_action = ACTION_TYPE_ADDNEW;
		}
			break;
		case R.id.btnHome: {
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnLock: {
			Intent i = new Intent(v.getContext(), LoginActivity.class);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnDel: {
			SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
			db.delete("note8tb", "id= ?", new String[] { n_.id });
			db.close();
			Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			v.getContext().startActivity(i);
		}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Intent i = new Intent(v.getContext(), NoteEdit.class);
		i.putExtra("note", n_);
		i.putExtra("type", NoteEdit.ACTION_TYPE_EDIT);
		v.getContext().startActivity(i);
		return false;
	}

}
