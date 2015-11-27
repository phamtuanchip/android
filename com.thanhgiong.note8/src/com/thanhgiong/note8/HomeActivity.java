package com.thanhgiong.note8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.thanhgiong.note8.R;

@SuppressWarnings("deprecation")
public class HomeActivity extends Activity implements OnClickListener{
	public final static String EXTRA_MESSAGE = "MESSAGE";
	private ListView list;
	DBNote mydb;

	ImageButton add ;
	ImageButton sw ;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mydb = new DBNote(this);
		ArrayList<Note> array_list = mydb.getAllNote();
		if(array_list == null || array_list.size() == 0) {
			array_list.add(new Note("1", "test1", null, null, null, null, String.valueOf(System.currentTimeMillis()), null, null, null));
			array_list.add(new Note("2", "test2", null, null, null, null,  String.valueOf(new Date(2015,11, 20).getTime()), null, null, null));
			array_list.add(new Note("3", "test3", null, null, null, null,  String.valueOf(new Date(2015,11, 28).getTime()), null, null, null));
			array_list.add(new Note("4", "test4", null, null, null, null,  String.valueOf(new Date(2015,11, 20).getTime()), null, null, null));
		}
		add = (ImageButton) findViewById(R.id.btnHAddnew);
		sw = (ImageButton) findViewById(R.id.btnHswitch);
		add.setOnClickListener(this);
		sw.setOnClickListener(this);
		list = (ListView) findViewById(R.id.listView);
		list.setAdapter(new NoteAdapter(this, array_list));
		//      obj.setOnItemClickListener(new OnItemClickListener(){
		//         @Override
		//         public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		//            // TODO Auto-generated method stub
		//            int id_To_Search = arg2 + 1;
		//            
		//            Bundle dataBundle = new Bundle();
		//            dataBundle.putInt("id", id_To_Search);
		//            
		//            Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
		//            
		//            intent.putExtras(dataBundle);
		//            startActivity(intent);
		//         }
		//      });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);

		switch(item.getItemId())
		{
		case R.id.item1 :Bundle dataBundle = new Bundle();
		dataBundle.putInt("id", 0);

		Intent intent = new Intent(getApplicationContext(),NoteDetail.class);
		intent.putExtras(dataBundle);

		startActivity(intent);
		return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_BACK) {
			moveTaskToBack(true);
		}
		return super.onKeyDown(keycode, event);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnHAddnew: {
			Intent i = new Intent(v.getContext(), NoteEdit.class);
			i.putExtra("type", NoteEdit.ACTION_TYPE_ADDNEW);
        	v.getContext().startActivity(i);
		}
		break;
		case R.id.btnHswitch: {
			Toast.makeText(this, "Change view", Toast.LENGTH_SHORT).show();
		}
		default:
			break;
		}
	}
}