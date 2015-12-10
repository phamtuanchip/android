package com.thanhgiong.note8;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLoadedCallback;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

public class MapPane extends Activity implements OnMapReadyCallback, ConnectionCallbacks, OnMapLoadedCallback, OnConnectionFailedListener, OnMapClickListener {
	LatLng current;
	GPSTracker gps;
	private GoogleMap map;
	GoogleApiClient mGoogleApiClient;
	Location mLastLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		gps = new GPSTracker(this);
		// check if GPS enabled
		if (gps.canGetLocation()) {
			double latitude = gps.getLatitude();
			double longitude = gps.getLongitude();
			current = new LatLng(latitude, longitude);
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		if (mGoogleApiClient == null) {
			mGoogleApiClient = new GoogleApiClient.Builder(this)
					.addConnectionCallbacks(this)
					.addOnConnectionFailedListener(this)
					.addApi(LocationServices.API)
					.build();
		}
		MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		map =	mapFragment.getMap() ;
		if (map!=null){
			mapFragment.getMapAsync(this);
			map.setOnMapLoadedCallback(this);
			map.setOnMapClickListener(this);
			//map.setOnInfoWindowClickListener(this);
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			map.getUiSettings().setZoomGesturesEnabled(true);
		}


	}

	@Override
	public void onMapReady(GoogleMap map) {
//		map.setMyLocationEnabled(true);
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 13));

	}

	//	@Override
	//	public void onInfoWindowClick(Marker marker) {
	//		gps.stopUsingGPS();
	//		Intent i = new Intent(this, NoteEdit.class);
	//		startActivity(i);
	//		// TODO Auto-generated method stub
	//
	//	}

	@Override
	public void onMapLoaded() {
		Geocoder gcd = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = gcd.getFromLocation(current.latitude, current.longitude, 1);
			if (addresses.size() > 0) 	{	    
				Marker mk =	map.addMarker(new MarkerOptions().position(current).title(addresses.get(0).getLocality()));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));

	}

	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub

	}

	public void onConnected(Bundle connectionHint) {
		mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
				mGoogleApiClient);
		if (mLastLocation != null) {
			//mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
			//mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

		}
	}


	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMapClick(LatLng point) {		
		Intent i = new Intent(this, NoteEdit.class);
		Geocoder gcd = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = gcd.getFromLocation(point.latitude, point.longitude, 1);
			if (addresses.size() > 0) 		    
				i.putExtra("address", addresses.get(0).getLocality());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		gps.stopUsingGPS();
		startActivity(i);

	}
}