package com.devotify.gabrielhorn.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.zbar.Symbol;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.RewardListadapter;
import com.devotify.gabrielhorn.model.Reward;
import com.devotify.gabrielhorn.utility.Constants;
import com.devotify.gabrielhorn.utility.NestedListView;
import com.devotify.gabrielhorn.utility.Utils;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentRewards extends Fragment implements OnItemClickListener {

	private final String TAG = this.getClass().getSimpleName();

	private static final int ZBAR_QR_SCANNER_REQUEST = 1;

	private static final int BUTTON_POSITIVE = -1;
	private static final int BUTTON_NEGATIVE = -2;

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
	private ProgressDialog pDialog;

	public FragmentRewards() {
	}

	public static Fragment newInstance() {
		return new FragmentRewards();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_rewards, container, false);
		listView = (NestedListView) v.findViewById(R.id.listView1);

		tvUserPoint = (TextView) v.findViewById(R.id.tv_user_point);

		UserPoint = ParseUser.getCurrentUser().getInt(Constants.USER_REWARD_POINTS);
		Log.e("points", "" + UserPoint);
		// initData();
		pDialog = Utils.createProgressDialog(getActivity());
		pDialog.show();
		getAppCompany();
		rewardListadapter = new RewardListadapter(getActivity(), R.layout.row_list_reward, finalAwardList);
		listView.setAdapter(rewardListadapter);
		listView.setOnItemClickListener(this);

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

			// View footerView =
			// getActivity().getLayoutInflater().inflate(R.layout.list_offer_footer,
			// null, false);
			// listView.addFooterView(footerView);
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
						if (e == null && qrCodeObject != null && qrCodeObject.size() > 0) {
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
						} else {
							Log.d(TAG, "The QR-code is not supported!");
							alert("Cannot find this prouct.");
						}
					}
				});
			}
		}
	}

	protected void alert(String message) {
		try {
			AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
			bld.setMessage(message);
			bld.setNeutralButton("Close", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
			bld.create().show();
		} catch (Exception e) {
			Log.e(TAG, "Exception inside alert with message: " + message + "\n" + e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private void logOut() {
		ParseUser user = ParseUser.getCurrentUser();
		Log.e("MSG", user.getUsername());
		ParseUser.logOut();

	}

	private void getRewardList() {
		Log.e(">>>>>>>>", "getRewardList");
		// finalAwardList = new ArrayList<ParseObject>();
		 finalAwardList.clear();
		ParseQuery<ParseObject> rewardQuery = ParseQuery.getQuery(Constants.OBJECT_REWARDS);
		rewardQuery.whereLessThanOrEqualTo(Constants.REWARD_POINTS_NEEDED, UserPoint);
		rewardQuery.whereEqualTo("appCompany", Utils.appCompany);
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
													myObj.put(Constants.OWN_REWARD, object.get(Constants.OWN_REWARD));
													int points = rewardList.get(pos).getInt(
															Constants.REWARD_POINTS_NEEDED);
													Log.e("points", "" + points);
													myObj.put(Constants.REWARD_POINTS_NEEDED, points);
													finalAwardList.add(myObj);

													rewardListadapter.notifyDataSetChanged();
												} else {
													Log.e("failed", e.toString());
												}

											}
										});

									} else if (list.size() > 0) {
										Log.e("Award in view", "" + list.size());
										for (int j = 0; j < list.size(); j++) {
											ParseObject myObj = new ParseObject("Reward");
											myObj.put("name", rewardList.get(pos).get("name"));
											myObj.put(Constants.EXPIRATION_DATE,
													list.get(j).get(Constants.EXPIRATION_DATE));
											myObj.put(Constants.OWN_REWARD, list.get(j).get(Constants.OWN_REWARD));
											int points = rewardList.get(pos).getInt(Constants.REWARD_POINTS_NEEDED);
											Log.e("points", "" + points);
											myObj.put(Constants.REWARD_POINTS_NEEDED, points);
											finalAwardList.add(myObj);
											rewardListadapter.notifyDataSetChanged();
										}
									}

								}

							}
						});

					}
					if (pDialog.isShowing())
						pDialog.cancel();

				}

			}

		});

	}

	private Date calculateExpiryDate(long miliLeft) {
		long curMili = System.currentTimeMillis();
		long total = miliLeft + curMili;
		Date date = new Date(total);
		// Log.e("date", date.toString());
		return date;

	}

	@SuppressWarnings("unused")
	private void initData() {
		listRewards.add(new Reward("Reward 1", "7"));
		listRewards.add(new Reward("Reward 2", "17"));
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
		ParseObject reward = finalAwardList.get(position);
		if (isRewardExpired(reward)) {
			showExpiredDialog("" + reward.get("name"));
		} else {
			showRewardOptionDialog(reward);
		}

	}

	private void updateWonReward(final ParseObject reward) {
		if (!pDialog.isShowing())
			pDialog.show();
		ParseQuery<ParseObject> query = ParseQuery.getQuery(Constants.OBJECT_REWARDSWON);
		query.whereEqualTo(Constants.WINNER, ParseUser.getCurrentUser());
		query.whereEqualTo(Constants.EXPIRATION_DATE, reward.get(Constants.EXPIRATION_DATE));
		query.whereEqualTo(Constants.OWN_REWARD, reward.get(Constants.OWN_REWARD));
		query.whereEqualTo(Constants.HAS_USER_CLAIMED, false);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> list, ParseException e) {
				if (e == null) {
					Log.e("reedem list size", "" + list.size());
					if (list.size() == 1) {
						ParseObject wonReward = list.get(0);
						wonReward.put(Constants.HAS_USER_CLAIMED, true);
						wonReward.saveInBackground();
						setUserPoints(reward.getInt(Constants.REWARD_POINTS_NEEDED));

					}

				}

			}
		});

	}

	private void setUserPoints(int pointsUsed) {
		ParseUser user = ParseUser.getCurrentUser();
		UserPoint = UserPoint - pointsUsed;
		user.put(Constants.USER_REWARD_POINTS, UserPoint);
		user.saveInBackground();
		// finalAwardList.clear();
		// rewardListadapter.notifyDataSetChanged();
		tvUserPoint.setText("" + UserPoint);
		Log.e("up:", UserPoint + "   " + pointsUsed);
		getRewardList();

	}

	private boolean isRewardExpired(ParseObject reward) {
		Date expDate = reward.getDate(Constants.EXPIRATION_DATE);
		long mili = expDate.getTime();

		long curMili = System.currentTimeMillis();
		if (mili < curMili)
			return true;
		else
			return false;
	}

	private void getAppCompany() {
		ParseQuery<ParseObject> queryAppCompany = ParseQuery.getQuery("AppParentCompany");
		queryAppCompany.fromLocalDatastore();
		String appParentId = Utils.readString(getActivity(), Utils.KEY_PARENT_APP_ID, "");
		queryAppCompany.getInBackground(appParentId, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {
					Utils.appCompany = obj;
					getRewardList();
					Log.e("MSG", "got app company");
				}

			}
		});

	}

	private void showExpiredDialog(String name) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dilog_expired);
		TextView tvClose = (TextView) dialog.findViewById(R.id.tv_close);
		TextView tvGift = (TextView) dialog.findViewById(R.id.tv_name);
		tvGift.setText(name);
		tvClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}

		});
		dialog.show();
	}

	private void showRewardOptionDialog(final ParseObject reward) {

		AlertDialog Alert = new AlertDialog.Builder(getActivity()).create();
		Alert.setTitle("" + reward.get("name"));
		Alert.setMessage("Do you want to use this reward?");

		Alert.setButton(BUTTON_POSITIVE, "Redeem", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				updateWonReward(reward);
			}
		});

		Alert.setButton(BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
			}
		});
		Alert.show();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (pDialog.isShowing())
			pDialog.dismiss();
	}

}