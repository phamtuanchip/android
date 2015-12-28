package com.thanhgiong.note8;

import java.util.Calendar;
import java.util.Timer;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyAlarmService extends Service {

	private final int UPDATE_INTERVAL = 60 * 1000;
	private Timer timer = new Timer();
	private static final int NOTIFICATION_EX = 1;
	private NotificationManager notificationManager;
	LoginActivity not;

	public MyAlarmService() {
		not = new LoginActivity();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// code to execute when the service is first created
		super.onCreate();
		Log.i("MyService", "Service Started.");
		showNotification();
	}

	public void showNotification() {
		final Calendar cld = Calendar.getInstance();

		int time = cld.get(Calendar.HOUR_OF_DAY);
		if (time > 12) {
			not.createNotification(null);

		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage("Not yet");
			alert.setTitle("Error");
			alert.setPositiveButton("OK", null);
			alert.create().show();
		}
	}

	@Override
	public void onDestroy() {
		if (timer != null) {
			timer.cancel();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startid) {
		return START_STICKY;
	}

	private void stopService() {
		if (timer != null)
			timer.cancel();
	}

}
