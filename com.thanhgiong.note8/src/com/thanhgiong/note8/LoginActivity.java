package com.thanhgiong.note8;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	// Class cÃ i Ä‘áº·t login cháº¡y ngáº§m AsyncTask
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mPassword;

		UserLoginTask(String password) {
			mPassword = password;
		}

		// Thá»±c hiá»‡n login cháº¡y ngáº§m
		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
			String savedPWD = settings.getString(PREFS_KEY, null);
			return savedPWD.equals(mPassword);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}

		// Thá»±c hiá»‡n chuyá»ƒn giao diá»‡n náº¿u login thÃ nh cÃ´ng
		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
			if (success) {
				Intent i = new Intent(getBaseContext(), HomeActivity.class);
				startActivity(i);
			} else {
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}
	}
	static final String PREFS_KEY = "pwd";
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	static final String PREFS_NAME = "note8Info";
	static final String PREFS_VIEW = "view";

	View createPass;
	EditText e1;
	EditText e2;
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	private View mLoginFormView;
	private EditText mPasswordView;
	private View mProgressView;

	Button saveP;

	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}
		mPasswordView.setError(null);
		String password = mPasswordView.getText().toString();
		boolean cancel = false;
		View focusView = null;

		// Thá»±c hiá»‡n kiá»ƒm tra máº­t kháº©u
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			// Tá»±c hiá»‡n quÃ¡ trÃ¬nh login
			showProgress(true);
			mAuthTask = new UserLoginTask(password);
			mAuthTask.execute((Void) null);
		}
	}

	// HÃ m kiá»ƒm tra Ä‘á»™ dÃ i tá»‘i thiá»ƒu cá»§a máº­t kháº©u
	private boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		startService(new Intent(this, MyAlarmService.class));
		 
		// Láº¥y thÃ´ng tin sharedprefrence Ä‘á»ƒ kiá»ƒm tra xem ngÆ°á»�i dÃ¹ng Ä‘Ã£ lÆ°u máº­t
		// kháº©u chÆ°a
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		// láº¥y form login
		mLoginFormView = findViewById(R.id.login_form);
		// láº¥y form cÃ³ áº£nh progess
		mProgressView = findViewById(R.id.login_progress);
		createPass = findViewById(R.id.createPass);
		// Kiá»ƒm tra máº­t kháº©u Ä‘Ã£ lÆ°u náº¿u chÆ°a lÆ°u máº­t kháº©u hiá»‡n form lÆ°u máº­t kháº©u
		if (settings.getString(PREFS_KEY, null) == null) {
			createPass.setVisibility(View.VISIBLE);
			mLoginFormView.setVisibility(View.GONE);
			e1 = (EditText) findViewById(R.id.password1);
			e2 = (EditText) findViewById(R.id.password2);
			saveP = (Button) findViewById(R.id.savePass);
			saveP.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					String p1 = e1.getText().toString();
					// Máº­t kháº©u khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng
					if (!TextUtils.isEmpty(p1) && !isPasswordValid(p1)) {
						e1.setError(getString(R.string.error_invalid_password));
						e1.requestFocus();
						return;
					}
					String p2 = e2.getText().toString();
					// GhÃµ láº¡i máº­t kháº©u khÃ´ng Ä‘Æ°á»£c Ä‘á»ƒ trá»‘ng
					if (!TextUtils.isEmpty(p2) && !isPasswordValid(p2)) {
						e2.setError(getString(R.string.error_invalid_password));
						e2.requestFocus();
						return;
					} else if (!p1.equals(p2)) {
						e2.setError(getString(R.string.error_invalid_confirm));
						e2.requestFocus();
					} else {
						editor.putString(PREFS_KEY, p1);
						editor.commit();
						Toast.makeText(v.getContext(), "password saved!", Toast.LENGTH_SHORT).show();
						Intent i = new Intent(v.getContext(), LoginActivity.class);
						v.getContext().startActivity(i);
					}

				}
			});

		} else {
			// Náº¿u Ä‘Ã£ lÆ°u máº­t kháº©u rá»“i thÃ¬ hiá»‡n form Ä‘Äƒng nháº­p
			createPass.setVisibility(View.GONE);
			mLoginFormView.setVisibility(View.VISIBLE);
			mPasswordView = (EditText) findViewById(R.id.password);
			mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
					if (id == R.id.login || id == EditorInfo.IME_NULL) {
						// Gá»�i hÃ m login
						attemptLogin();
						return true;
					}
					return false;
				}
			});

			Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
			mEmailSignInButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					attemptLogin();
				}
			});

		}
	}

	// HÃ m thá»±c hiá»‡n viá»‡c login vÃ  load áº£nh quay (progressbar)
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	public void showProgress(final boolean show) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime).alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
						}
					});

			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mProgressView.animate().setDuration(shortAnimTime).alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
						}
					});
		} else {
			mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	 public void createNotification(View view) {
		 if(view != null) {
	        // Prepare intent which is triggered if the
	        // notification is selected
	        Intent intent = new Intent(this, LoginActivity.class);
	        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

	        // Build notification
	        // Actions are just fake
	        Notification noti = new Notification.Builder(this)
	            .setContentTitle("Notification Title")
	            .setContentText("Click here to read").setSmallIcon(R.drawable.ic_launcher)
	            .setContentIntent(pIntent)
	            .build();
	        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        // hide the notification after its selected
	        noti.flags |= Notification.FLAG_AUTO_CANCEL;

	        notificationManager.notify(0, noti);
		 }
	      } 


}
