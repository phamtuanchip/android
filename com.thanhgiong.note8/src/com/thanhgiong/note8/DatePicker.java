package com.thanhgiong.note8;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;



class DatePicker extends Activity implements OnClickListener, OnDateSetListener {   
	TextView _editText;
	private int _day;
	private int _month;
	private int _birthYear;
	private Context _context;

	public DatePicker(Context context, int editTextViewID)
	{       
		Activity act = (Activity)context;
		this._editText = (TextView)act.findViewById(editTextViewID);
		this._editText.setOnClickListener(this);
		this._context = context;
	}


	@Override
	public void onClick(View v) {
		DatePickerDialog dialog =  new DatePickerDialog(_context, this, 2013, 2, 18);
		dialog.show();

	}

	// updates the date in the birth date EditText
	private void updateDisplay() {

		_editText.setText(new StringBuilder()
		// Month is 0 based so add 1
		.append(_day).append("/").append(_month + 1).append("/").append(_birthYear).append(" "));
	}

	@Override
	public void onDateSet(android.widget.DatePicker view, int year,
			int monthOfYear, int dayOfMonth) {
		// TODO Auto-generated method stub
		_birthYear = year;
		_month = monthOfYear;
		_day = dayOfMonth;
		updateDisplay();
	}

}

