package com.devotify.gabrielhorn.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devotify.gabrielhorn.R;
import com.devotify.gabrielhorn.model.Post;
import com.devotify.gabrielhorn.utility.Utils;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class PostViewListViewAdapter extends ParseQueryAdapter<Post> {
	public PostViewListViewAdapter(final Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Post>() {
			@Override
			public ParseQuery<Post> create() {
				ParseQuery<Post> query = Post.getQuery();
				query.whereEqualTo("appCompany", Utils.appCompany);
				query.addDescendingOrder("expiration");
				return query;
			}
		});
	}

	@Override
	public View getItemView(Post post, View v, ViewGroup parent) {

		final ViewHolder holder;
		LayoutInflater mInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (v == null) {
			v = View.inflate(getContext(), R.layout.row_post, null);
			holder = new ViewHolder();
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		super.getItemView(post, v, parent);

		holder.tvTitle = (TextView) v.findViewById(R.id.tv_title);
		holder.tvContent = (TextView) v.findViewById(R.id.tv_content);
		holder.ivExpired = (ImageView) v.findViewById(R.id.iv_expired);
		holder.todoImage = (ParseImageView) v.findViewById(R.id.iv_post_image);

		ParseFile imageFile = post.getParseFile("image");
		if (imageFile != null) {
			holder.todoImage.setParseFile(imageFile);
			holder.todoImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
				}
			});
		}
		holder.tvTitle.setText(post.getTitle());
		holder.tvContent.setText(post.getContent());

		long currentTimeInMillis = System.currentTimeMillis();

		if (post.getExpired() != null) {
			long expiredTime = post.getExpired().getTime();
			if (expiredTime > currentTimeInMillis) {
				holder.ivExpired.setVisibility(View.GONE);
			} else {
				holder.ivExpired.setVisibility(View.VISIBLE);
			}
			// Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
			// Locale.getDefault());
			// String date = formatter.format(post.getExpired());
		}

		return v;
	}

	static class ViewHolder {
		TextView tvTitle;
		TextView tvContent;
		ImageView ivExpired;
		ParseImageView todoImage;
	}
}
