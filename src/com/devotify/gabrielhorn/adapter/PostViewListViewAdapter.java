package com.devotify.gabrielhorn.adapter;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Locale;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.activity.HolderActivity;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.Utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class PostViewListViewAdapter extends ParseQueryAdapter<Post> {
	public PostViewListViewAdapter(final Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Post>() {
			@Override
			public ParseQuery<Post> create() {
				ParseQuery<Post> query = Post.getQuery();
				query.whereEqualTo("appCompany",Utils.appCompany);	
				query.addDescendingOrder("expiration");
				return query;
			}
		});
	}

	@Override
	public View getItemView(Post post, View v, ViewGroup parent) {
		if (v == null) {
			v = View.inflate(getContext(), R.layout.row_post, null);
		}

		super.getItemView(post, v, parent);

		ParseImageView todoImage = (ParseImageView) v.findViewById(R.id.iv_post_image);
		ParseFile imageFile = post.getParseFile("image");
		if (imageFile != null) {
			todoImage.setParseFile(imageFile);
			todoImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
				}
			});
		}

		final TextView tvTitle = (TextView) v.findViewById(R.id.tv_title);
		final TextView tvContent = (TextView) v.findViewById(R.id.tv_content);

		tvTitle.setText(post.getTitle());
		tvContent.setText(post.getContent());

		final ImageView ivExpired = (ImageView) v.findViewById(R.id.iv_expired);
		long currentTimeInMillis = System.currentTimeMillis();

		if (post.getExpired() != null) {
			long expiredTime = post.getExpired().getTime();
			if (expiredTime > currentTimeInMillis) {
				ivExpired.setVisibility(View.GONE);
			} else {
				ivExpired.setVisibility(View.VISIBLE);
			}
			Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
			String date = formatter.format(post.getExpired());
			// Log.e(">>>>>>>", "date in Date = " + post.getExpired() +
			// "   AND date in String = " + date);
		}

		return v;
	}
}
