package com.minibittechnologies.fragments;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.woodyguthriecenter.app.R;
import com.minibittechnologies.adapter.RewardListadapter;
import com.minibittechnologies.model.Reward;
import com.minibittechnologies.utility.Constants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FragmentRewards extends Fragment{
	ListView listView;
	ArrayList<Reward> listRewards=new ArrayList<Reward>();
	RewardListadapter rewardListadapter;
	int UserPoint;
	//private List<ParseObject> tempRewardList,alredyownList;
	//private ArrayList<ParseObject> expiredAwardList=new  ArrayList<>();
	//private ArrayList<ParseObject> newAwardList=new  ArrayList<>();
	private ArrayList<ParseObject> finalAwardList=new  ArrayList<>();
	//ArrayList<String> awardObjectIdList=new ArrayList<String>();
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		 View v=inflater.inflate(R.layout.fragment_rewards,container,false);
		 listView=(ListView)v.findViewById(R.id.listView1);
		 UserPoint=ParseUser.getCurrentUser().getInt(Constants.USER_REWARD_POINTS);
		 Log.e("points",""+UserPoint);
		// initData();
		 getRewardList();
		 rewardListadapter=new RewardListadapter(getActivity(),R.layout.row_list_reward, finalAwardList);
		 listView.setAdapter(rewardListadapter);
		// logOut();
		 return v;
	}
	private void logOut()
	{
		ParseUser user=ParseUser.getCurrentUser();
		 Log.e("MSG", user.getUsername());
		  ParseUser.logOut();
		
	}
	private void getRewardList()
	{
		ParseQuery<ParseObject> rewardQuery=ParseQuery.getQuery(Constants.OBJECT_REWARDS);
		rewardQuery.whereLessThanOrEqualTo(Constants.REWARD_POINTS_NEEDED,UserPoint);
		rewardQuery.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(final List<ParseObject> rewardList, ParseException e) {
				if(e==null)
				{
					Log.e("SIZE AwardList",""+rewardList.size());
					for(int i=0;i<rewardList.size();i++)
					{
						//reward=rewardList.get(i);
						final int pos=i;
						ParseQuery<ParseObject> query=ParseQuery.getQuery(Constants.OBJECT_REWARDSWON);
						
						query.whereEqualTo(Constants.WINNER,ParseUser.getCurrentUser());
						query.whereEqualTo(Constants.OWN_REWARD,rewardList.get(pos));
						query.whereEqualTo(Constants.HAS_USER_CLAIMED,false);
						query.findInBackground(new FindCallback<ParseObject>() {

							@Override
							public void done(List<ParseObject> list, ParseException e) {
								if(e==null)
								{
									if(list.size()==0)
									{
										Log.e("SIZE NOT IN", ""+list.size());
										final ParseObject object=new ParseObject(Constants.OBJECT_REWARDSWON);
										Date date=calculateExpiryDate(rewardList.get(pos).getLong(Constants.MILIS_BEFORE_EXPIRATION));
										//Log.e("Time",""+reward.getLong(Constants.MILIS_BEFORE_EXPIRATION));
										object.put(Constants.EXPIRATION_DATE,date );
										object.put(Constants.WINNER,ParseUser.getCurrentUser());
										object.put(Constants.OWN_REWARD,rewardList.get(pos));
										object.put(Constants.HAS_USER_CLAIMED,false);
										object.saveInBackground(new SaveCallback() {
											
											@Override
											public void done(ParseException e) {
												if(e==null)
												{
													Log.e("success","reward saved");
													
													ParseObject myObj=new ParseObject("Reward");
													myObj.put("name",rewardList.get(pos).get("name"));
													myObj.put(Constants.EXPIRATION_DATE,object.get(Constants.EXPIRATION_DATE));
													finalAwardList.add(myObj);
													
													rewardListadapter.notifyDataSetChanged();
												}
												else
												{
													Log.e("failed",e.toString());
												}
												
												
											}
										});
										
									}
									else if(list.size()>0)
									{
										for(int j=0;j<list.size();j++)
										{
											ParseObject myObj=new ParseObject("Reward");
											myObj.put("name",rewardList.get(pos).get("name"));
											myObj.put(Constants.EXPIRATION_DATE,list.get(j).get(Constants.EXPIRATION_DATE));
											finalAwardList.add(myObj);
											rewardListadapter.notifyDataSetChanged();
										}
									}
										
								}
								
								
							}
						});
						
						
					}
					//getAvailableWonList();
					
					
					
				}
				
			}

			
		});
		
		
	}
	private Date calculateExpiryDate(long miliLeft)
	{
		long curMili=System.currentTimeMillis();
		long total=miliLeft+curMili;
		Date date=new Date(total);
		Log.e("date",date.toString());
		return date;
		
	}
	private void initData()
	{
		listRewards.add(new Reward("Reward 1", "7"));
		listRewards.add(new Reward("Reward 2", "17"));
	}

}
