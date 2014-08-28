package com.minibittechnologies.fragments;

import java.util.ArrayList;
import java.util.List;

import com.minibittechnologies.R;
import com.minibittechnologies.adapter.RewardListadapter;
import com.minibittechnologies.model.Reward;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentRewards extends Fragment{
	ListView listView;
	ArrayList<Reward> listRewards=new ArrayList<Reward>();
	RewardListadapter rewardListadapter;
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View v=inflater.inflate(R.layout.fragment_rewards,container,false);
		 listView=(ListView)v.findViewById(R.id.listView1);
		 initData();
		 rewardListadapter=new RewardListadapter(getActivity(),R.layout.rewards_list_row, listRewards);
		 listView.setAdapter(rewardListadapter);
		 return v;
	}
	private void initData()
	{
		listRewards.add(new Reward("Reward 1", "7"));
		listRewards.add(new Reward("Reward 2", "17"));
	}

}
