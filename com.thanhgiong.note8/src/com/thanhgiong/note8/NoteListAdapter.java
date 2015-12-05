package com.thanhgiong.note8;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter {
	static List<Note> data_;
	static Note n_;
	private static LayoutInflater inflater = null;
	Context currentContext_;

	public NoteListAdapter(Context currentContext, List<Note> data) {
		super();
		currentContext_ = currentContext;
		data_ = data;
		inflater = (LayoutInflater) currentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data_.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data_.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View currentContext, ViewGroup arg2) {
		View rowView = currentContext;
		if (rowView == null)
			rowView = inflater.inflate(R.layout.activity_display_list, arg2, false);
		Holder holder = new Holder();
		n_ = (Note) getItem(position);
		holder.what = (TextView) rowView.findViewById(R.id.txtWhat);
		holder.when = (TextView) rowView.findViewById(R.id.txtWhen);
		holder.remind = (ImageView) rowView.findViewById(R.id.re);
		holder.loc = (ImageView) rowView.findViewById(R.id.loc);
		holder.img = (ImageView) rowView.findViewById(R.id.img);
		Typeface tf = Typeface.createFromAsset(currentContext_.getAssets(), "fonts/angelina.ttf");
		holder.what.setTypeface(tf);
		holder.when.setTypeface(tf);
		holder.what.setText(n_.what);
		holder.when.setText(n_.getFormatedDate());
		if (!Boolean.parseBoolean(n_.remind))
			holder.remind.setVisibility(View.GONE);
		else
			holder.remind.setVisibility(View.VISIBLE);

		if (n_.where != null && !n_.where.isEmpty())
			holder.loc.setVisibility(View.VISIBLE);
		else
			holder.loc.setVisibility(View.GONE);

		if (n_.image != null && !n_.image.isEmpty())
			holder.img.setVisibility(View.VISIBLE);
		else
			holder.img.setVisibility(View.GONE);

		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(currentContext_, "You Clicked ",
				// Toast.LENGTH_LONG).show();
				Intent i = new Intent(v.getContext(), NoteDetail.class);
				i.putExtra("note", n_);
				i.putExtra("type", NoteEdit.ACTION_TYPE_EDIT);
				v.getContext().startActivity(i);
			}
		});
		return rowView;
	}

	public class Holder {
		TextView what;
		TextView when;
		TextView where;
		RadioButton rimder;
		ImageView img;
		ImageView remind;
		ImageView loc;
	}
}
