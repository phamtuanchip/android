package com.thanhgiong.member;

import java.text.SimpleDateFormat;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

	public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {

		@Override
		public Member createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			return new Member(source);
		}

		@Override
		public Member[] newArray(int size) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	public byte[] binary;
	public String id;
	public String image;
	public String remind;
	public String remindTime;
	public String what;
	public String when;

	public String where;

	public Member() {

	}

	public Member(Parcel p) {
		this.id = p.readString();
		this.what = p.readString();
		this.when = p.readString();
		this.where = p.readString();
		this.remind = p.readString();
		this.image = p.readString();
		this.remindTime = p.readString();
		this.binary = new byte[p.readInt()];
		if (binary.length > 0)
			p.readByteArray(this.binary);

	}

	@Deprecated
	public Member(String id, String what, String when, String where, String remind, String image) {
		this.id = id;
		this.what = what;
		this.when = when;
		this.where = where;
		this.remind = remind;
		this.image = image;
	}

	@Deprecated
	public Member(String id, String what, String when, String where, String remind, String image, byte[] binary) {
		this(id, what, when, where, remind, image);
		this.binary = binary;
	}

	public Member(String id, String what, String when, String where, String remind, String image, byte[] binary,
			String remindTime) {
		this(id, what, when, where, remind, image, binary);
		this.remindTime = remindTime;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
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

	public Member[] newArray(int size) {
		return new Member[size];
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
		dest.writeString(remindTime);
		if (binary != null) {
			dest.writeInt(binary.length);
			dest.writeByteArray(binary);
		}

	}

}
