package com.thanhgiong.note8;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class NoteEdit extends NoteActivity implements OnClickListener, OnDateSetListener, OnTimeSetListener {
	LatLng current;
	GPSTracker gps;
	ImageButton home;
	LocationManager locationManager;
	Bitmap mImageBitmap;

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
		case R.id.loc: {
			gps = new GPSTracker(this);
			if (gps.canGetLocation()) {
				double latitude = gps.getLatitude();
				double longitude = gps.getLongitude();
				CityAsyncTask cs = new CityAsyncTask(this, latitude, longitude);
				cs.execute();
			}

		}
			break;
		case R.id.remind: {
			if (reminder.isChecked()) {
				remindTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
				remindTime.setVisibility(View.VISIBLE);
			} else {
				remindTime.setVisibility(View.GONE);
			}
		}
			break;
		case R.id.textRemindTime: {
			Calendar cal = Calendar.getInstance();
			TimePickerDialog dialog = new TimePickerDialog(v.getContext(), this, cal.get(Calendar.HOUR_OF_DAY),
					cal.get(Calendar.MINUTE), false);
			dialog.show();
		}
			break;
		case R.id.btnAddnew: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnSave: {
			if (TextUtils.isEmpty(whatE.getText().toString())) {
				whatE.setError(getString(R.string.error_field_required));
				whatE.requestFocus();
				return;
			}
			byte[] binary = null;
			if (mImageBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				binary = stream.toByteArray();
			}
			Note n = new Note(null, whatE.getText().toString(), whenE.getText().toString(), whereE.getText().toString(),
					String.valueOf(reminder.isChecked()), image, binary, remindTime.getText().toString());
			if (current_action == ACTION_TYPE_EDIT) {
				n.id = n_.id;
				update(n);
			} else if (current_action == ACTION_TYPE_ADDNEW) {
				n_ = save(n);

			}
			Intent i = new Intent(v.getContext(), NoteDetail.class);
			i.putExtra("note", n);
			startActivity(i);
		}
			break;
		case R.id.btnCancel: {
			this.finish();
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
		remindTime.setText(new SimpleDateFormat("HH:mm").format(new Date()));
		current_action = b.getInt("type");
		if (b != null) {
			n_ = (Note) b.get("note");
			if (n_ != null) {
				if (n_.binary != null) {
					mImageBitmap = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
					img_frame.setImageBitmap(mImageBitmap);
				} else {
					Resources res = getResources();
					int id = R.drawable.bg_default;
					mImageBitmap = BitmapFactory.decodeResource(res, id);
					img_frame.setImageBitmap(mImageBitmap);
				}
				whatE.setText(n_.what);
				whenE.setText(n_.when);
				whereE.setText(n_.where);
				reminder.setChecked(Boolean.parseBoolean(n_.remind));
				if (Boolean.parseBoolean(n_.remind)) {
					remindTime.setText(n_.remindTime);
					remindTime.setVisibility(View.VISIBLE);
				} else {
					remindTime.setVisibility(View.GONE);
				}
			} else {
				gps = new GPSTracker(this);
				if (gps.canGetLocation()) {
					double latitude = gps.getLatitude();
					double longitude = gps.getLongitude();
					CityAsyncTask cs = new CityAsyncTask(this, latitude, longitude);
					cs.execute();
				}
			}
		}

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

	@Override
	public void onTimeSet(TimePicker arg0, int hours, int min) {
		String mins = String.valueOf(min);
		if (min < 10)
			mins = "0" + mins;
		remindTime.setText(new StringBuilder().append(hours).append(":").append(mins));
	}

	private Note save(Note n) {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(CREATE_TABLE);
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
		if (Boolean.parseBoolean(n.remind)) {
			try {
				Intent myIntent = new Intent(getApplicationContext(), MyReceiver.class);
				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent,
						PendingIntent.FLAG_ONE_SHOT);
				setAlarm(new SimpleDateFormat("dd/MM/yyyy hh:mm").parse(n.when + " " + n.remindTime), pendingIntent);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		return n;
	}

	public void setAlarm(Date date, PendingIntent intent) {
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), intent);
	}

	private void update(Note n) {
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
