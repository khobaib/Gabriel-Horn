/**
 * 
 */
package com.minibittechnologies.fragments;

import org.woodyguthriecenter.app.R;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

/**
 * @author Touhid
 * 
 */
public class FragmentEditLocation extends Fragment implements OnMarkerDragListener {

	private final String TAG = this.getClass().getSimpleName();

	private GoogleMap gMap;

	private LatLng initPos = new LatLng(36.132894, -95.945205);

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
				alert("This saving is yet to complete!\nMsg to show: 'Loaction Saved.'");
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
	}

	@Override
	public void onMarkerDragEnd(Marker marker) {
		Log.i(TAG, "onMarkerDragEnd :: marker end pos. = " + marker.getPosition());
	}

	@Override
	public void onMarkerDragStart(Marker marker) {
		Log.i(TAG, "onMarkerDragStart :: marker start pos. = " + marker.getPosition());
	}
}
