package com.thanhgiong.note8;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NoteDetail extends Activity {
	Note n_;
	RelativeLayout l2;
	LinearLayout l21;
	LinearLayout l22;
	LinearLayout l23;
	LinearLayout l24;
	TextView what;
	TextView when;
	TextView where;
	RadioButton rimder;
	ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_note);
		n_ = (Note) this.getIntent().getExtras().get("note");
		l2 = (RelativeLayout) findViewById(R.id.l2);
		l21 = (LinearLayout)  findViewById(R.id.l21);
		l22 = (LinearLayout)  findViewById(R.id.l22);
		l23 = (LinearLayout)  findViewById(R.id.l23);
		l24 = (LinearLayout)  findViewById(R.id.l24);
 		img=(ImageView)  findViewById(R.id.img);
 		long d = System.currentTimeMillis() - Long.parseLong(n_.date); 
        if(d > TimeUnit.DAYS.toMillis(1))  l2.setBackgroundResource(R.drawable.future_bg);
        else if(d > 0) l2.setBackgroundResource(R.drawable.upcomming_bg);
        else l2.setBackgroundResource(R.drawable.past_bg);
        
		what=(TextView)  findViewById(R.id.txtTitle);
		what.setText(n_.title);
		when=(TextView)  findViewById(R.id.textwhen);
		when.setText(String.valueOf(new Date(Long.parseLong(n_.date))));
		where=(TextView)  findViewById(R.id.textWhere);
		img.setOnClickListener(snap);
		
	}
	
	OnClickListener snap = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			startActivityForResult(takePictureIntent, 1);
			
		}
	};
}
