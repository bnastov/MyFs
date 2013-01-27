package fr.um2.myfs;

import fr.um2.apicaller.ResponseApi;
import fr.um2.user.OwerUser;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	
	private EditText rEmailView;
	private EditText rPasswordView;
	private EditText rPasswordConfirView;
	private EditText rFirstNameView;
	private EditText rLastNameView;
	
	private View rRegisterFormView;
	private View mLoginFormView;
	private View mLoginStatusView;
	
	private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		initGUI();		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}
	
	/**
	 * Initialize the Graphic User Interface
	 */
	public void initGUI(){
		initView();
		initActionListeners();
	}
	
	/**
	 * Initialize the views
	 */
	public void initView(){

		mEmailView 	= (EditText) findViewById(R.id.email);
		mPasswordView           = (EditText) findViewById(R.id.password);		
		mLoginFormView          = findViewById(R.id.login_form);
		mLoginStatusView        = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		rRegisterFormView = findViewById(R.id.register_form);
		
		rEmailView          = (EditText) findViewById(R.id.register_email);
		rPasswordView       = (EditText) findViewById(R.id.register_password);
		rPasswordConfirView = (EditText) findViewById(R.id.register_password_confirm);
		rFirstNameView      = (EditText) findViewById(R.id.register_first_name);
		rLastNameView       = (EditText) findViewById(R.id.register_last_name);
		
	}
	
	/**
	 * Initialize the action listeners
	 */
	public void initActionListeners(){
		mPasswordView.setOnEditorActionListener(
				new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});
		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						mLoginFormView.setVisibility(View.INVISIBLE);
						rRegisterFormView.setVisibility(View.VISIBLE);
					}
				});
		findViewById(R.id.register_confirm).setOnClickListener(
				new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						attemptRegister();						
					}
				});
		findViewById(R.id.register_cancel).setOnClickListener(
				new View.OnClickListener() {					
					@Override
					public void onClick(View v) {
						rEmailView.setText("");
						rPasswordView.setText("");
						rPasswordConfirView.setText("");
						rFirstNameView.setText("");
						rLastNameView.setText("");
						mLoginFormView.setVisibility(View.VISIBLE);
						rRegisterFormView.setVisibility(View.INVISIBLE);						
					}
				});
	}
	
	/**
	 * Attempts to register the account specified by the register form.
	 */
	
	public void attemptRegister(){
		// Reset errors.
		rEmailView.setError(null);
		rPasswordView.setError(null);
		rPasswordConfirView.setError(null);
		rFirstNameView.setError(null);
		rLastNameView.setError(null);
		// Store values at the time of the register attempt.
		String email           = rEmailView.getText().toString();
		String password        = rPasswordView.getText().toString();
		String confirmPassword = rPasswordConfirView.getText().toString();
		String firstName       = rFirstNameView.getText().toString();
		String lastName        = rLastNameView.getText().toString();

		View focusView = isValidRegistrationFormulaire(email, password, confirmPassword, firstName, lastName);		
		
		if (focusView != null) {
			// There was an error; don't attempt register and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_loading);			
			showProgress(true, false);
			ResponseApi<Void> result = OwerUser.createUser(email, password, firstName, lastName);
			//Simulation of creating user..
			if(result.isOK()){
				OwerUser.loginUser(email, password);
				showProgress(false, false);
				openConnectedActivity();
			}else{
				showProgress(false, false);
				rEmailView.setError(result.getInfo());
				rEmailView.requestFocus();
			}
		}
	}
	
	/**
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual register attempt is made.
	 * 
	 * @param email the entered email address
	 * @param password the entered password
	 * @param confirmPassword the confirmed password
	 * @param firstName the first name
	 * @param lastName the last name
	 * @return the invalid field, null if nothing is invalid
	 */
	public View isValidRegistrationFormulaire(String email, String password, 
			String confirmPassword, String firstName, String lastName){
		
		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			rEmailView.setError(getString(R.string.error_field_required));
			return rEmailView;
		} else if (!email.contains("@")) {
			rEmailView.setError(getString(R.string.error_invalid_email));
			return rEmailView;
		}
		
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			rPasswordView.setError(getString(R.string.error_field_required));
			return rPasswordView;
		} else if (password.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			return mPasswordView;
		}
		
		// Check for a valid confirm password
		if(TextUtils.isEmpty(confirmPassword)){
			rPasswordConfirView.setError(getString(R.string.error_field_required));
			return rPasswordConfirView;
		} else if(!confirmPassword.equals(password)){
			rPasswordConfirView.setError(getString(R.string.error_incorrect_confirm_password_));
			return rPasswordConfirView;
		}
		
		// Check for a valid first name
		if(TextUtils.isEmpty(firstName)){
			rFirstNameView.setError(getString(R.string.error_field_required));
			return rFirstNameView;
		}
		
		// Check for a valid last name
		if(TextUtils.isEmpty(lastName)){
			rFirstNameView.setError(getString(R.string.error_field_required));
			return rLastNameView;
		}
		return null;
	}
	
	/**
	 * Attempts to sign in the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		String email    = mEmailView.getText().toString();
		String password = mPasswordView.getText().toString();

		View focusView = isValidLoginFormulaire(email, password);

		if (focusView != null) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_loading);
			
			showProgress(true, true);
			
			if(OwerUser.loginUser(email, password).isOK()){
				//initUser();
				showProgress(false, true);
				openConnectedActivity();
			}else{
				showProgress(false, true);
				mPasswordView.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
			
		}
	}
	
	/**
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual register attempt is made.
	 * @param email the entered email
	 * @param password the entered password
	 * @return the invalid field, null if nothing is invalid
	 */
	public View isValidLoginFormulaire(String email, String password){
		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			return mPasswordView;
		} else if (password.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			return mPasswordView;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(email)) {
			mEmailView.setError(getString(R.string.error_field_required));
			return mEmailView;
		} else if (!email.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			return mEmailView;
		}
		return null;
	}

	/**
	 * Shows the progress UI and hides the login form.
	 * @param show true to show the loading bar
	 * @param who true to show the login formulaire, false to show the register formulaire
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show, boolean who) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});
			if(who){
				mLoginFormView.setVisibility(View.VISIBLE);
				mLoginFormView.animate().setDuration(shortAnimTime)
						.alpha(show ? 0 : 1)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								mLoginFormView.setVisibility(show ? View.GONE
										: View.VISIBLE);
							}
						});
			}else{
			rRegisterFormView.setVisibility(View.VISIBLE);
			rRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							rRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
			}
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
			rRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}
	
	/**
	 * Go to user activity
	 */
	public void openConnectedActivity() {
		Intent intent = new Intent(this, ConnectedActivity.class);
		startActivity(intent);		
	}
}
