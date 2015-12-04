package com.thanhgiong.note8;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

public class NoteEdit extends Activity implements OnClickListener, OnDateSetListener {
	Note n_;
	EditText whatE;
	EditText whenE;
	EditText whereE;
	String image;
	EditText textTimeE;
	ImageView when;
	ImageView where;
	// GPSTracker class
    GPSTracker gps;

	ImageView img;
	ImageView img_frame;
	RadioButton reminder;
	ImageButton edit;
	ImageButton add;
	ImageButton del;
	ImageButton save;
	ImageButton sw;
	ImageButton lock;
	ImageButton home;
	LocationManager locationManager;
	Bitmap mImageBitmap;
	public static int ACTION_TYPE_VIEW = 1;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_ADDNEW = 3;
	int current_action;
	boolean isEdit = false;
	public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS note8tb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125), nbinary BLOB )";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_edit);
		isEdit = false;
		Bundle b = this.getIntent().getExtras();
		img = (ImageView) findViewById(R.id.img);
		img_frame = (ImageView) findViewById(R.id.image);
		reminder = (RadioButton) findViewById(R.id.remind);
		whatE = (EditText) findViewById(R.id.txtTitleE);
		whenE = (EditText) findViewById(R.id.textwhenE);
		textTimeE = (EditText) findViewById(R.id.textwhenE);
		whereE = (EditText) findViewById(R.id.textWhereE);
		when = (ImageView) findViewById(R.id.date);
		where = (ImageView) findViewById(R.id.loc);
		if (b != null) {
			n_ = (Note) b.get("note");
			if (n_ != null && n_.binary != null) {
				mImageBitmap = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
				img_frame.setImageBitmap(mImageBitmap);
			}
			current_action = b.getInt("type");
		}
		whenE.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		img.setOnClickListener(this);
		when.setOnClickListener(this);
		where.setOnClickListener(this);

		int displayButton[] = { View.GONE, View.GONE, View.GONE, View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE };
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

		save.setOnClickListener(this);
		home.setOnClickListener(this);
		lock.setOnClickListener(this);

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.date: {
			DatePickerDialog dialog = new DatePickerDialog(v.getContext(), this, 2015, 11, 27);
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
//			gps = new GPSTracker(NoteEdit.this);
//            // check if GPS enabled     
//            if(gps.canGetLocation()){
//                 
//                double latitude = gps.getLatitude();
//                double longitude = gps.getLongitude();
//            	String uri = "geo:"+latitude+","+longitude+"";
//    			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
//    			if (isAppInstalled("com.google.android.apps.maps")) {
//    				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
//    				startActivity(intent);
//    			}
//                // \n is for new line
//                //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();    
//            }else{
//                // can't get location
//                // GPS or Network is not enabled
//                // Ask user to enable GPS/network in settings
//                gps.showSettingsAlert();
//            }
			
			Intent i = new Intent(this, MapPane.class);
			startActivity(i);

		
		}
			break;
		case R.id.btnSave: {
			byte[] binary = null;
			if (mImageBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				binary = stream.toByteArray();
			}
			Note n = new Note(null, whatE.getText().toString(), whenE.getText().toString(), whereE.getText().toString(),
					String.valueOf(reminder.isChecked()), image, binary);
			if (current_action == ACTION_TYPE_EDIT) {
				n.id = n_.id;
				update(n);
			} else if (current_action == ACTION_TYPE_ADDNEW) {
				n_ = save(n);

			}
			Intent i = new Intent(v.getContext(), NoteDetail.class);
			i.putExtra("note", n);
			startActivity(i);
			isEdit = false;
		}
			break;
		case R.id.btnHome: {
			Intent i = new Intent(v.getContext(), HomeActivity.class);
			v.getContext().startActivity(i);
		}
			break;
		case R.id.btnLock: {
			Intent i = new Intent(v.getContext(), LoginActivity.class);
			this.fileList();
			v.getContext().startActivity(i);
		}
			break;
		default:
			break;
		}

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
		values.put("nbinary", n.binary);
		n.id = String.valueOf(db.insert("note8tb", null, values));
		db.close();
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		return n;
	}

	private void update(Note n) {
		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		ContentValues values = new ContentValues();
		values.put("nwhat", n.what);
		values.put("nwhen", n.when);
		values.put("nwhere", n.where);
		values.put("nremind", n.remind);
		values.put("nimage", n.image);
		values.put("nbinary", n.binary);
		db.update("note8tb", values, "id= ?", new String[] { n.id });
		db.close();
		Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
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
		whenE.setText(new StringBuilder()
				.append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Bundle extras = data.getExtras();
		mImageBitmap = (Bitmap) extras.get("data");
		img_frame.setImageBitmap(mImageBitmap);
		Toast.makeText(this, "Snaped", Toast.LENGTH_SHORT).show();
	}

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

	public void testProviders() {
		EditText tv = (EditText) findViewById(R.id.textwhenE);
		StringBuilder sb = new StringBuilder("Enabled Providers:");
		List<String> providers = locationManager.getProviders(true);
		for (String provider : providers) {
			locationManager.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
				public void onLocationChanged(Location location) {
				}

				public void onProviderDisabled(String provider) {
				}

				public void onProviderEnabled(String provider) {
				}

				public void onStatusChanged(String provider, int status, Bundle extras) {
				}
			});
			sb.append("\n").append(provider).append(": ");
			Location location = locationManager.getLastKnownLocation(provider);
			if (location != null) {
				double lat = location.getLatitude();
				double lng = location.getLongitude();
				sb.append(lat).append(", ").append(lng);
			} else {
				sb.append("No Location");
			}
		}
		tv.setText(sb.toString());
	}
	
	public void checkLocation(){
		String location_context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(location_context);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		String provider = LocationManager.GPS_PROVIDER;
		Location location = locationManager.getLastKnownLocation(bestProvider);
		//testProviders();
		if (location != null)
			whereE.setText(location.getLongitude() + "," + location.getLatitude());
	}
	
}
