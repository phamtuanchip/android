package com.thanhgiong.member;

import android.os.Parcel;
import android.os.Parcelable;

public class Member implements Parcelable {

	public static final Parcelable.Creator<Member> CREATOR = new Parcelable.Creator<Member>() {

		@Override
		public Member createFromParcel(Parcel source) {
			return new Member(source);
		}

		@Override
		public Member[] newArray(int size) {
			return null;
		}
	};

	public String id;
	public String name;
	public String dob;
	public String add;
	public String gt;
	public String nimage;
	public byte[] nbinary;

	public Member() {

	}
	public Member(String name) {
		this.name = name;
	}
	public Member(String id, String name, String dob, String add, String gt, String image, byte[] nbinary) {
		this.id = id; 
		this.name = name;
		this.dob = dob;
		this.add = add;
		this.gt = gt;
		this.nimage = image;
		this.nbinary = nbinary;
	}
	public Member(Parcel p) {
		this.id = p.readString();
		this.name = p.readString();
		this.dob = p.readString();
		this.add = p.readString();
		this.gt = p.readString();
		this.nimage = p.readString();
		this.nbinary = new byte[p.readInt()];
		if (nbinary.length > 0)
			p.readByteArray(this.nbinary);

	}

	@Override
	public int describeContents() {
		return 0;
	}

	public String getFormatedDate() {
		return dob;
	}

	public Member[] newArray(int size) {
		return new Member[size];
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(dob);
		dest.writeString(add);
		dest.writeString(gt);
		dest.writeString(nimage);
		if (nbinary != null) {
			dest.writeInt(nbinary.length);
			dest.writeByteArray(nbinary);
		}

	}

}
