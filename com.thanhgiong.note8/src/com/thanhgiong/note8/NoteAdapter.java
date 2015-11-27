package com.thanhgiong.note8;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.thanhgiong.note8.R;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class NoteAdapter extends BaseAdapter {
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
        rowView = inflater.inflate(R.layout.activity_display_note, arg2, false);
        Holder holder=new Holder();
        //holder.l1 = (LinearLayout) rowView.findViewById(R.id.l1);
        holder.l2 = (RelativeLayout) rowView.findViewById(R.id.l2);
        n_ = (Note)getItem(position);
        long d = System.currentTimeMillis() - Long.parseLong(n_.date); 
        if(d > TimeUnit.DAYS.toMillis(1))  holder.l2.setBackgroundResource(R.drawable.future_bg);
        else if(d > 0) holder.l2.setBackgroundResource(R.drawable.upcomming_bg);
        else holder.l2.setBackgroundResource(R.drawable.past_bg);
        holder.l21 = (LinearLayout) rowView.findViewById(R.id.l21);
        holder.l22 = (LinearLayout) rowView.findViewById(R.id.l22);
        holder.l23 = (LinearLayout) rowView.findViewById(R.id.l23);
        holder.l24 = (LinearLayout) rowView.findViewById(R.id.l24);
        
        holder.img=(ImageView) rowView.findViewById(R.id.img);	
        //holder.img.setImageResource(R.drawable.ic_launcher);
        holder.what=(TextView) rowView.findViewById(R.id.txtTitle);
        holder.what.setText(n_.title);
        holder.when=(TextView) rowView.findViewById(R.id.textwhen);
        holder.when.setText(String.valueOf(new Date(Long.parseLong(n_.date))));
        holder.where=(TextView) rowView.findViewById(R.id.textWhere);
		 
		rowView.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               //Toast.makeText(currentContext_, "You Clicked ", Toast.LENGTH_LONG).show();
            	Intent i = new Intent(v.getContext(), NoteDetail.class);
            	i.putExtra("note", n_);
            	v.getContext().startActivity(i);
            }
        });   
		return rowView;
	}

	 public class Holder
	    { 
		 	LinearLayout l1;
		 	RelativeLayout l2;
		 	LinearLayout l21;
		 	LinearLayout l22;
		 	LinearLayout l23;
		 	LinearLayout l24;
	        TextView what;
	        TextView when;
	        TextView where;
	        RadioButton rimder;
	        ImageView img;
	    }
}
