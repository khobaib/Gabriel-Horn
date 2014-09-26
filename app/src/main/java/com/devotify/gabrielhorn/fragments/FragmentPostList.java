package com.devotify.gabrielhorn.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.PostViewListViewAdapter;
import com.devotify.gabrielhorn.interfaces.FragmentClickListener;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.Constants;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.parse.ParseQueryAdapter;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class FragmentPostList extends Fragment
{
    private boolean isTopVarVisible = true;
    private final int HEADER_TRANSITION_DURATION = 90;

    private ParseQueryAdapter<Post> postAdapter;
    private ListView postsListView;
    private SwipeRefreshLayout postsRefreshLayout;
    private ImageView appLogo;
    private FragmentClickListener fragClicker;

    public static Fragment newInstance()
    {
        return new FragmentPostList();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        fragClicker = (FragmentClickListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
        appLogo = (ImageView) rootView.findViewById(R.id.app_logo);

        postAdapter = new PostViewListViewAdapter(getActivity());
        postAdapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<Post>()
        {
            @Override
            public void onLoading()
            {

            }

            @Override
            public void onLoaded(List<Post> posts, Exception e)
            {
                postsRefreshLayout.setEnabled(false);
            }
        });

        postsRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.post_pull_refresh_view);
        postsRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
               postAdapter.loadObjects();
            }
        });

        postsListView = (ListView) rootView.findViewById(R.id.list_view_latest_offer);
        postsListView.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id)
            {
                fragClicker.onFragmentItemClick(Constants.FRAG_OFFER, false, (Post) parent.getItemAtPosition(position));
            }
        });

        View footerView = getActivity().getLayoutInflater().inflate(R.layout.list_offer_footer, null, false);
        postsListView.addFooterView(footerView);
        postsListView.setAdapter(postAdapter);
        initPostsListView();
        return rootView;
    }

    public void initPostsListView()
    {
        postsListView.addHeaderView(View.inflate(getActivity(), R.layout.posts_header_layout, null));
        postsListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                View firstChildView = view.getChildAt(0);
                if (firstChildView != null)
                {
                    int topScrollPosition = getScroll();
                    if (topScrollPosition > appLogo.getHeight() / 3)
                    {
                        hideTopBar();
                    }
                    else
                    {
                        showTopBar();
                    }
                }
            }

            private Dictionary<Integer, Integer> listViewItemHeights = new Hashtable<Integer, Integer>();

            private int getScroll()
            {
                View c = postsListView.getChildAt(0); //this is the first visible row
                int scrollY = -c.getTop();
                listViewItemHeights.put(postsListView.getFirstVisiblePosition(), c.getHeight());
                for (int i = 0; i < postsListView.getFirstVisiblePosition(); ++i)
                {
                    if (listViewItemHeights.get(i) != null) // (this is a sanity check)
                        scrollY += listViewItemHeights.get(i); //add all heights of the views that are gone
                }

                return scrollY;
            }
        });
    }

    public void showTopBar()
    {
        if (!isTopVarVisible)
        {
            ObjectAnimator showAnimator = ObjectAnimator.ofFloat(appLogo, "y", -appLogo.getHeight(), 0);
            showAnimator.setDuration(HEADER_TRANSITION_DURATION);
            showAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator arg0)
                {
                }

                @Override
                public void onAnimationRepeat(Animator arg0)
                {
                }

                @Override
                public void onAnimationEnd(Animator arg0)
                {
                    isTopVarVisible = true;
                }

                @Override
                public void onAnimationCancel(Animator arg0)
                {
                }
            });

            showAnimator.start();
        }
    }

    public void hideTopBar()
    {
        if (isTopVarVisible)
        {
            ObjectAnimator hideAnimator = ObjectAnimator.ofFloat(appLogo, "y", 0, -appLogo.getHeight());
            hideAnimator.setDuration(HEADER_TRANSITION_DURATION);
            hideAnimator.addListener(new Animator.AnimatorListener()
            {
                @Override
                public void onAnimationStart(Animator arg0)
                {
                }

                @Override
                public void onAnimationRepeat(Animator arg0)
                {
                }

                @Override
                public void onAnimationEnd(Animator arg0)
                {
                    isTopVarVisible = false;
                }

                @Override
                public void onAnimationCancel(Animator arg0)
                {
                }
            });

            hideAnimator.start();
        }
    }
}
