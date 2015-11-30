package com.thanhgiong.note8;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class NoteEdit extends Activity  implements OnClickListener, OnDateSetListener, OnTimeSetListener{
	Note n_;
	EditText whatE;
	EditText whenE;
	EditText whereE;
	String image;
	EditText textTimeE;
	ImageView when;
	ImageView where;
	ImageView img;
	RadioButton reminder;
	ImageButton edit;
	ImageButton add ;
	ImageButton del;
	ImageButton save;
	ImageButton sw ;
	ImageButton lock ;
	ImageButton home;
	AlbumStorageDirFactory mAlbumStorageDirFactory = null;
	private static final String JPEG_FILE_PREFIX = "IMG_";
	private static final String JPEG_FILE_SUFFIX = ".jpg";
	Bitmap mImageBitmap;
	public static int ACTION_TYPE_VIEW = 1;
	public static int ACTION_TYPE_EDIT = 2;
	public static int ACTION_TYPE_ADDNEW = 3;
	int current_action ;
	boolean isEdit = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_edit);
		isEdit = false;
		Bundle b = this.getIntent().getExtras();
		img=(ImageView)  findViewById(R.id.img);
		reminder = (RadioButton) findViewById(R.id.remind);
		whatE =(EditText) findViewById(R.id.txtTitleE);
		whenE =(EditText) findViewById(R.id.textwhenE);
		textTimeE =(EditText) findViewById(R.id.textwhenE);
		whereE =(EditText) findViewById(R.id.textWhereE);
		when=(ImageView)  findViewById(R.id.date);
		where=(ImageView)  findViewById(R.id.loc);
		if(b != null) {
			n_ = (Note)  b.get("note");
//			if(n_ != null) {
//				long d = System.currentTimeMillis() - Long.parseLong(n_.when); 
//				if(d > TimeUnit.DAYS.toMillis(1))  l2.setBackgroundResource(R.drawable.future_bg);
//				else if(d > 0) l2.setBackgroundResource(R.drawable.upcomming_bg);
//				else l2.setBackgroundResource(R.drawable.past_bg);
//			}
			current_action = b.getInt("type");
		}
		whenE.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		img.setOnClickListener(this);
		when.setOnClickListener(this);
		where.setOnClickListener(this);

		int displayButton[]= {View.GONE, View.GONE, View.GONE, View.VISIBLE,View.GONE,View.VISIBLE, View.VISIBLE};
		edit = (ImageButton) findViewById(R.id.btnEdit);
		add = (ImageButton) findViewById(R.id.btnAddnew);
		del = (ImageButton) findViewById(R.id.btnDel);
		save = (ImageButton) findViewById(R.id.btnSave);
		sw = (ImageButton) findViewById(R.id.btnSwitch);
		lock= (ImageButton) findViewById(R.id.btnLock); 
		home=(ImageButton) findViewById(R.id.btnHome);
		
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
		case  R.id.date: {
			DatePickerDialog dialog =  new DatePickerDialog(v.getContext(), this, 2015, 11,27);
			dialog.show();
		}
		break;
		case R.id.img: {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			File f = null;
			try {
				f = setUpPhotoFile();
				image = f.getAbsolutePath();
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
			String uri= "geo:47.6,-122.3";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			if (isAppInstalled("com.google.android.apps.maps")) {
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		}  
		break;
		case R.id.btnSave : {
			 
			Note n = new Note(null, whatE.getText().toString(), whenE.getText().toString(), whereE.getText().toString(),
					String.valueOf(reminder.isChecked()), image);
			
			if(current_action == ACTION_TYPE_EDIT) {
				n.id = n_.id;
				update(n);
			}
			else if (current_action == ACTION_TYPE_ADDNEW) {
				save(n);
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
	private void save(Note n) {
		SQLiteDatabase db =  openOrCreateDatabase("note8db",MODE_PRIVATE, null);
		db.execSQL("CREATE TABLE IF NOT EXISTS note8tb (id integer primary key autoincrement, nwhat varchar(125), nwhen varchar(30), nwhere varchar(125), nremind varchar(10), nimage varchar (125) )");
		ContentValues values = new ContentValues();
		values.put("nwhat", n.what);
		values.put("nwhen", n.when);
		values.put("nwhere", n.where);
		values.put("nremind", n.remind);
		values.put("nimage", n.image);
//		db.execSQL("Insert into note8tb (nwhat, nwhen, nwhere, nremind, nimage)  values ('"+n.what+"',"+n.when+"','"+n.where+"',"+n.remind+"',"+n.image+"')");
		db.insert("note8tb", null, values);
		db.close(); 
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
	}

	private void update(Note n) {
		SQLiteDatabase db =     openOrCreateDatabase("note8db",MODE_PRIVATE, null);
		ContentValues values = new ContentValues();
		values.put("nwhat", n.what);
		values.put("nwhen", n.when);
		values.put("nwhere", n.where);
		values.put("nremind", n.remind);
		values.put("nimage", n.image);
		
//		Cursor cs = db.rawQuery("select * from note8tb where id = ?", new String[]{n.id});
//		db.execSQL("Update table note8tb set nwhat='"+n.what+"', nwhen='"+n.when+"'nwhere='"+n.where+"',"
//				+ " nremind='"+n.remind+"', nimage='"+n.image+"' where id = ?", new String[]{n.id});
		db.update("note8tb", values, "id= ?", new String[]{n.id});
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
	public void onDateSet(android.widget.DatePicker view, int year,
			int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		whenE.setText(new StringBuilder()
		// Month is 0 based so add 1
		.append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" "));
	}

	@Override
	public void onTimeSet(TimePicker arg0, int arg1, int arg2) {
		textTimeE.setText(new StringBuilder()
		// Month is 0 based so add 1
		.append(arg1).append("/").append(arg2 + 1).append(" "));
		
	}
	@Override
	  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		 Bundle extras = data.getExtras();
		    mImageBitmap = (Bitmap) extras.get("data");
		    img.setImageBitmap(mImageBitmap);
		Toast.makeText(this, "Snaped", Toast.LENGTH_SHORT).show();
	}
	private File setUpPhotoFile() throws IOException {

		File f = createImageFile();
		image = f.getAbsolutePath();

		return f;
	}
	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
		File albumF = getAlbumDir();
		File imageF = File.createTempFile(imageFileName, JPEG_FILE_SUFFIX, albumF);
		return imageF;
	}
	private File getAlbumDir() {
		File storageDir = null;

		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {

			storageDir = mAlbumStorageDirFactory.getAlbumStorageDir(getAlbumName());

			if (storageDir != null) {
				if (! storageDir.mkdirs()) {
					if (! storageDir.exists()){
						Log.d("CameraSample", "failed to create directory");
						return null;
					}
				}
			}

		} else {
			Log.v(getString(R.string.app_name), "External storage is not mounted READ/WRITE.");
		}

		return storageDir;
	}
	private String getAlbumName() {
		return "note8img";
	}
}
