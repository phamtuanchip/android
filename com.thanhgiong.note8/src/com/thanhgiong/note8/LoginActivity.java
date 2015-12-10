package com.thanhgiong.note8;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
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

	// Class cài đặt login chạy ngầm AsyncTask
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

		private final String mPassword;

		UserLoginTask(String password) {
			mPassword = password;
		}

		// Thực hiện login chạy ngầm
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

		// Thực hiện chuyển giao diện nếu login thành công
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

		// Thực hiện kiểm tra mật khẩu
		if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			// Tực hiện quá trình login
			showProgress(true);
			mAuthTask = new UserLoginTask(password);
			mAuthTask.execute((Void) null);
		}
	}

	// Hàm kiểm tra độ dài tối thiểu của mật khẩu
	private boolean isPasswordValid(String password) {
		return password.length() > 4;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// Lấy thông tin sharedprefrence để kiểm tra xem người dùng đã lưu mật
		// khẩu chưa
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		// lấy form login
		mLoginFormView = findViewById(R.id.login_form);
		// lấy form có ảnh progess
		mProgressView = findViewById(R.id.login_progress);
		createPass = findViewById(R.id.createPass);
		// Kiểm tra mật khẩu đã lưu nếu chưa lưu mật khẩu hiện form lưu mật khẩu
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
					// Mật khẩu không được để trống
					if (!TextUtils.isEmpty(p1) && !isPasswordValid(p1)) {
						e1.setError(getString(R.string.error_invalid_password));
						e1.requestFocus();
						return;
					}
					String p2 = e2.getText().toString();
					// Ghõ lại mật khẩu không được để trống
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
			// Nếu đã lưu mật khẩu rồi thì hiện form đăng nhập
			createPass.setVisibility(View.GONE);
			mLoginFormView.setVisibility(View.VISIBLE);
			mPasswordView = (EditText) findViewById(R.id.password);
			mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				@Override
				public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
					if (id == R.id.login || id == EditorInfo.IME_NULL) {
						// Gọi hàm login
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

	// Hàm thực hiện việc login và load ảnh quay (progressbar)
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
}
