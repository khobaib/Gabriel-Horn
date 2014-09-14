package com.devotify.gabrielhorn.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.model.Post;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;

@SuppressLint("NewApi")
public class FragmentSingleOffer extends Fragment {

	// public OffersTabFragment parent;

	private Post singleofferDetails;
	private TextView tv_title, tv_message;
	// private ImageView img_pic;
	@SuppressWarnings("unused")
	private ImageView backbuttonoftab;
	private TextView welcome_title;

	public static FragmentSingleOffer newInstance(Post singleofferDetails) {
		FragmentSingleOffer f = new FragmentSingleOffer();
		Bundle args = new Bundle();
		args.putSerializable("post", singleofferDetails);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.singleofferDetails = (Post) getArguments().get("post");
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
		tv_message.setText(singleofferDetails.getContent());

		ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.img_pic);
		ParseFile imageFile = singleofferDetails.getParseFile("image");
		if (imageFile != null) {
			todoImage.setParseFile(imageFile);
			todoImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
				}
			});
		}
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
		super.onPause();
		// ((HomeActivity)getActivity()).welcome_title.setText("Gebriel Horn");
	}

}
