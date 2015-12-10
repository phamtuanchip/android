package com.thanhgiong.note8;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

public class MyAlarmService extends Service {

	private NotificationManager mManager;
	private PendingIntent pendingIntent;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		SQLiteDatabase db = openOrCreateDatabase("note8db", MODE_PRIVATE, null);
		db.execSQL(NoteEdit.CREATE_TABLE);
		Cursor cs = db.rawQuery("SELECT * FROM note8tb where nremind=?", new String[] { String.valueOf(Boolean.TRUE) });
		if (cs.moveToFirst()) {
			do {
				Note n = new Note(String.valueOf(cs.getInt(0)), cs.getString(1), cs.getString(2), cs.getString(3),
						cs.getString(4), cs.getString(5), cs.getBlob(6), cs.getString(7));
				SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
				Calendar calendar = new GregorianCalendar();
				try {
					calendar.setTime(sf.parse(n.when));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				calendar.set(Calendar.HOUR_OF_DAY, 11);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.AM_PM, Calendar.AM);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);
			} while (cs.moveToNext());
		}
		db.close();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@SuppressWarnings("static-access")
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);

		this.getApplicationContext();
		mManager = (NotificationManager) this.getApplicationContext()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent1 = new Intent(this.getApplicationContext(), HomeActivity.class);

		Notification notification = new Notification(R.drawable.ic_launcher, "This is a test message!",
				System.currentTimeMillis());
		intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

		PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent1,
				PendingIntent.FLAG_UPDATE_CURRENT);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// notification.setLatestEventInfo(this.getApplicationContext(),
		// "AlarmManagerDemo", "This is a test message!",
		// pendingNotificationIntent);

		mManager.notify(0, notification);
	}

}
