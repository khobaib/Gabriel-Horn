package com.minibittechnologies.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.zbar.Symbol;

import org.woodyguthriecenter.app.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.minibittechnologies.adapter.RewardListadapter;
import com.minibittechnologies.model.Reward;
import com.minibittechnologies.utility.Constants;
import com.minibittechnologies.utility.NestedListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentRewards extends Fragment {

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	NestedListView listView;
	TextView tvUserPoint;
	Activity activity;
	ArrayList<Reward> listRewards = new ArrayList<Reward>();
	RewardListadapter rewardListadapter;
	private Button btnScanForStar;
	int UserPoint;
	// private List<ParseObject> tempRewardList,alredyownList;
	// private ArrayList<ParseObject> expiredAwardList=new ArrayList<>();
	// private ArrayList<ParseObject> newAwardList=new ArrayList<>();
	private ArrayList<ParseObject> finalAwardList = new ArrayList<>();

	// private static final int SCANNER_REQUEST_CODE = 1111;

	// ArrayList<String> awardObjectIdList=new ArrayList<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rewards, container, false);
		listView = (NestedListView) v.findViewById(R.id.listView1);
		tvUserPoint = (TextView) v.findViewById(R.id.tv_user_point);

		UserPoint = ParseUser.getCurrentUser().getInt(Constants.USER_REWARD_POINTS);
		Log.e("points", "" + UserPoint);
		// initData();
		getRewardList();
		rewardListadapter = new RewardListadapter(getActivity(), R.layout.row_list_reward, finalAwardList);
		listView.setAdapter(rewardListadapter);
		// logOut();
		btnScanForStar = (Button) v.findViewById(R.id.btnScanForStar);
		btnScanForStar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				scanAndGetStar();
			}
		});
		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		activity = getActivity();
		if (activity != null) {
			tvUserPoint.setText("" + UserPoint);
		}
	}

	// private void scanAndGetStar() {
	// // go to fullscreen scan
	// Intent intent = new Intent("com.touhiDroid.android.SCAN");
	// // intent.putExtra(Intents.Scan.FORMATS, Intents.Scan.AZTEC_MODE + "," +
	// // Intents.Scan.PDF417_MODE + ","
	// // + Intents.Scan.QR_CODE_MODE + "," + Intents.Scan.DATA_MATRIX_MODE);
	// Log.d("SCAN_CLICK", "Starting scan");
	// startActivityForResult(intent, SCANNER_REQUEST_CODE);
	// }

	private void scanAndGetStar() {
		if (isCameraAvailable()) {
			Intent intent = new Intent(activity, ZBarScannerActivity.class);
			intent.putExtra(ZBarConstants.SCAN_MODES, new int[] { Symbol.QRCODE });
			startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
		} else {
			Toast.makeText(activity, "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean isCameraAvailable() {
		PackageManager pm = activity.getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode == Activity.RESULT_OK) {
			// Handle successful scan
			if (requestCode == ZBAR_QR_SCANNER_REQUEST) {
				// Handle scan intent
				String contents = data.getStringExtra(ZBarConstants.SCAN_RESULT);
				Log.e(">>>>>>>", "content = " + contents);

				ParseQuery<ParseObject> qrCodequery = ParseQuery.getQuery(Constants.OBJECT_QRCODE);
				qrCodequery.whereEqualTo(Constants.OBJECT_ID, contents);
				qrCodequery.findInBackground(new FindCallback<ParseObject>() {

					@Override
					public void done(List<ParseObject> qrCodeObject, ParseException e) {
						if (e == null) {
							int pointsToAward = qrCodeObject.get(0).getInt(Constants.POINTS_TO_AWARD);

							ParseUser user = ParseUser.getCurrentUser();
							user.put(Constants.USER_REWARD_POINTS, UserPoint + pointsToAward);
							user.saveInBackground(new SaveCallback() {

								@Override
								public void done(ParseException paramParseException) {
									ParseUser user = ParseUser.getCurrentUser();
									UserPoint = user.getInt(Constants.USER_REWARD_POINTS);
									tvUserPoint.setText("" + UserPoint);
								}
							});
						}

					}
				});
				// String formatName =
				// data.getStringExtra("SCAN_RESULT_FORMAT");
				// byte[] rawBytes =
				// data.getByteArrayExtra("SCAN_RESULT_BYTES");
				// int intentOrientation =
				// data.getIntExtra("SCAN_RESULT_ORIENTATION",
				// Integer.MIN_VALUE);
				// Integer orientation = (intentOrientation ==
				// Integer.MIN_VALUE) ? null : intentOrientation;
				// String errorCorrectionLevel =
				// data.getStringExtra("SCAN_RESULT_ERROR_CORRECTION_LEVEL");
				//
				// System.out.println("Contents: " + contents + "\n\n" +
				// "Format: " + formatName + "Orientation: "
				// + "\n\n" + orientation + "\n\n" + "Error Correction level: "
				// + errorCorrectionLevel);
				// Log.d("SCANNED_RES", "Contents: " + contents + "\n\n" +
				// "Format: " + formatName + "Orientation: "
				// + "\n\n" + orientation + "\n\n" + "Error Correction level: "
				// + errorCorrectionLevel);

			}
		}
	}

	@SuppressWarnings("unused")
	private void logOut() {
		ParseUser user = ParseUser.getCurrentUser();
		Log.e("MSG", user.getUsername());
		ParseUser.logOut();

	}

	private void getRewardList() {
		ParseQuery<ParseObject> rewardQuery = ParseQuery.getQuery(Constants.OBJECT_REWARDS);
		rewardQuery.whereLessThanOrEqualTo(Constants.REWARD_POINTS_NEEDED, UserPoint);
		rewardQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> rewardList, ParseException e) {
				if (e == null) {
					Log.e("SIZE AwardList", "" + rewardList.size());
					for (int i = 0; i < rewardList.size(); i++) {
						// reward=rewardList.get(i);
						final int pos = i;
						ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.OBJECT_REWARDSWON);

						query.whereEqualTo(Constants.WINNER, ParseUser.getCurrentUser());
						query.whereEqualTo(Constants.OWN_REWARD, rewardList.get(pos));
						query.whereEqualTo(Constants.HAS_USER_CLAIMED, false);
						query.findInBackground(new FindCallback<ParseObject>() {

							@Override
							public void done(List<ParseObject> list, ParseException e) {
								if (e == null) {
									if (list.size() == 0) {
										Log.e("SIZE NOT IN", "" + list.size());
										final ParseObject object = new ParseObject(Constants.OBJECT_REWARDSWON);
										Date date = calculateExpiryDate(rewardList.get(pos).getLong(
												Constants.MILIS_BEFORE_EXPIRATION));
										// Log.e("Time",""+reward.getLong(Constants.MILIS_BEFORE_EXPIRATION));
										object.put(Constants.EXPIRATION_DATE, date);
										object.put(Constants.WINNER, ParseUser.getCurrentUser());
										object.put(Constants.OWN_REWARD, rewardList.get(pos));
										object.put(Constants.HAS_USER_CLAIMED, false);
										object.saveInBackground(new SaveCallback() {

											@Override
											public void done(ParseException e) {
												if (e == null) {
													Log.e("success", "reward saved");

													ParseObject myObj = new ParseObject("Reward");
													myObj.put("name", rewardList.get(pos).get("name"));
													myObj.put(Constants.EXPIRATION_DATE,
															object.get(Constants.EXPIRATION_DATE));
													finalAwardList.add(myObj);

													rewardListadapter.notifyDataSetChanged();
												} else {
													Log.e("failed", e.toString());
												}

											}
										});

									} else if (list.size() > 0) {
										for (int j = 0; j < list.size(); j++) {
											ParseObject myObj = new ParseObject("Reward");
											myObj.put("name", rewardList.get(pos).get("name"));
											myObj.put(Constants.EXPIRATION_DATE,
													list.get(j).get(Constants.EXPIRATION_DATE));
											finalAwardList.add(myObj);
											rewardListadapter.notifyDataSetChanged();
										}
									}

								}

							}
						});

					}
					// getAvailableWonList();

				}

			}

		});

	}

	private Date calculateExpiryDate(long miliLeft) {
		long curMili = System.currentTimeMillis();
		long total = miliLeft + curMili;
		Date date = new Date(total);
		Log.e("date", date.toString());
		return date;

	}

	private void initData() {
		listRewards.add(new Reward("Reward 1", "7"));
		listRewards.add(new Reward("Reward 2", "17"));
	}

}
