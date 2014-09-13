/**
 * 
 */
package com.devotify.gabrielhorn.fragments;

import java.text.DecimalFormat;
import java.util.List;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.utility.Utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

/**
 * @author Touhid
 * 
 */
public class FragmentEditLocation extends Fragment implements OnMarkerDragListener {

	private final String TAG = this.getClass().getSimpleName();

	private double latitude = 36.132894;
	private double longitude = -95.945205;
	private GoogleMap gMap;

	private LatLng initPos = new LatLng(36.132894, -95.945205);
	private ProgressDialog pDialog;

	public static Fragment newInstance() {
		return new FragmentEditLocation();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rv = inflater.inflate(R.layout.frag_edit_location, container, false);

		rv.findViewById(R.id.btnsaveEditLocation).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// alert("This saving is yet to complete!\nMsg to show: 'Loaction Saved.'");
				pDialog = Utils.createProgressDialog(getActivity());
				getAppCompany();
			}
		});

		SupportMapFragment mf = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(
				R.id.mapEditLocation));
		gMap = mf.getMap();
		// Marker hamburg =
		gMap.addMarker(new MarkerOptions().position(initPos).title("Hamburg").draggable(true));

		// Move the camera instantly to hamburg with a zoom of 15.
		gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPos, 15));
		// Zoom in, animating the camera.
		gMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
		gMap.setOnMarkerDragListener(this);

		return rv;
	}

	private void alert(String message) {
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

	@Override
	public void onMarkerDrag(Marker marker) {
		// marker.setTitle("108 Gallery");
		// marker.setSnippet(marker.getPosition().latitude+","+marker.getPosition().longitude);

	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		Log.i(TAG, "onMarkerDragEnd :: marker end pos. = " + marker.getPosition());
		marker.setTitle("108 Gallery");

		DecimalFormat df = getFormatter();
		String strLat = df.format(marker.getPosition().latitude);
		String strLon = df.format(marker.getPosition().longitude);
		marker.setSnippet(strLat + "," + strLon);
		latitude = Double.valueOf(strLat);
		longitude = Double.valueOf(strLon);
		marker.showInfoWindow();

	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		Log.i(TAG, "onMarkerDragStart :: marker start pos. = " + marker.getPosition());
		// marker.setTitle("108 Gallery");
		// marker.setSnippet(marker.getPosition().latitude+","+marker.getPosition().longitude);
		marker.hideInfoWindow();
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
					getCurrentLocation();
					Log.e("MSG", "got app company");
				}

			}
		});

	}

	private void getCurrentLocation() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("RetailLocation");
		query.whereEqualTo("appCompany", Utils.appCompany);
		query.whereEqualTo("name", "108 Gallery");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> list, ParseException e) {

				if (e == null && list.size() > 0) {
					updateLocation(list.get(0));
				}
			}
		});
	}

	private void updateLocation(ParseObject location) {

		ParseGeoPoint geoPoint = new ParseGeoPoint(latitude, longitude);
		location.put("location", geoPoint);
		location.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException e) {
				if (e == null) {
					alert("Location Updated.");
				} else {
					alert("Location Update Failed.");
				}
				if (pDialog.isShowing())
					pDialog.dismiss();
			}
		});
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		try {
			SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
					.findFragmentById(R.id.mapEditLocation);
			if (fragment != null)
				getFragmentManager().beginTransaction().remove(fragment).commit();

		} catch (IllegalStateException e) {
			// handle this situation because you are necessary will get
			// an exception here :(
		}
	}

	private DecimalFormat getFormatter() {

		DecimalFormat df = new DecimalFormat("#.000000");
		return df;
	}
}
