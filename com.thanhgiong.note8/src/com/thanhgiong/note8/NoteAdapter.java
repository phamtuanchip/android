package com.thanhgiong.note8;

import java.util.List;

import android.R.drawable;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class NoteAdapter extends BaseAdapter implements Filterable{
	static List<Note> data_ ;
	static Note n_;
	private static LayoutInflater inflater=null;	
	Context currentContext_ ;

	public NoteAdapter(Context currentContext, List<Note> data) {
		super();
		currentContext_ = currentContext ;
		data_ = data ;
		inflater = ( LayoutInflater )currentContext.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
		if(rowView == null)
			rowView = inflater.inflate(R.layout.activity_display_block, arg2, false);
		Holder holder=new Holder();
		n_ = (Note)getItem(position);
		//        long d = System.currentTimeMillis() - Long.parseLong(n_.when); 
		//        if(d > TimeUnit.DAYS.toMillis(1))  holder.l2.setBackgroundResource(R.drawable.future_bg);
		//        else if(d > 0) holder.l2.setBackgroundResource(R.drawable.upcomming_bg);
		//        else holder.l2.setBackgroundResource(R.drawable.past_bg);

		holder.img=(ImageView) rowView.findViewById(R.id.image);	
		if(n_.binary != null) {
			Bitmap bm = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
			holder.img.setImageBitmap(bm);
		} else holder.img.setImageBitmap(BitmapFactory.decodeResource(currentContext_.getResources(), R.drawable.bg_default)); 
		holder.what=(TextView) rowView.findViewById(R.id.txtTitle);
		holder.what.setText(n_.what);
		holder.when=(TextView) rowView.findViewById(R.id.textwhen);
		holder.when.setText(n_.when);
		holder.where =(TextView) rowView.findViewById(R.id.txtWhere);
		holder.loc=(ImageView) rowView.findViewById(R.id.loc);
		holder.remind = (ImageView) rowView.findViewById(R.id.re);
		if(!Boolean.parseBoolean(n_.remind)) holder.remind.setVisibility(View.GONE); 
		else holder.remind.setVisibility(View.VISIBLE); 
		if(n_.when != null && !n_.when.isEmpty())  {
			holder.where.setText(n_.where);
			holder.loc.setVisibility(View.VISIBLE); 
			
		} else {
			holder.where.setVisibility(View.GONE);
			holder.loc.setVisibility(View.GONE); 
		}
		//holder.where=(TextView) rowView.findViewById(R.id.textWhere);
		//holder.where.setText(n_.where);

		rowView.setOnClickListener(new OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(currentContext_, "You Clicked ", Toast.LENGTH_LONG).show();
				Intent i = new Intent(v.getContext(), NoteDetail.class);
				i.putExtra("note", n_);
				i.putExtra("type", NoteEdit.ACTION_TYPE_EDIT);
				v.getContext().startActivity(i);
			}
		});   
		return rowView;
	}

	public class Holder
	{ 
		TextView what;
		TextView when;
		TextView where;
		RadioButton rimder;
		ImageView img;
		ImageView remind;
		ImageView loc;
	}

	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return null;
	}
}
