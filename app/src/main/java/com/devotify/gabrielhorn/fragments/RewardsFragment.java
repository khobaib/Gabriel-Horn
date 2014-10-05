package com.devotify.gabrielhorn.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.adapter.RewardListAdapter;
import com.devotify.gabrielhorn.interfaces.ActivityResultListener;
import com.devotify.gabrielhorn.interfaces.RegisterActivityResultListener;
import com.devotify.gabrielhorn.model.Reward;
import com.devotify.gabrielhorn.utility.Constants;
import com.devotify.gabrielhorn.utility.FontUtils;
import com.devotify.gabrielhorn.utility.Fonts;
import com.dm.zbar.android.scanner.ZBarConstants;
import com.dm.zbar.android.scanner.ZBarScannerActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.sourceforge.zbar.Symbol;

import java.util.List;

public class RewardsFragment extends Fragment implements OnItemClickListener, ActivityResultListener
{
    private static final int ZBAR_QR_SCANNER_REQUEST = 1;

    private RewardListAdapter rewardListadapter;
    private Button btnScanForStar;
    private ListView rewardsListView;
    private TextView userRewardPointsTextView;

    public RewardsFragment()
    {
    }

    public static RewardsFragment newInstance()
    {
        return new RewardsFragment();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        ((RegisterActivityResultListener) activity).registerActivityResultListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_rewards, container, false);

        rewardListadapter = new RewardListAdapter(getActivity(), R.layout.row_list_reward);
        rewardsListView = (ListView) v.findViewById(R.id.rewards_list_view);
        rewardsListView.setAdapter(rewardListadapter);
        rewardsListView.setOnItemClickListener(this);

        btnScanForStar = (Button) v.findViewById(R.id.btnScanForStar);
        btnScanForStar.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                scanAndGetStar();
            }
        });

        userRewardPointsTextView = (TextView) v.findViewById(R.id.user_points_text_view);
        updateRewardsTextView();
        FontUtils.getInstance().overrideFonts(v, Fonts.LIGHT);
        return v;
    }

    private void scanAndGetStar()
    {
        if (isCameraAvailable())
        {
            Intent intent = new Intent(getActivity(), ZBarScannerActivity.class);
            intent.putExtra(ZBarConstants.SCAN_MODES, new int[]{Symbol.QRCODE});
            getActivity().startActivityForResult(intent, ZBAR_QR_SCANNER_REQUEST);
        }
        else
        {
            Toast.makeText(getActivity(), "Rear Facing Camera Unavailable", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isCameraAvailable()
    {
        PackageManager pm = getActivity().getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == ZBAR_QR_SCANNER_REQUEST)
            {
                final ProgressDialog dialog = ProgressDialog.show(getActivity(), "Loading", "Checking reward...");

                String contents = data.getStringExtra(ZBarConstants.SCAN_RESULT);
                ParseQuery<ParseObject> qrCodequery = ParseQuery.getQuery(Constants.OBJECT_QRCODE);
                qrCodequery.whereEqualTo(Constants.OBJECT_ID, contents);
                qrCodequery.findInBackground(new FindCallback<ParseObject>()
                {
                    @Override
                    public void done(List<ParseObject> qrCodeObject, ParseException e)
                    {
                        dialog.cancel();
                        if (e == null && qrCodeObject != null && qrCodeObject.size() > 0)
                        {
                            int pointsToAward = qrCodeObject.get(0).getInt(Constants.POINTS_TO_AWARD);

                            ParseUser user = ParseUser.getCurrentUser();
                            user.put(Constants.USER_REWARD_POINTS, user.getInt("rewardPoints") + pointsToAward);
                            user.saveInBackground(new SaveCallback()
                            {
                                @Override
                                public void done(ParseException paramParseException)
                                {
                                    updateRewardsTextView();
                                }
                            });
                        }
                        else
                        {
                            alert("Cannot find this product.");
                        }
                    }
                });
            }
        }
    }

    protected void alert(String message)
    {
        try
        {
            AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
            bld.setMessage(message);
            bld.setNeutralButton("Close", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    dialog.dismiss();
                }
            });
            bld.create().show();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        Reward reward = rewardListadapter.getItem(position);
        if (ParseUser.getCurrentUser().getInt("rewardPoints") < reward.getPointsNeeded())
        {
            Toast.makeText(getActivity(), "Sorry! You don't have enough stars.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showRewardOptionDialog(reward);
        }
    }

    private void updateWonReward(final Reward reward)
    {
        ParseObject rewardWon = new ParseObject("RewardsWon");
        rewardWon.put("wonReward", reward);
        rewardWon.put("winner", ParseUser.getCurrentUser());
        rewardWon.put("hasUserClaimed", true);
        rewardWon.saveInBackground(null);

        ParseUser.getCurrentUser().put("rewardPoints", ParseUser.getCurrentUser().getInt("rewardPoints") - reward.getPointsNeeded());
        ParseUser.getCurrentUser().saveInBackground(null);
        updateRewardsTextView();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(reward.getName()).setMessage("Show to employee to redeem").setNegativeButton(android.R.string.cancel, null);
        builder.show();
    }

    private void showRewardOptionDialog(final Reward reward)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("" + reward.get("name"));
        alert.setMessage("Do you want to use this reward?");
        alert.setPositiveButton("Redeem", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                updateWonReward(reward);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    public void updateRewardsTextView()
    {
        if (ParseUser.getCurrentUser() != null)
            userRewardPointsTextView.setText(Integer.toString(ParseUser.getCurrentUser().getInt("rewardPoints")));
    }
}