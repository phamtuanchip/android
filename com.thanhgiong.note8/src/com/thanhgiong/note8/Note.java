package com.thanhgiong.note8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

	public String id;
	public String what;
	public String when;
	public String where;
	public String image;
	public String remind;
	public byte[] binary;

	public Note(){

	}
	public Note(String id, String what, String when, String where, String remind, String image){
		this.id = id;
		this.what = what;
		this.when = when;
		this.where = where;
		this.remind = remind;
		this.image = image;
	}

	public Note(String id, String what, String when, String where, String remind, String image, byte[] binary){
		this(id, what, when, where, remind, image);
		this.binary = binary;
	}
	public Note(Parcel p){
		this.id = p.readString();
		this.what =  p.readString();
		this.when =  p.readString();
		this.where =  p.readString();
		this.remind =  p.readString();
		this.image =  p.readString();
		this.binary = new byte[p.readInt()];
		p.readByteArray(this.binary);
	}

	 
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(what);
		dest.writeString(when);
		dest.writeString(where);
		dest.writeString(remind);
		dest.writeString(image);
		if(binary!= null) {
		dest.writeInt(binary.length);	
		dest.writeByteArray(binary);
		}
	}
	public static final Parcelable.Creator<Note> CREATOR = new Parcelable.Creator<Note>() {

		@Override
		public Note createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Note(source);
		}

		@Override
		public Note[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	}; 
			 
		
	
	public Note[] newArray(int size) { return new Note[size]; }

	public String getDate() {
		return when;
	}
	
	public String getFormatedDate(){
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			
			return sf.format(new Date(Long.parseLong(when)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return sf.format(new Date());
	}

	public void setDate(String date) {		
		this.when = date;
	} 
	
	public void setDateValue(String f) {
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			when = String.valueOf(sf.parse(f));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
