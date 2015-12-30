package com.thanhgiong.member;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MemberEdit extends Activity implements OnClickListener {

	ImageButton home;
	Bitmap mImageBitmap;
	String image;
	int current_action;

	Button cancel;
	Button save;

	ImageView img_frame;
	Member n_;
	TextView nameT;
	TextView dobT;
	TextView addT;
	TextView gtT;

	CheckBox reminder;
	TextView remindTime;

	EditText name;
	EditText dob;
	EditText add;
	RadioGroup gt;

	private File createImageFile() throws IOException {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getDataDirectory();
		File image = File.createTempFile(imageFileName, /* prefix */
				".jpg", /* suffix */
				storageDir /* directory */
		);
		this.image = "file:" + image.getAbsolutePath();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle extras = data.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		img_frame.setImageBitmap(mImageBitmap);
		Toast.makeText(this, "Snaped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.img: {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File f = null;
			try {
				f = createImageFile();
				if (f != null)
					takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			} catch (IOException e) {
				e.printStackTrace();
				f = null;
				image = null;
			}
			startActivityForResult(takePictureIntent, 1);
		}
			break;

		case R.id.btnSave: {
			if (TextUtils.isEmpty(name.getText().toString())) {
				name.requestFocus();
				return;
			}
			byte[] binary = null;
			if (mImageBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				binary = stream.toByteArray();
			}
			Member n = new Member(null, "test", "12/2/2012", "hanoi", "nam", "url", null);
			if (current_action == HomeActivity.ACTION_TYPE_EDIT) {
				n.id = n_.id;
				update(n);
			} else if (current_action == HomeActivity.ACTION_TYPE_ADDNEW) {
				n_ = save(n);

			}
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			startActivity(i);
		}
			break;
		case R.id.btnCancel: {
			this.finish();
		}
			break;

		default:
			break;
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_edit);
		Bundle b = this.getIntent().getExtras();
		img_frame = (ImageView) findViewById(R.id.image);
		save = (Button) findViewById(R.id.btnSave);
		cancel = (Button) findViewById(R.id.btnCancel);
		name = (EditText) findViewById(R.id.txtName);
		add = (EditText) findViewById(R.id.txtAdd);
		gt = (RadioGroup) findViewById(R.id.rdGt);
		dob = (EditText) findViewById(R.id.txtDob);
		dob.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		n_ = (Member) b.get("member");
		current_action = b.getInt("type");
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
	}

	private Member save(Member n) {
		SQLiteDatabase db = openOrCreateDatabase("memberdb", MODE_PRIVATE, null);
		db.execSQL(HomeActivity.CREATE_TABLE);
		ContentValues values = new ContentValues();
		// d integer primary key autoincrement, name varchar(125), dob
		// varchar(30), add varchar(125), gt varchar(10), nimage varchar (125),
		// nbinary BLOB
		values.put("name", n.name);
		values.put("dob", n.dob);
		values.put("add", n.add);
		values.put("gt", n.gt);
		values.put("nimage", n.nimage);
		values.put("nbinary", n.nbinary);
		n.id = String.valueOf(db.insert("membertb", null, values));
		db.close();

		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		return n;
	}

	public void setAlarm(Date date, PendingIntent intent) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), intent);
	}

	private void update(Member n) {
		SQLiteDatabase db = openOrCreateDatabase("memberdb", MODE_PRIVATE, null);
		ContentValues values = new ContentValues();
		values.put("name", n.name);
		values.put("dob", n.dob);
		values.put("add", n.add);
		values.put("gt", n.gt);
		values.put("nimage", n.nimage);
		values.put("nbinary", n.nbinary);
		db.update("membertb", values, "id= ?", new String[] { n.id });
		db.close();
		Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
	}

}
