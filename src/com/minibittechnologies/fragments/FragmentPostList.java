package com.minibittechnologies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;

import com.minibittechnologies.R;
import com.minibittechnologies.adapter.PostViewListViewAdapter;
import com.minibittechnologies.model.Post;
import com.minibittechnologies.utility.ScrollReportListView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.parse.ParseQueryAdapter;

public class FragmentPostList extends Fragment
{
	private ParseQueryAdapter<Post> postAdapter; // Adapter for the Parse query
	private ScrollReportListView lvPost;
	private OffersTabFragment parent;
	private View topBar;
	
	private boolean isTopVarVisible;
	private final int HEADER_TRANSITION_DURATION = 150;
	
	public static final String DEBUG_TAG = "Minibit Debug";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View rootView = inflater.inflate(R.layout.fragment_post, container, false);
		View topBarClone = View.inflate(getActivity(), R.layout.post_header_view, lvPost);

		lvPost = (ScrollReportListView) rootView.findViewById(R.id.list_view_latest_offer);
		lvPost.addHeaderView(topBarClone);
		lvPost.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position, long id)
			{
				Log.e(">>>>>>>", "pressed");
			}
		});

		initHeaderListener();

		topBar = getActivity().getLayoutInflater().inflate(R.layout.post_header_view, container, false);
		topBar.setVisibility(View.INVISIBLE);
		((RelativeLayout) rootView).addView(topBar);

		return rootView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);

		postAdapter = new PostViewListViewAdapter(getActivity());
		lvPost.setAdapter(postAdapter);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		doListQuery();
	}

	/*
	 * Set up a query to update the list view
	 */
	private void doListQuery()
	{
		// Refreshes the list view with new data based usually on updated location data.
		postAdapter.loadObjects();
	}

	public void onHeaderVisibilityUpdate(boolean shouldShow)
	{
		if (!isTopVarVisible && shouldShow)
		{
			showTopBar();
		}
		else if (isTopVarVisible && !shouldShow)
		{
			hideTopBar();
		}
	}

	public void initHeaderListener()
	{
		lvPost.setOnScrollListener(new OnScrollListener()
		{
			private int previousScrollY = -1, currentScrollState;
			private final int UPDATE_HEADER_THRESHOLD = 20;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				currentScrollState = scrollState;
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
			{
				View firstChildView = lvPost.getChildAt(0);
				if (firstChildView != null && currentScrollState == SCROLL_STATE_TOUCH_SCROLL)
				{
					int topScrollPosition = -firstChildView.getTop() + lvPost.getFirstVisiblePosition() * firstChildView.getHeight();
					if (previousScrollY == -1)
					{
						previousScrollY = topScrollPosition;
					}

					int dy = topScrollPosition - previousScrollY;
					if (Math.abs(dy) > UPDATE_HEADER_THRESHOLD)
					{
						if (dy > 0)
						{
							Log.e(DEBUG_TAG, "Scrolling down");
							onHeaderVisibilityUpdate(false);
						}
						else
						{
							Log.e(DEBUG_TAG, "Scrolling up");
							onHeaderVisibilityUpdate(true);
						}

						Log.e(DEBUG_TAG, topScrollPosition + ",	" + dy);
						previousScrollY = topScrollPosition;
					}
				}
			}
		});

	}

	public void showTopBar()
	{
		topBar.setVisibility(View.VISIBLE);
		ObjectAnimator showAnimator = ObjectAnimator.ofFloat(topBar, "y", -topBar.getHeight(), 0);
		showAnimator.setDuration(HEADER_TRANSITION_DURATION);
		showAnimator.addListener(new AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator arg0)
			{}

			@Override
			public void onAnimationRepeat(Animator arg0)
			{}

			@Override
			public void onAnimationEnd(Animator arg0)
			{
				isTopVarVisible = true;
			}

			@Override
			public void onAnimationCancel(Animator arg0)
			{}
		});

		showAnimator.start();
	}

	public void hideTopBar()
	{
		ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(topBar, "y", 0, -topBar.getHeight());
		hideAnimator.setDuration(HEADER_TRANSITION_DURATION);
		hideAnimator.addListener(new AnimatorListener()
		{
			@Override
			public void onAnimationStart(Animator arg0)
			{}

			@Override
			public void onAnimationRepeat(Animator arg0)
			{}

			@Override
			public void onAnimationEnd(Animator arg0)
			{
				isTopVarVisible = false;
			}

			@Override
			public void onAnimationCancel(Animator arg0)
			{}
		});
		
		hideAnimator.start();
	}

	public OffersTabFragment getParent()
	{
		return parent;
	}

	public void setParent(OffersTabFragment parent)
	{
		this.parent = parent;
	}
}
