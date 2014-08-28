package com.minibittechnologies.fragments;

import java.security.PublicKey;

import info.hoang8f.android.segmented.SegmentedGroup;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.minibittechnologies.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

	SegmentedGroup loginSegment;
	RelativeLayout signUpLayout, loginLayout;
	EditText etEmail, etFirstName, etLastName, etPassword,etEmailLogIn,etPasswordLogin;
	Button bSignUp, bLogin;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(">>>>>>", "onCreate, LoginFragment");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		Log.e(">>>>>>", "onCreateView, LoginFragment");
		View v = inflater.inflate(R.layout.fragment_login, container, false);

		loginSegment = (SegmentedGroup) v.findViewById(R.id.segmented);

		signUpLayout = (RelativeLayout) v.findViewById(R.id.sg_signup);
		loginLayout = (RelativeLayout) v.findViewById(R.id.sg_login);

		etEmail = (EditText) v.findViewById(R.id.et_email);
		etFirstName = (EditText) v.findViewById(R.id.et_first_name);
		etLastName = (EditText) v.findViewById(R.id.et_last_name);
		etPassword = (EditText) v.findViewById(R.id.et_password);

		bSignUp = (Button) v.findViewById(R.id.b_continue);
		bSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Validate the sign up data
				boolean validationError = false;
				StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
				if (isEmpty(etEmail)) {
					validationError = true;
					validationErrorMessage.append(getResources().getString(R.string.error_blank_email));
				}
				if (isEmpty(etFirstName)) {
					if (validationError) {
						validationErrorMessage.append(getResources().getString(R.string.error_join));
					}
					validationError = true;
					validationErrorMessage.append(getResources().getString(R.string.error_blank_username));
				}
				if (isEmpty(etPassword)) {
					if (validationError) {
						validationErrorMessage.append(getResources().getString(R.string.error_join));
					}
					validationError = true;
					validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
				}

				validationErrorMessage.append(getResources().getString(R.string.error_end));

				// If there is a validation error, display the error
				if (validationError) {
					Toast.makeText(getActivity(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
					return;
				}

				// Set up a progress dialog
				final ProgressDialog dlg = new ProgressDialog(getActivity());
				dlg.setTitle("Please wait.");
				dlg.setMessage("Signing up.  Please wait...");
				dlg.show();

				// Set up a new Parse user
				ParseUser user = new ParseUser();
				user.setEmail(etEmail.getText().toString().trim());
				user.setUsername(etEmail.getText().toString().toString());
				user.setPassword(etPassword.getText().toString());
				user.put("fullName",etFirstName.getText().toString()+" "+ etLastName.getText().toString());
				// Call the Parse signup method
				user.signUpInBackground(new SignUpCallback() {

					@Override
					public void done(ParseException e) {
						dlg.dismiss();
						if (e != null) {
							Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
						} else {
							// Start an intent for the dispatch activity
							// Intent intent = new Intent(getActivity(),
							// DispatchActivity.class);
							// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
							// Intent.FLAG_ACTIVITY_NEW_TASK);
							// startActivity(intent);
						}
					}
				});
			}

		});
		etEmailLogIn=(EditText)v.findViewById(R.id.et_email_login);
		etPasswordLogin=(EditText)v.findViewById(R.id.et_password_log_in);
		bLogin=(Button)v.findViewById(R.id.b_login);
		bLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean validationError = false;
				StringBuilder validationErrorMessage = new StringBuilder(getResources().getString(R.string.error_intro));
				if (isEmpty(etEmailLogIn)) {
					validationError = true;
					validationErrorMessage.append(getResources().getString(R.string.error_blank_email));
				}
				if (isEmpty(etPasswordLogin)) {
					if (validationError) {
						validationErrorMessage.append(getResources().getString(R.string.error_join));
					}
					validationError = true;
					validationErrorMessage.append(getResources().getString(R.string.error_blank_password));
				}
				validationErrorMessage.append(getResources().getString(R.string.error_end));

				// If there is a validation error, display the error
				if (validationError) {
					Toast.makeText(getActivity(), validationErrorMessage.toString(), Toast.LENGTH_LONG).show();
					return;
				}
				
				// Set up a progress dialog
				final ProgressDialog dlg = new ProgressDialog(getActivity());
				dlg.setTitle("Please wait.");
				dlg.setMessage("Logging in.  Please wait...");
				dlg.show();
				
				//log in code
				
				String email=etEmailLogIn.getText().toString();
				String password=etPasswordLogin.getText().toString();
				//ParseUser.l
				ParseUser.logInInBackground(email, password, new LogInCallback() {
					
					@Override
					public void done(ParseUser user, ParseException excption) {
						dlg.dismiss();
						if(user!=null)
						{
							Toast.makeText(getActivity(),"Successfully logged in.",Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(getActivity(),excption.getMessage(),Toast.LENGTH_LONG).show();
						}
					}
				});
				
			}
		});

		loginSegment.setOnCheckedChangeListener(this);

		return v;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rb_signup:
			signUpLayout.setVisibility(View.VISIBLE);
			loginLayout.setVisibility(View.GONE);
			break;
		case R.id.rb_login:
			loginLayout.setVisibility(View.VISIBLE);
			signUpLayout.setVisibility(View.GONE);
			break;
		}

	}

	private boolean isEmpty(EditText etText) {
		if (etText.getText().toString().trim().length() > 0) {
			return false;
		} else {
			return true;
		}
	}

}
