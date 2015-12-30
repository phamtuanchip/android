package com.thanhgiong.member;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class MemberListAdapter extends BaseAdapter implements Filterable {
	public class Holder {
		ImageView img;
		TextView name;
	}
	 
	static List<Member> data_;
	private static LayoutInflater mInflater = null;
	static Member n_;
	Context currentContext_;


	public MemberListAdapter(Context currentContext, List<Member> data) {
		super();
		currentContext_ = currentContext;
		data_ = data;
		mInflater = LayoutInflater.from(currentContext);
	}

	@Override
	public int getCount() {
		return data_.size();
	}

	 

	@Override
	public Object getItem(int arg0) {
		return data_.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View currentContext, ViewGroup arg2) {
		View rowView = currentContext;
		if (rowView == null)
			rowView = mInflater.inflate(R.layout.activity_display_list, arg2, false);
		Holder holder = new Holder();
		n_ = (Member) getItem(position);
		holder.img = (ImageView) rowView.findViewById(R.id.img);
		holder.name = (TextView) rowView.findViewById(R.id.name);
		holder.name.setText(n_.name);
		rowView.setTag(position);
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), MemberEdit.class);
				//i.putExtra("member", (Member) getItem(Integer.parseInt(rowView.getTag().toString())));
				i.putExtra("type", HomeActivity.ACTION_TYPE_EDIT);
				v.getContext().startActivity(i);
			}
		});
		return rowView;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
