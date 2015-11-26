package com.thanhgiong.note8;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBNote extends SQLiteOpenHelper {

	   public static final String DATABASE_NAME = "mynote.db";
	   public static final String NOTE_TABLE_NAME = "note";
	   public static final String NOTE_COLUMN_ID = "id";	 
	   public static final String NOTE_COLUMN_TITLE = "title";
	   public static final String NOTE_COLUMN_EMAIL = "email";
	   public static final String NOTE_COLUMN_STREET = "street";
	   public static final String NOTE_COLUMN_CITY = "place";
	   public static final String NOTE_COLUMN_PHONE = "phone";
	   public static final String NOTE_COLUMN_DATE = "date";
	   public static final String NOTE_COLUMN_LAST_DATE = "last_date";
	   public static final String NOTE_COLUMN_REMIND = "remind";
	   public static final String NOTE_COLUMN_LOC = "location";
	   private HashMap hp;

	   public DBNote(Context context)
	   {
	      super(context, DATABASE_NAME , null, 1);
	   }

	   @Override
	   public void onCreate(SQLiteDatabase db) {
	      // TODO Auto-generated method stub
	      db.execSQL(
		      "create table " +NOTE_TABLE_NAME +" ("
		      + NOTE_COLUMN_ID +"integer primary key,"
		      + NOTE_COLUMN_TITLE +" text, " 
		      + NOTE_COLUMN_PHONE +" text, " 
		      + NOTE_COLUMN_EMAIL +" text, "
		      + NOTE_COLUMN_STREET+" text, " 
		      + NOTE_COLUMN_CITY  +" text, "
		      + NOTE_COLUMN_DATE  +" text, "
		      + NOTE_COLUMN_LAST_DATE  +" text, "
		      + NOTE_COLUMN_REMIND  +" text, "
		      + NOTE_COLUMN_LOC     +" text)"
	      );
	   }

	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	      // TODO Auto-generated method stub
	      db.execSQL("DROP TABLE IF EXISTS " + NOTE_TABLE_NAME);
	      onCreate(db);
	   }

	   public boolean insertContact  (String name, String phone, String email, String street,String place)
	   {
	      
	      return true;
	   }
	   
	   public boolean insertNote  (Note n)
	   {
		   SQLiteDatabase db = this.getWritableDatabase();
		   ContentValues contentValues = new ContentValues();
		      contentValues.put(NOTE_COLUMN_TITLE,  n.title);
		      contentValues.put(NOTE_COLUMN_PHONE, n.phone);
		      contentValues.put(NOTE_COLUMN_EMAIL, n.email);	
		      contentValues.put(NOTE_COLUMN_STREET, n.street);
		      contentValues.put(NOTE_COLUMN_CITY, n.place);
		      contentValues.put(NOTE_COLUMN_DATE, n.date);
		      contentValues.put(NOTE_COLUMN_LAST_DATE, n.last_date);
		      contentValues.put(NOTE_COLUMN_REMIND, n.remind);
		      contentValues.put(NOTE_COLUMN_LOC, n.location);	      
	      db.insert(NOTE_TABLE_NAME, null, contentValues);
	      return true;
	   }
	   
	   
	   public Cursor getData(int id){
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from "+ NOTE_TABLE_NAME +" where id="+id+"", null );
	      return res;
	   }
	   
	   public int numberOfRows(){
	      SQLiteDatabase db = this.getReadableDatabase();
	      int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTE_TABLE_NAME);
	      return numRows;
	   }
	   
	  
	   
	   public boolean updateNote(Integer id, Note n)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      ContentValues contentValues = new ContentValues();
	      contentValues.put(NOTE_COLUMN_TITLE,  n.title);
	      contentValues.put(NOTE_COLUMN_PHONE, n.phone);
	      contentValues.put(NOTE_COLUMN_EMAIL, n.email);	
	      contentValues.put(NOTE_COLUMN_STREET, n.street);
	      contentValues.put(NOTE_COLUMN_CITY, n.place);
	      contentValues.put(NOTE_COLUMN_DATE, n.date);
	      contentValues.put(NOTE_COLUMN_LAST_DATE, n.last_date);
	      contentValues.put(NOTE_COLUMN_REMIND, n.remind);
	      contentValues.put(NOTE_COLUMN_LOC, n.location);	      
	      db.update(NOTE_TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
	      return true;
	   }
 
	   
	   public Integer deleteNote(Integer id)
	   {
	      SQLiteDatabase db = this.getWritableDatabase();
	      return db.delete(NOTE_TABLE_NAME,"id = ? ",new String[] { Integer.toString(id) });
	   }
	   
	   public ArrayList<Note> getAllNote()
	   {
	      ArrayList<Note> array_list = new ArrayList<Note>();
	      
	      SQLiteDatabase db = this.getReadableDatabase();
	      Cursor res =  db.rawQuery( "select * from " + NOTE_TABLE_NAME, null );
	      res.moveToFirst();
	      
	      while(res.isAfterLast() == false){
	    	  Note n = new Note(res.getString(res.getColumnIndex(NOTE_COLUMN_ID))
	    			           ,res.getString(res.getColumnIndex(NOTE_COLUMN_TITLE))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_PHONE))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_EMAIL))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_STREET))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_CITY))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_DATE))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_LAST_DATE))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_REMIND))
	    			  		   ,res.getString(res.getColumnIndex(NOTE_COLUMN_LOC))
	    			          );
	         array_list.add(n);
	         res.moveToNext();
	      }	      
	   return array_list;
	   }
	}