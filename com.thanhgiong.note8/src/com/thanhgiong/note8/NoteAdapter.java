package com.thanhgiong.note8;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
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

public class NoteAdapter extends BaseAdapter implements Filterable {
	public class Holder {
		ImageView img;
		ImageView loc;
		ImageView remind;
		// TextView reminderTime;
		RadioButton rimder;
		TextView what;
		TextView when;
		TextView where;
	}
	private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			String filterString = constraint.toString().toLowerCase();

			FilterResults results = new FilterResults();

			final List<Note> list = data_;

			int count = list.size();
			final ArrayList<Note> nlist = new ArrayList<Note>(count);

			Note n;

			for (int i = 0; i < count; i++) {
				n = list.get(i);
				if (n.what.toLowerCase().contains(filterString)) {
					nlist.add(n);
				}
			}

			results.values = nlist;
			results.count = nlist.size();

			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			filter_data_ = (ArrayList<Note>) results.values;
			notifyDataSetChanged();
		}

	}
	private static LayoutInflater mInflater = null;
	static Note n_;
	Context currentContext_;
	List<Note> data_;

	List<Note> filter_data_;

	private ItemFilter mFilter = new ItemFilter();

	public NoteAdapter(Context currentContext, List<Note> data) {
		super();
		currentContext_ = currentContext;
		data_ = data;
		filter_data_ = data;
		// inflater = (LayoutInflater)
		// currentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mInflater = LayoutInflater.from(currentContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return filter_data_.size();
	}

	@Override
	public Filter getFilter() {
		return mFilter;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return filter_data_.get(arg0);
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
			rowView = mInflater.inflate(R.layout.activity_display_block, arg2, false);
		Holder holder = new Holder();
		n_ = (Note) getItem(position);
		holder.what = (TextView) rowView.findViewById(R.id.txtTitle);
		holder.when = (TextView) rowView.findViewById(R.id.textwhen);
		holder.where = (TextView) rowView.findViewById(R.id.txtWhere);
		holder.loc = (ImageView) rowView.findViewById(R.id.loc);
		holder.remind = (ImageView) rowView.findViewById(R.id.re);
		// holder.reminderTime = (TextView)
		// rowView.findViewById(R.id.textRemindTime);
		holder.img = (ImageView) rowView.findViewById(R.id.image);
		if (n_.binary != null) {
			Bitmap bm = BitmapFactory.decodeByteArray(n_.binary, 0, n_.binary.length);
			holder.img.setImageBitmap(bm);
		} else
			holder.img.setImageBitmap(
					BitmapFactory.decodeResource(currentContext_.getResources(), R.drawable.bg_default));
		Typeface tf = Typeface.createFromAsset(currentContext_.getAssets(), "fonts/angelina.ttf");
		holder.what.setTypeface(tf);
		holder.when.setTypeface(tf);
		holder.where.setTypeface(tf);
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

		rowView.setTag(position);
		rowView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(v.getContext(), NoteDetail.class);
				Integer position = (Integer) v.getTag();
				i.putExtra("note", (Note) getItem(position));
				i.putExtra("type", NoteEdit.ACTION_TYPE_EDIT);
				v.getContext().startActivity(i);
			}

		});
		return rowView;
	}
}
