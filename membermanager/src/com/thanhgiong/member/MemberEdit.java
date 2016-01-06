package com.thanhgiong.member;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MemberEdit extends Activity implements OnClickListener {

	ImageButton home;
	Bitmap mImageBitmap;
	String image;
	int current_action;
	Button cancel;
	Button save;
	Button del;
	ImageView img_frame;
	Member n_;

	EditText name;
	EditText dob;
	EditText phone;
	EditText add;
	RadioGroup gt;
	RadioButton m;
	RadioButton f;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_edit);
		//Parse.enableLocalDatastore(this);
		//Parse.initialize(this);
		Bundle b = this.getIntent().getExtras();
		img_frame = (ImageView) findViewById(R.id.image);
		save = (Button) findViewById(R.id.btnSave);
		cancel = (Button) findViewById(R.id.btnCancel);
		del = (Button) findViewById(R.id.btnDel);
		name = (EditText) findViewById(R.id.txtName);
		add = (EditText) findViewById(R.id.txtAdd);
		gt = (RadioGroup) findViewById(R.id.rdGt);
		m = (RadioButton) findViewById(R.id.rdM);
		m.setChecked(true);
		f = (RadioButton) findViewById(R.id.rdF);
		dob = (EditText) findViewById(R.id.txtDob);
		phone = (EditText) findViewById(R.id.txtPhone);
		current_action = b.getInt("type");
		if (b != null) {
			n_ = (Member) b.get("member");
			if (n_ != null) {
				name.setText(n_.name);
				add.setText(n_.add);
				dob.setText(n_.dob);
				phone.setText(n_.phone);
				m.setChecked("Male".equals(n_.gt));
				f.setChecked("Female".equals(n_.gt));
				if (n_.nbinary != null) {
					mImageBitmap = BitmapFactory.decodeByteArray(n_.nbinary, 0, n_.nbinary.length);
					img_frame.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
					img_frame.setImageBitmap(mImageBitmap);
				} else {
					Resources res = getResources();
					int id = R.drawable.business189;
					mImageBitmap = BitmapFactory.decodeResource(res, id);
					img_frame.setImageBitmap(mImageBitmap);
				}
			}
		}
		img_frame.setOnClickListener(this);
		save.setOnClickListener(this);
		cancel.setOnClickListener(this);
		del.setOnClickListener(this);
	}

	@SuppressLint("SimpleDateFormat")
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
		img_frame.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
		img_frame.setImageBitmap(mImageBitmap);
		Toast.makeText(this, "Snaped", Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.image: {
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
			if (TextUtils.isEmpty(name.getText())) {
				name.setError("Name is required!");
				name.requestFocus();
				return;
			}
			byte[] binary = null;
			if (mImageBitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				mImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				binary = stream.toByteArray();
			}
			final String value = ((RadioButton) findViewById(gt.getCheckedRadioButtonId())).getText().toString();
			Member n = new Member(null, name.getText().toString(), dob.getText().toString(), add.getText().toString(),
					value, phone.getText().toString(), binary);
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
		case R.id.btnDel: {
			SQLiteDatabase db = openOrCreateDatabase(DbUtil.DB_NAME, MODE_PRIVATE, null);
			db.delete(DbUtil.TB_MEMBER_NAME, "id= ?", new String[] { n_.id });
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

	public boolean isOnline() {
		ConnectivityManager cm =
				(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();
	}

	private Member save(Member n) {
		if(isOnline()) {
			  ParseObject p = new ParseObject("member");
			  for (Field f : n.getClass().getDeclaredFields()) {
				  try {
					p.put(f.getName(), f.get(n));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }
			  p.saveInBackground(new SaveCallback() {
				@Override
				public void done(ParseException arg0) {
					Toast.makeText(MemberEdit.this, "Saved to cloud", Toast.LENGTH_SHORT); 
				}
			});
			 
		}
		SQLiteDatabase db = openOrCreateDatabase(DbUtil.DB_NAME, MODE_PRIVATE, null);
		db.execSQL(DbUtil.CREATE_TABLE);
		ContentValues values = new ContentValues();
		values.put("name", n.name);
		values.put("dob", n.dob);
		values.put("addr", n.add);
		values.put("gt", n.gt);
		values.put("phone", n.phone);
		values.put("nbinary", n.nbinary);
		n.id = String.valueOf(db.insert(DbUtil.TB_MEMBER_NAME, null, values));
		db.close();
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
		return n;
	}

	private void update(Member n) {
		SQLiteDatabase db = openOrCreateDatabase(DbUtil.DB_NAME, MODE_PRIVATE, null);
		ContentValues values = new ContentValues();
		values.put("name", n.name);
		values.put("dob", n.dob);
		values.put("addr", n.add);
		values.put("gt", n.gt);
		values.put("phone", n.phone);
		values.put("nbinary", n.nbinary);
		db.update(DbUtil.TB_MEMBER_NAME, values, "id= ?", new String[] { n.id });
		db.close();
		Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show();
	}

}
