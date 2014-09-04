package com.minibittechnologies.fragments;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;

import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import com.minibittechnologies.fragments.FragmentMore.OnDataPass;
import com.minibittechnologies.utility.Utils;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LoginFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

	SegmentedGroup loginSegment;
	RelativeLayout signUpLayout, loginLayout;
	EditText etEmail, etFirstName, etLastName, etPassword, etEmailLogIn, etPasswordLogin;
	Button bSignUp, bLogin;
	OnDataPass dataPasser;
	private ProgressDialog pDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e(">>>>>>", "onCreate, LoginFragment");

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		dataPasser = (OnDataPass) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
               String appPackage=getActivity().getApplicationContext().getPackageName();
               Log.e("appPackage",appPackage);
				// Set up a progress dialog
				pDialog=Utils.createProgressDialog(getActivity());
				pDialog.show();
				ParseQuery<ParseObject> parseQuery=ParseQuery.getQuery("AppParentCompany");
				parseQuery.whereEqualTo("appIdentifier",appPackage);
				parseQuery.findInBackground(new FindCallback<ParseObject>() {
					
					@Override
					public void done(List<ParseObject> list, ParseException e) {
						if(e==null)
						{
							if(list.size()>0)
							{
								ParseObject appCompany=list.get(0);
								ParseUser user = new ParseUser();
								user.setEmail(etEmail.getText().toString().trim());
								user.setUsername(etEmail.getText().toString().toString());
								user.setPassword(etPassword.getText().toString());
								user.put("appCompany",appCompany);
								user.put("fullName", etFirstName.getText().toString() + " " + etLastName.getText().toString());
								// Call the Parse signup method
								user.signUpInBackground(new SignUpCallback() {

									@Override
									public void done(ParseException e) {
										pDialog.dismiss();
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
						}
						else
						{
							 Log.e("errre",e.toString());
						}
						
					}
				});
				
			}

		});
		etEmailLogIn = (EditText) v.findViewById(R.id.et_email_login);
		etPasswordLogin = (EditText) v.findViewById(R.id.et_password_log_in);
		bLogin = (Button) v.findViewById(R.id.b_login);
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
				pDialog=Utils.createProgressDialog(getActivity());
				pDialog.show();

				// log in code

				String email = etEmailLogIn.getText().toString();
				String password = etPasswordLogin.getText().toString();
				// ParseUser.l
				ParseUser.logInInBackground(email, password, new LogInCallback() {

					@Override
					public void done(ParseUser user, ParseException excption) {
						pDialog.dismiss();
						if (user != null) {
							Toast.makeText(getActivity(), "Successfully logged in.", Toast.LENGTH_LONG).show();
							dataPasser.onDataPass("rewardFragment");
						} else {
							Toast.makeText(getActivity(), excption.getMessage(), Toast.LENGTH_LONG).show();
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
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(pDialog.isShowing())
			pDialog.cancel();
	}

}
