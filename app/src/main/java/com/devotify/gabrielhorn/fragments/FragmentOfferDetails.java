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
public class FragmentOfferDetails extends Fragment
{
    private Post singleofferDetails;
    private TextView tv_title, tv_message;
    private ImageView backbuttonoftab;
    private TextView welcome_title;

    public static FragmentOfferDetails newInstance(Post singleofferDetails)
    {
        FragmentOfferDetails f = new FragmentOfferDetails();
        Bundle args = new Bundle();
        args.putSerializable("post", singleofferDetails);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.singleofferDetails = (Post) getArguments().get("post");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_offer_details, container, false);
        tv_title = (TextView) v.findViewById(R.id.tv_title);
        tv_message = (TextView) v.findViewById(R.id.tv_message);
        welcome_title = (TextView) v.findViewById(R.id.welcome_title);
        welcome_title.setText("Post Details");
        tv_title.setText(singleofferDetails.getTitle());
        tv_message.setText(singleofferDetails.getContents());

        ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.img_pic);
        ParseFile imageFile = singleofferDetails.getParseFile("image");
        if (imageFile != null)
        {
            todoImage.setParseFile(imageFile);
            todoImage.loadInBackground(new GetDataCallback()
            {
                @Override
                public void done(byte[] data, ParseException e)
                {
                }
            });
        }

        return v;
    }
}
