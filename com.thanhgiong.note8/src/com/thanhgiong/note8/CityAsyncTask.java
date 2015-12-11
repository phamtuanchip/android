package com.thanhgiong.note8;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.EditText;

public class CityAsyncTask extends AsyncTask<String, String, String> {
	Activity act;
	double latitude;
	double longitude;
	// private String Address1 = "", Address2 = "", City = "", State = "",
	// Country = "", County = "", PIN = "";

	public CityAsyncTask(Activity act, double latitude, double longitude) {
		// TODO Auto-generated constructor stub
		this.act = act;
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = "";
		Geocoder geocoder = new Geocoder(act, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
			// Log.e("Addresses", "-->" + addresses);
			if(addresses != null && addresses.size() > 0)
			result = new StringBuilder(addresses.get(0).getAddressLine(0)).append(",")
					.append(addresses.get(0).getAddressLine(2)).append(",").append(addresses.get(0).getAddressLine(3))
					.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		// super.onPostExecute(result);
		EditText whereE = (EditText) act.findViewById(R.id.textWhereE);
		whereE.setText(result);
	}

}