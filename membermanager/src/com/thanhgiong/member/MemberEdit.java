package com.thanhgiong.member;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MemberEdit extends Activity implements OnClickListener, OnDateSetListener {
	 
	ImageButton home;
	Bitmap mImageBitmap;
	String image;
int current_action;
	
	ImageButton del;
	ImageButton edit;
	ImageButton add;
	ImageButton cancel;
	ImageButton save;
	ImageButton lock;
	ImageButton sw;
	
	ImageView img;
	ImageView img_frame;
	LinearLayout l1;
	Member n_;
	TextView remindT;
	TextView whatT;
	TextView whenT;
	TextView whereT;
	
	CheckBox reminder;
	TextView remindTime;
	EditText whatE;
	ImageView whenI;
	EditText whenE;
	ImageView whereI;
	EditText whereE;
	
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
		case R.id.date: {
			Calendar cal = Calendar.getInstance();
			DatePickerDialog dialog = new DatePickerDialog(v.getContext(), this, cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
			dialog.show();
		}
			break;
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
		 
		case R.id.btnAddnew: {
			Intent i = new Intent(v.getContext(), MemberEdit.class);
			i.putExtra("type", HomeActivity.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnSave: {
			if(TextUtils.isEmpty(whatE.getText().toString())) {
				whatE.requestFocus();
				return;
			}
			byte[] binary = null;
			if (mImageBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				binary = stream.toByteArray();
			}
			Member n = new Member(null, whatE.getText().toString(), whenE.getText().toString(), whereE.getText().toString(),
					String.valueOf(reminder.isChecked()), image, binary, remindTime.getText().toString());
			if (current_action ==HomeActivity.ACTION_TYPE_EDIT) {
				n.id = n_.id;
				update(n);
			} else if (current_action == HomeActivity.ACTION_TYPE_ADDNEW) {
				n_ = save(n);

			}
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			i.putExtra("note", n);
			startActivity(i);
		}
			break;
		case R.id.btnCancel: {
			this.finish();
		}
			break;
		case R.id.btnLock: {
			Intent i = new Intent(v.getContext(), LoginActivity.class);
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
		setContentView(R.layout.activity_display_edit);
		Bundle b = this.getIntent().getExtras();
		img = (ImageView) findViewById(R.id.img);
		img_frame = (ImageView) findViewById(R.id.image);
		reminder = (CheckBox) findViewById(R.id.remind);
		remindTime = (TextView) findViewById(R.id.textRemindTime);
		whatE = (EditText) findViewById(R.id.txtTitleE);
		whenE = (EditText) findViewById(R.id.textwhenE);
		whereE = (EditText) findViewById(R.id.textWhereE);
		whenI = (ImageView) findViewById(R.id.date);
		whereI = (ImageView) findViewById(R.id.loc);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		del = (ImageButton) findViewById(R.id.btnDel);
		save = (ImageButton) findViewById(R.id.btnSave);
		cancel = (ImageButton) findViewById(R.id.btnCancel);
		lock = (ImageButton) findViewById(R.id.btnLock);
		home = (ImageButton) findViewById(R.id.btnHome);
		whenE.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		current_action = b.getInt("type");
		 

		img.setOnClickListener(this);
		whenI.setOnClickListener(this);
		whereI.setOnClickListener(this);
		reminder.setOnClickListener(this);
		remindTime.setOnClickListener(this);
		add.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		home.setOnClickListener(this);
		lock.setOnClickListener(this);
		save.setVisibility(View.VISIBLE);
		cancel.setVisibility(View.VISIBLE);
		lock.setVisibility(View.VISIBLE);
		home.setVisibility(View.VISIBLE);

	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		whenE.setText(
				new StringBuilder().append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year));
	}

	public void onTimeSet(TimePicker arg0, int hours, int min) {
		String mins = String.valueOf(min);
		if (min < 10)
			mins = "0" + mins;
		remindTime.setText(new StringBuilder().append(hours).append(":").append(mins));
	}

	private Member save(Member n) {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(HomeActivity.CREATE_TABLE);
		ContentValues values = new ContentValues();
		values.put("nwhat", n.what);
		values.put("nwhen", n.when);
		values.put("nwhere", n.where);
		values.put("nremind", n.remind);
		values.put("nimage", n.image);
		values.put("ntime", n.remindTime);
		values.put("nbinary", n.binary);
		n.id = String.valueOf(db.insert("note8tb", null, values));
		db.close();
		 
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		return n;
	}

	public void setAlarm(Date date, PendingIntent intent) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), intent);
	}

	private void update(Member n) {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		ContentValues values = new ContentValues();
		values.put("nwhat", n.what);
		values.put("nwhen", n.when);
		values.put("nwhere", n.where);
		values.put("nremind", n.remind);
		values.put("nimage", n.image);
		values.put("ntime", n.remindTime);
		values.put("nbinary", n.binary);
		db.update("note8tb", values, "id= ?", new String[] { n.id });
		db.close();
		Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
	}

}
