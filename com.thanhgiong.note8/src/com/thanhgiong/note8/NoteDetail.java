package com.thanhgiong.note8;

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
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetail extends NoteActivity implements OnClickListener, OnTouchListener {

	@Override
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
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_detail);
		Bundle b = this.getIntent().getExtras();
		l1 = (LinearLayout) findViewById(R.id.l2);
		img = (ImageView) findViewById(R.id.image);
		whatT = (TextView) findViewById(R.id.txtWhat);
		whenT = (TextView) findViewById(R.id.textwhen);
		whereT = (TextView) findViewById(R.id.textWhere);
		remindT = (TextView) findViewById(R.id.textRemind);
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");
		whatT.setTypeface(tf);
		whenT.setTypeface(tf);
		whereT.setTypeface(tf);
		remindT.setTypeface(tf);
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
				if (n_.remindTime != null && !n_.remindTime.isEmpty())
					remindT.setText(new StringBuffer(remindT.getText().toString()).append(n_.remindTime));
				if (Boolean.parseBoolean(n_.remind))
					remindT.setVisibility(View.VISIBLE);
				else
					remindT.setVisibility(View.GONE);
				whenT.setText(n_.getFormatedDate());
				whereT.setText(n_.where);
				whatT.setText(n_.what);
			}
		}

		edit = (ImageButton) findViewById(R.id.btnEdit);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		del = (ImageButton) findViewById(R.id.btnDel);
		lock = (ImageButton) findViewById(R.id.btnLock);
		home = (ImageButton) findViewById(R.id.btnHome);

		edit.setVisibility(View.VISIBLE);
		add.setVisibility(View.VISIBLE);
		del.setVisibility(View.VISIBLE);
		lock.setVisibility(View.VISIBLE);
		home.setVisibility(View.VISIBLE);

		edit.setOnClickListener(this);
		lock.setOnClickListener(this);
		add.setOnClickListener(this);
		home.setOnClickListener(this);
		del.setOnClickListener(this);
		l1.setOnTouchListener(this);
		current_action = ACTION_TYPE_VIEW;
	}

	// Overider ontouch event 
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

}
