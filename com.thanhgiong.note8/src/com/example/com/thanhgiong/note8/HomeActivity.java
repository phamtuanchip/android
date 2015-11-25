package com.example.com.thanhgiong.note8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class HomeActivity extends Activity {
   public final static String EXTRA_MESSAGE = "MESSAGE";
   private ListView list;
   DBNote mydb;
   
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_home);
      
      mydb = new DBNote(this);
      ArrayList<Note> array_list = mydb.getAllNote();
      array_list.add(new Note("1", "test2", null, null, null, null, null, null, null, null));
      array_list.add(new Note("21", "test2", null, null, null, null, null, null, null, null));
      NoteAdapter arrayAdapter=new NoteAdapter(this,array_list);
      list = (ListView) findViewById(R.id.listview);
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
         
         Intent intent = new Intent(getApplicationContext(),DisplayContact.class);
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
}