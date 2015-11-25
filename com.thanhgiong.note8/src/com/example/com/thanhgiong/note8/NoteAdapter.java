package com.example.com.thanhgiong.note8;

import java.util.List;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class NoteAdapter extends BaseAdapter {
private List<Note> data_ ;
private static LayoutInflater inflater=null;	
	Context currentContext_ ;
	
	public NoteAdapter(Context currentContext, List<Note> data) {
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
		View rowView;       
        rowView = inflater.inflate(R.layout.activity_display_note, null);
        Holder holder=new Holder();
        holder.l1 = (LinearLayout) rowView.findViewById(R.id.l1);
        holder.l2 = (LinearLayout) rowView.findViewById(R.id.l2);
        holder.img=(ImageView) rowView.findViewById(R.id.img);		 
        holder.what=(TextView) rowView.findViewById(R.id.txtTitle);
        holder.when=(TextView) rowView.findViewById(R.id.textwhen);
        holder.where=(TextView) rowView.findViewById(R.id.textWhere);
       
		TextView display = new TextView(currentContext_) ;
		display.setText(data_.get(position).title) ;
		return display;
	}

	 public class Holder
	    { 
		 	LinearLayout l1;
		 	LinearLayout l2;
	        TextView what;
	        TextView when;
	        TextView where;
	        RadioButton rimder;
	        ImageView img;
	    }
}
