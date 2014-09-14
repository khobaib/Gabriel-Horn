package com.devotify.gabrielhorn.fragments;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.PostViewListViewAdapter;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.Constants;
import com.devotify.gabrielhorn.utility.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class FragmentPostList extends Fragment {
	private ParseQueryAdapter<Post> postAdapter; // Adapter for the Parse query
	private ListView lvPost;
	// private OffersTabFragment parent;
	private View topBar;

	private boolean isTopBarVisible;
	private final int HEADER_TRANSITION_DURATION = 150;

	public static final String DEBUG_TAG = "Minibit Debug";
	private ProgressDialog pDialog;

	View topBarClone;

	private PullToRefreshListView mPullToRefreshListView;

	private FragmentClickListener fragClicker;

	// /**
	// * Try never to use it! Rather call <br>
	// * {@code newInstance(FragmentClickListener fragClicker)}
	// */
	// public FragmentPostList() {
	// }
	//
	// private FragmentPostList(FragmentClickListener fragClicker) {
	// this.fragClicker = fragClicker;
	// }

	public static Fragment newInstance() {
		return new FragmentPostList();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		fragClicker = (FragmentClickListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// fragClicker = (FragmentClickListener)
		// getArguments().getSerializable(Constants.KEY_FRAG_CLICKER);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_post, container, false);

		// lvPost = (ListView)
		// rootView.findViewById(R.id.list_view_latest_offer);
		mPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.list_view_latest_offer);
		lvPost = this.mPullToRefreshListView.getRefreshableView();
		// View topBarClone = View.inflate(getActivity(),
		// R.layout.post_header_view, lvPost);
		// topBarClone =
		// getActivity().getLayoutInflater().inflate(R.layout.post_header_view,
		// null, false);
		// Log.e(">>>>>>>>>", "adding new top bar");
		// lvPost.addHeaderView(topBarClone);

		// isTopBarVisible = true;

		// View topBarClone = inflater.inflate(R.layout.post_header_view,
		// container, false);
		// lvPost.addHeaderView(topBarClone);
		// mPullToRefreshListView = (PullToRefreshListView)
		// rootView.findViewById(R.id.list_view_latest_offer);
		// lvPost = this.mPullToRefreshListView.getRefreshableView();
		//
		lvPost.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				Log.e(">>>>>>>", "pressed");
				fragClicker.onFragmentItemClick(Constants.FRAG_OFFER, false, (Post) parent.getItemAtPosition(position));
			}
		});
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				postAdapter.loadObjects();
				pDialog.show();
			}
		});
		// initHeaderListener();

		View footerView = getActivity().getLayoutInflater().inflate(R.layout.list_offer_footer, null, false);
		lvPost.addFooterView(footerView);

		// topBar =
		// getActivity().getLayoutInflater().inflate(R.layout.post_header_view,
		// container, false);
		// topBar.setVisibility(View.VISIBLE);
		// ((RelativeLayout) rootView).addView(topBar);

		return rootView;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(">>>>>>>>>", "removing new top bar");
		lvPost.removeHeaderView(topBarClone);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		pDialog = Utils.createProgressDialog(getActivity());
		pDialog.show();
		postAdapter = new PostViewListViewAdapter(getActivity());

		postAdapter.addOnQueryLoadListener(new OnQueryLoadListener<Post>() {

			@Override
			public void onLoaded(List<Post> arg0, Exception arg1) {

				if (pDialog.isShowing())
					pDialog.dismiss();
				// lvPost.addHeaderView(topBarClone);
				mPullToRefreshListView.onRefreshComplete();
				Log.e("size", "" + postAdapter.getCount());
			}

			@Override
			public void onLoading() {
				// topBarClone.setVisibility(View.VISIBLE);
				// postAdapter.clear();

			}

		});
		lvPost.setAdapter(postAdapter);
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
		// Refreshes the list view with new data based usually on updated
		// location data.
		ParseQuery<ParseObject> queryAppCompany = ParseQuery.getQuery("AppParentCompany");
		String appParentId = Utils.readString(getActivity(), Utils.KEY_PARENT_APP_ID, "");
		queryAppCompany.fromLocalDatastore();
		queryAppCompany.getInBackground(appParentId, new GetCallback<ParseObject>() {

			@Override
			public void done(ParseObject obj, ParseException e) {
				if (e == null) {
					Utils.appCompany = obj;
					postAdapter.loadObjects();
					Log.e("MSG", "got app company");
				}

			}
		});
	}

	public void onHeaderVisibilityUpdate(boolean shouldShow) {
		Log.e(">>>>>>>>>>>", "topbar visible = " + isTopBarVisible + " and shouldShow = " + shouldShow);
		if (!isTopBarVisible && shouldShow) {
			showTopBar();
		} else if (isTopBarVisible && !shouldShow) {
			hideTopBar();
		}
	}

	public void initHeaderListener() {
		lvPost.setOnScrollListener(new OnScrollListener() {
			private int previousScrollY = -1, currentScrollState;
			private final int UPDATE_HEADER_THRESHOLD = 20;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				currentScrollState = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				View firstChildView = lvPost.getChildAt(0);
				if (firstChildView != null && currentScrollState == SCROLL_STATE_TOUCH_SCROLL) {
					int topScrollPosition = -firstChildView.getTop() + lvPost.getFirstVisiblePosition()
							* firstChildView.getHeight();
					if (previousScrollY == -1) {
						previousScrollY = topScrollPosition;
					}

					int dy = topScrollPosition - previousScrollY;
					if (Math.abs(dy) > UPDATE_HEADER_THRESHOLD) {
						if (dy > 0) {
							// Log.e(DEBUG_TAG, "Scrolling down");
							onHeaderVisibilityUpdate(false);
						} else {
							// Log.e(DEBUG_TAG, "Scrolling up");
							onHeaderVisibilityUpdate(true);
						}

						// Log.e(DEBUG_TAG, topScrollPosition + ",	" + dy);
						previousScrollY = topScrollPosition;
					}
				}
			}
		});

	}

	public void showTopBar() {
		topBar.setVisibility(View.VISIBLE);
		ObjectAnimator showAnimator = ObjectAnimator.ofFloat(topBar, "y", -topBar.getHeight(), 0);
		showAnimator.setDuration(HEADER_TRANSITION_DURATION);
		showAnimator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				isTopBarVisible = true;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});

		showAnimator.start();
	}

	public void hideTopBar() {
		ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(topBar, "y", 0, -topBar.getHeight());
		hideAnimator.setDuration(HEADER_TRANSITION_DURATION);
		hideAnimator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
			}

			@Override
			public void onAnimationRepeat(Animator arg0) {
			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				isTopBarVisible = false;
			}

			@Override
			public void onAnimationCancel(Animator arg0) {
			}
		});

		hideAnimator.start();
	}

	// public OffersTabFragment getParent() {
	// return parent;
	// }
	//
	// public void setParent(OffersTabFragment parent) {
	// this.parent = parent;
	// }

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
}
