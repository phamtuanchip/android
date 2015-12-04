package com.thanhgiong.note8;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NoteDetail extends Activity implements OnClickListener, OnDateSetListener {
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

	public static int ACTION_TYPE_VIEW = 1;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_ADDNEW = 3;
	int current_action;
	boolean isEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_detail);
		isEdit = false;
		n_ = (Note) this.getIntent().getExtras().get("note");

		img = (ImageView) findViewById(R.id.image);
		if (n_.binary != null) {
			Bitmap bm = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
			img.setImageBitmap(bm);
		} else
			img.setImageBitmap(BitmapFactory.decodeResource(getBaseContext().getResources(), R.drawable.bg_default));
		Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/angelina.ttf");
		what = (TextView) findViewById(R.id.txtTitle);
		when = (TextView) findViewById(R.id.textwhen);
		where = (TextView) findViewById(R.id.textWhere);

		what.setTypeface(tf);
		what.setText(n_.what);

		when.setTypeface(tf);
		when.setText(n_.getFormatedDate());

		where.setTypeface(tf);
		where.setText(n_.where);

		remind = (TextView) findViewById(R.id.textRemind);
		if (Boolean.parseBoolean(n_.remind))
			remind.setVisibility(View.VISIBLE);
		else
			remind.setVisibility(View.GONE);

		int displayButton[] = { View.VISIBLE, View.VISIBLE, View.GONE, View.GONE, View.GONE, View.VISIBLE,
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

		current_action = ACTION_TYPE_VIEW;
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textwhen: {
			DatePickerDialog dialog = new DatePickerDialog(v.getContext(), this, 2015, 11, 27);
			dialog.show();
		}
			break;
		case R.id.img: {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			startActivityForResult(takePictureIntent, 1);
		}
			break;
		case R.id.textWhere: {
			String uri = "geo:47.6,-122.3";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			if (isAppInstalled("com.google.android.apps.maps")) {
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		}
		case R.id.btnEdit: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("note", n_);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			this.fileList();
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnAddnew: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			this.fileList();
			v.getContext().startActivity(i);
			isEdit = false;
			current_action = ACTION_TYPE_ADDNEW;
		}
			break;
		case R.id.btnHome: {
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			this.fileList();
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnLock: {
			Intent i = new Intent(v.getContext(), LoginActivity.class);
			this.fileList();
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnDel: {
			SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
			db.delete("note8tb", "id= ?", new String[] { n_.id });
			db.close();
			Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
		}
			break;
		default:
			break;
		}

	}

	private boolean isAppInstalled(String uri) {
		PackageManager pm = getApplicationContext().getPackageManager();
		boolean app_installed = false;
		try {
			pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
			app_installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			app_installed = false;
		}
		return app_installed;
	}

	public void showMap(Uri geoLocation) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(geoLocation);
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}

	public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub

		when.setText(new StringBuilder()
				// Month is 0 based so add 1
				.append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" "));
	}

}
