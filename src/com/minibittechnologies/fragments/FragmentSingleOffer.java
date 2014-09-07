package com.minibittechnologies.fragments;

import org.woodyguthriecenter.app.R;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.minibittechnologies.interfaces.FragmentClickListener;
import com.minibittechnologies.model.Post;
import com.minibittechnologies.utility.Constants;

@SuppressLint("NewApi")
public class FragmentSingleOffer extends Fragment {

	public OffersTabFragment parent;

	private Post singleofferDetails;
	@SuppressWarnings("unused")
	private TextView tv_title, tv_message;
	// private ImageView img_pic;
	private ImageView backbuttonoftab;
	private TextView welcome_title;

	// private RelativeLayout topBar;

	// private FragmentClickListener fragClicker;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public FragmentSingleOffer(Post singleofferDetails) {
		this.singleofferDetails = singleofferDetails;
//		this.fragClicker = fragClicker;
	}

	public static Fragment newInstance(Post singleofferDetails) {
		return new FragmentSingleOffer(singleofferDetails);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_single_offer, container, false);
		tv_title = (TextView) v.findViewById(R.id.tv_title);
		tv_message = (TextView) v.findViewById(R.id.tv_message);
		// img_pic = (ImageView) v.findViewById(R.id.img_pic);
		// topBar = (RelativeLayout) v.findViewById(R.id.topBar);
		welcome_title = (TextView) v.findViewById(R.id.welcome_title);
		// backbuttonoftab = (ImageView) v.findViewById(R.id.backbuttonoftab);
		// backbuttonoftab.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // parent.onBackPressed();
		// // Back from the page
		// fragClicker.onFragmentItemClick(Constants.FRAG_LOGIN, false,null);
		// }
		// });
		welcome_title.setText("Post Details");
		tv_title.setText(singleofferDetails.getTitle());
		// tv_message.setText(singleofferDetails.getMessage());
		// ImageLoader.getInstance().displayImage(singleofferDetails.getImg_url(),img_pic);
		return v;
	}

	public void onResume() {
		super.onResume();
		// topBar=(RelativeLayout) findViewById(R.id.topBar);
		// welcome_title=(TextView) findViewById(R.id.welcome_title);
		// backbuttonoftab=(ImageView) findViewById(R.id.backbuttonoftab);
		// ((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new
		// OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// parent.onBackPressed();
		// }
		// });
		// ((HomeActivity)getActivity()).welcome_title.setText("Post Details");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		super.onPause();
		// ((HomeActivity)getActivity()).welcome_title.setText("Gebriel Horn");
	}

}
