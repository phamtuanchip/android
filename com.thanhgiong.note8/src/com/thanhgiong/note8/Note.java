package com.thanhgiong.note8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

	public String id;
	public String title;
	public String phone;
	public String email;
	public String street;
	public String place;
	protected String date;
	public String last_date;
	public String remind;
	public String location;


	public Note(){

	}

	public Note(String id, String title, String phone, String email, String street,String place, String date, String last_date, String remind, String location){
		this.id = String.valueOf(id);
		this.title = title;
		this.phone = phone;
		this.email = email;
		this.street = street;
		this.place = place;
		this.setDate(date);		 
		this.last_date = last_date;
		this.remind = remind;
		this.location = location;
	}

	public Note(Parcel p){
		this.id = p.readString();
		this.title =  p.readString();
		this.phone =  p.readString();
		this.email =  p.readString();
		this.street =  p.readString();
		this.place =  p.readString();
		this.setDate(p.readString());	 
		this.last_date =  p.readString();
		this.remind =  p.readString();
		this.location =  p.readString();
	}


	public String getAddress(){
		return street + "," + place ;
	}
	public String getLongitude(){
		if(location == null || location.isEmpty()) return null;
		return location.split(",")[0];
	}

	public String getlongitude(){
		if(location == null || location.isEmpty()) return null;
		return location.split(",")[1];
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
		dest.writeString(title);
		dest.writeString(phone);
		dest.writeString(email);
		dest.writeString(street);
		dest.writeString(place);
		dest.writeString(getDate());
		dest.writeString(last_date);
		dest.writeString(remind);
		dest.writeString(location);
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
		return date;
	}
	
	public String getFormatedDate(){
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		try {
			
			return sf.format(new Date(Long.parseLong(date)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		return sf.format(new Date());
	}

	public void setDate(String date) {		
		this.date = date;
	} 
	
	public void setDateValue(String f) {
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		try {
			date = String.valueOf(sf.parse(f));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
