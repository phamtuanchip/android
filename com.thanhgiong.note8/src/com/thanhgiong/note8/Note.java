package com.thanhgiong.note8;

public class Note {
	
	public String id;
	public String title;
	public String phone;
	public String email;
	public String street;
	public String place;
	public String date;
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
		this.date = date;		 
		this.last_date = last_date;
		this.remind = remind;
		this.location = location;
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
 
}
