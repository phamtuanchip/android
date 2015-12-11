package com.thanhgiong.note8.rest;

import java.io.Serializable;
import java.text.SimpleDateFormat;


public class Note implements Serializable {
 
	public byte[] binary;
	public String id;
	public String image;
	public String remind;
	public String remindTime;
	public String what;
	public String when;

	public String where;

	public Note() {

	}


	@Deprecated
	public Note(String id, String what, String when, String where, String remind, String image) {
		this.id = id;
		this.what = what;
		this.when = when;
		this.where = where;
		this.remind = remind;
		this.image = image;
	}

	@Deprecated
	public Note(String id, String what, String when, String where, String remind, String image, byte[] binary) {
		this(id, what, when, where, remind, image);
		this.binary = binary;
	}

	public Note(String id, String what, String when, String where, String remind, String image, byte[] binary,
			String remindTime) {
		this(id, what, when, where, remind, image, binary);
		this.remindTime = remindTime;
	}

	 
	public String getFormatedDate() {
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sf2 = new SimpleDateFormat("EEEE dd MMMM, yyyy");
		try {

			return sf2.format(sf.parse(when));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public Note[] newArray(int size) {
		return new Note[size];
	}


}
