package com.thanhgiong.note8;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.CalendarContract.Reminders;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteEdit extends Activity  implements OnClickListener, OnDateSetListener{
	Note n_;
	RelativeLayout l2;
	RelativeLayout r1;
	LinearLayout l21;
	LinearLayout l22;
	LinearLayout l23;
	LinearLayout l24;
	EditText whatE;
	EditText whenE;
	EditText whereE;

	RadioButton reminder;
	ImageView img;
	Button edit;
	Button save;
	Button add;
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
		l2 = (RelativeLayout) findViewById(R.id.l2);
		l21 = (LinearLayout)  findViewById(R.id.l21);
		l22 = (LinearLayout)  findViewById(R.id.l22);
		l23 = (LinearLayout)  findViewById(R.id.l23);
		l24 = (LinearLayout)  findViewById(R.id.l24);
		r1 = (RelativeLayout) findViewById(R.id.r1);
		img=(ImageView)  findViewById(R.id.img);
		reminder = (RadioButton) findViewById(R.id.remind);
		whatE =(EditText) findViewById(R.id.txtTitleE);
		whenE =(EditText) findViewById(R.id.textwhenE);
		whereE =(EditText) findViewById(R.id.textWhereE);
		if(b != null) {
			n_ = (Note)  b.get("note");
			long d = System.currentTimeMillis() - Long.parseLong(n_.date); 
			if(d > TimeUnit.DAYS.toMillis(1))  l2.setBackgroundResource(R.drawable.future_bg);
			else if(d > 0) l2.setBackgroundResource(R.drawable.upcomming_bg);
			else l2.setBackgroundResource(R.drawable.past_bg);
		}
		current_action = ACTION_TYPE_EDIT;
		r1.setVisibility(View.VISIBLE);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case  R.id.textwhen: {
			DatePickerDialog dialog =  new DatePickerDialog(v.getContext(), this, 2015, 11,27);
			dialog.show();
		}
		break;
		case R.id.img: {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, 1);
		}
		break;
		case R.id.textWhere: {
			String uri= "geo:47.6,-122.3";
			Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
			if (isAppInstalled("com.google.android.apps.maps")) {
				intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
				startActivity(intent);
			}
		} case R.id.btnEdit: {
			setContentView(R.layout.activity_display_edit);
			bindEField(n_);
			save = (Button) findViewById(R.id.btnSave);
			if(save != null) save.setOnClickListener(this);
			isEdit = true;
			current_action = ACTION_TYPE_EDIT;
		} 
		break;
		case R.id.btnAddnew: {
			setContentView(R.layout.activity_display_edit);
			bindEField(null);
			save = (Button) findViewById(R.id.btnSave);
			if(save != null) save.setOnClickListener(this);
			isEdit = false;
			current_action = ACTION_TYPE_ADDNEW;
		} 
		break;
		case R.id.btnSave : {			
			setContentView(R.layout.activity_display_detail);
			if(current_action == ACTION_TYPE_EDIT) update(n_);
			else if (current_action == ACTION_TYPE_ADDNEW) save(n_);
			r1 = (RelativeLayout)findViewById(R.id.r1);
			r1.setVisibility(View.VISIBLE);
			edit.setOnClickListener(this);
			isEdit = false;			
		} 
		break;
		default:
			break;
		}

	}
	private void save(Note n) {
		Note nd = new Note(n.id, n.title, n.phone, n.email, n.street,n.place, n.date, n.last_date, n.remind, n.location);
		Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
	}

	private void update(Note n) {
		Note nd = new Note(n.id, n.title, n.phone, n.email, n.street,n.place, n.date, n.last_date, n.remind, n.location);
		Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
	}
	private void bindEField(Note n_2) {
		whatE =(EditText) findViewById(R.id.txtTitleE);
		whenE =(EditText) findViewById(R.id.textwhenE);
		whereE =(EditText) findViewById(R.id.textWhereE);
		reminder = (RadioButton) findViewById(R.id.remind);
		if(n_2!= null) {
			whatE.setText(n_2.title);
			whenE.setText(n_2.getFormatedDate());
			whereE.setText(n_2.getAddress());
			reminder.setChecked(Boolean.parseBoolean(n_2.remind));
		} else {
			whatE.setText(null);
			whenE.setText(new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
			whereE.setText(null);
			reminder.setChecked(false);
		}

	}
	private void bindField(Note n_2) {
		reminder = (RadioButton) findViewById(R.id.re);
		reminder.setChecked(Boolean.parseBoolean(n_2.remind));
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
}
