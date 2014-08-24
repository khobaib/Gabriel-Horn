package com.minibittechnologies.utility;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class ScrollReportListView extends ListView
{
	public static interface OnScrollListener
	{
		public void onScrollChanged(int x, int y, int oldx, int oldy);
	}

	public enum ScrollDirections
	{
		IDLE, DOWN, UP
	}

	private OnScrollListener mOnScrollListener;
	private ScrollDirections currentScrollDirection;

	public ScrollReportListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public ScrollReportListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public ScrollReportListView(Context context)
	{
		super(context);
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy)
	{}

	public void setScrollMovementListener(OnScrollListener scrollListener)
	{
		mOnScrollListener = scrollListener;
	}

	public ScrollDirections getCurrentScrollDirection()
	{
		return currentScrollDirection;
	}
}
