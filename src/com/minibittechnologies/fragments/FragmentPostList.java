package com.minibittechnologies.fragments;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.minibittechnologies.R;
import com.minibittechnologies.adapter.PostViewListViewAdapter;
import com.minibittechnologies.model.Post;
import com.parse.ParseQueryAdapter;

@SuppressLint("NewApi")
public class FragmentPostList extends Fragment {

	List<Post> postList;
	// Adapter for the Parse query
	private ParseQueryAdapter<Post> postAdapter;
	// ListView lv_offer;
	// PullToRefreshListView list_view_latest_offer;
	ListView lvPost;
	// CustomAdapterForOfferDetails adapter;
	OffersTabFragment parent;
	// RelativeLayout re_top_top;
	RelativeLayout topBar;
	Boolean isTopBarHidden;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("onCreate", "homeonCreate");
		postList = new ArrayList<>();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_post, container, false);
		topBar = (RelativeLayout) v.findViewById(R.id.re_top);
		// re_top_top = (RelativeLayout) v.findViewById(R.id.re_top_top);

		lvPost = (ListView) v.findViewById(R.id.list_view_latest_offer);
		// listviewforoffer = list_view_latest_offer.getRefreshableView();

		isTopBarHidden = false;

		// loadarrlist();
		// listviewforoffer.setAdapter(adapter);
		lvPost.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Log.e(">>>>>>>", "pressed");
				// parent.StartSingleOfferFragment(list.get(position));
			}
		});

		return v;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Log.e("onActivityCreated", "onActivityCreated");

		postAdapter = new PostViewListViewAdapter(getActivity());

		// Set up a customized query
		// ParseQueryAdapter.QueryFactory<Post> factory = new
		// ParseQueryAdapter.QueryFactory<Post>() {
		// public ParseQuery<Post> create() {
		// ParseQuery<Post> query = Post.getQuery();
		// // query.whereEqualTo("", "");
		// return query;
		// }
		// };
		//
		// // Set up the query adapter
		// postAdapter = new ParseQueryAdapter<Post>(getActivity(), factory) {
		// @Override
		// public View getItemView(Post post, View view, ViewGroup parent) {
		// if (view == null) {
		// view = View.inflate(getContext(), R.layout.offer_details, null);
		// }
		//
		// super.getItemView(post, view, parent);
		//
		// TextView title = (TextView) view.findViewById(R.id.tv_title);
		// TextView message = (TextView) view.findViewById(R.id.tv_message);
		// // TextView usernameView = (TextView)
		// // view.findViewById(R.id.usernameView);
		// title.setText(post.getTitle());
		// message.setText(post.getContent());
		// return view;
		// }
		// };

		// Disable automatic loading when the adapter is attached to a view.
		// postAdapter.setAutoload(false);

		// Disable pagination, we'll manage the query limit ourselves
		// postAdapter.setPaginationEnabled(false);

		// Attach the query adapter to the view
		lvPost.setAdapter(postAdapter);

		lvPost.setOnScrollListener(new AbsListView.OnScrollListener() {
			private int mLastFirstVisibleItem;

			@Override
			public void onScrollStateChanged(AbsListView absListView, int i) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// ListView scrolledListView = (ListView) view;
				// Log.e(">>>>>>>>", "onscroll");

				int currentFirstVisibleItem = lvPost.getFirstVisiblePosition();
				if (currentFirstVisibleItem > mLastFirstVisibleItem) {
					onVisibilityUpdate(false); // Scrolling
					// down
				} else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
					onVisibilityUpdate(true); // Scrolling up
				}
				mLastFirstVisibleItem = currentFirstVisibleItem;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		doListQuery();

	}

	/*
	 * Set up a query to update the list view
	 */
	private void doListQuery() {
		// Refreshes the list view with new data based
		// usually on updated location data.
		postAdapter.loadObjects();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public void onVisibilityUpdate(boolean shouldShow) {
		if (shouldShow) {
			showTopBar();
		} else {
			hideTopBar();
		}
	}

	public void showTopBar() {
		if (isTopBarHidden) {
			ObjectAnimator animator = ObjectAnimator.ofFloat(topBar, "y", 0);
			ObjectAnimator animator2 = ObjectAnimator.ofFloat(lvPost, "y", topBar.getHeight());
			animator.setDuration(800);
			animator2.setDuration(800);
			animator.start();
			animator2.start();
			isTopBarHidden = false;
		}
	}

	public void hideTopBar() {
		if (!isTopBarHidden) {
			if (topBar.getHeight() == 0) {
				topBar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						topBar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						hideTopBar(); // Don't hide the bottom bar until we
						// actually know where it should go
						// when it becomes visible
					}
				});
			} else {
				ObjectAnimator animator = ObjectAnimator.ofFloat(topBar, "y", -topBar.getHeight());
				ObjectAnimator animator2 = ObjectAnimator.ofFloat(lvPost, "y", 0);
				animator.setDuration(800);
				animator2.setDuration(800);
				animator.start();
				animator2.start();

				lvPost.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2000));

				isTopBarHidden = true;
			}
		}
	}

	// public void loadarrlist() {
	// Post offerDetails = new Post();
	// list.add(offerDetails);
	// String message =
	// "Iphone 5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie,"
	// +
	// " download this wallpaper for free in HD resolution. Iphone 5S Camera Samples Untouched IPhone 5s"
	// +
	// " Camera Samples Photos Redmond Pie was posted in November 26, 2013 at 1:18 pm. This HD Wallpaper Iphone "
	// +
	// "5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie has viewed by 710 users. If you wanna have it"
	// +
	// " as yours, please click the wallpaper and you will go to page download, so you just choose the size above the wallpaper that"
	// + " you want in Download Size, Click it and download the "
	// +
	// "Iphone 5S Camera Samples Untouched IPhone 5s Camera Samples Photos Redmond Pie wallpaper.";
	// String url =
	// "http://www.lipberry.com//uploaded_content//article//1406.jpg";
	//
	// offerDetails = new Post("Iphone 5S Camera Samples Untouched I", message,
	// url, false);
	// list.add(offerDetails);
	// message =
	// "In all our business operations, we strive to play an integral role in society, sharing its values and working to create new value to help people around the world have healthier and more fulfilling lives."
	// +
	//
	// "This is the essence of the Olympus management philosophy of Social IN. As a member of society, Olympus places the utmost importance on its relationships with individuals. Accordingly, Olympus has adopted Social IN, a management philosophy that embraces social values as an essential part of its business ideology."
	// +
	//
	// "Olympus was founded with the declared purpose of manufacturing microscopes that would garner recognition in the global market. The impetus for this drive was a deep sense of duty to help advance medical development through the domestic manufacture of microscopes."
	// +
	//
	// "Since then, Olympus confirmed its position as a camera manufacturer and developed gastrocameras. Following this achievement, Olympus made a number of dreams come true through the application of Opto-Digital Technology, an area of core competence that integrates longstanding optical technology and state-of-the-art digital technology."
	// +
	//
	// "Your Vision, Our Future embodies our determination to realize dreams in a close bond with society while coexisting in harmony. Based on this corporate slogan, Olympus is pursuing innovation in order to become a company where all stakeholders, including shareholders, clients and employees, are proud of the Olympus brand.";
	// url = "http://www.lipberry.com//uploaded_content//article//1405.jpg";
	// offerDetails = new Post("E-620: Sample Images | Digital SLR |", message,
	// url, false);
	// list.add(offerDetails);
	// message =
	// "Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas. Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas.Beginning in 1934 as Japan's pioneering photographic film maker, Fujifilm has leveraged its imaging and information technology to become a global presence known for innovation in healthcare, graphic arts, optical devices, highly functional materials and other high-tech areas.";
	// url = "http://www.lipberry.com//uploaded_content//article//1402.jpg";
	// offerDetails = new Post("FUJIFILM X-S1 | Sample Images | Fujifilm",
	// message, url, false);
	// list.add(offerDetails);
	// adapter = new CustomAdapterForOfferDetails(getActivity(), list);
	// adapter.notifyDataSetChanged();
	// // list_view_latest_offer.onRefreshComplete();
	// }

}
