package com.devotify.gabrielhorn.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.PostViewListViewAdapter;
import com.devotify.gabrielhorn.model.Post;
import com.parse.ParseQueryAdapter;

import java.util.List;

public class FragmentPostList extends Fragment
{
    public interface FragmentPostItemClickedListener
    {
        public void onPostClicked(Post post);
    }

    private ParseQueryAdapter<Post> postAdapter;
    private ListView postsListView;
    private SwipeRefreshLayout postsRefreshLayout;
    private FragmentPostItemClickedListener postItemClickedListener;

    public static Fragment newInstance()
    {
        return new FragmentPostList();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        postItemClickedListener = (FragmentPostItemClickedListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_post, container, false);
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
                postItemClickedListener.onPostClicked(postAdapter.getItem(position));
            }
        });

        View footerView = getActivity().getLayoutInflater().inflate(R.layout.list_offer_footer, null, false);
        postsListView.addFooterView(footerView);
        postsListView.setAdapter(postAdapter);
        return rootView;
    }
}
