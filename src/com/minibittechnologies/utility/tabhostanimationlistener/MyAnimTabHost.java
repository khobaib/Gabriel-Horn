package com.minibittechnologies.utility.tabhostanimationlistener;

import org.woodyguthriecenter.app.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TabHost;
import android.widget.Toast;

public class MyAnimTabHost extends FragmentTabHost {
	Context context;
@Override
public TabSpec newTabSpec(String tag) {
	// TODO Auto-generated method stub
	return super.newTabSpec(tag);
}
@Override
	public void addTab(TabSpec arg0, Class<?> arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		super.addTab(arg0, arg1, arg2);
	}
    public MyAnimTabHost(Context context) {
        super(context);
        context=this.context;
    }

    public MyAnimTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
        context=this.context;
    }

    @Override
    public void setCurrentTab(int index) {
    	FrameLayout currentView=(FrameLayout) findViewById(android.R.id.tabcontent);
        

        if ((index==0)||(index==3)){

            if (currentView !=null){
                Animation anim=AnimationUtils.loadAnimation(this.getContext(),android.R.anim.slide_in_left);
            	anim.setDuration(1000);
                currentView.startAnimation(anim);
               
            }

            super.setCurrentTab(index);

            currentView=(FrameLayout) findViewById(android.R.id.tabcontent);

            if (currentView !=null){
            	Animation anim=AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_in_right_toleft);
            	anim.setDuration(1000);
                currentView.startAnimation(anim);
//                Toast.makeText(  getContext(), "calling b", 2000).show();
                
            }
        } else {

            if (currentView !=null){
            	
            	Animation anim=AnimationUtils.loadAnimation(this.getContext(),R.anim.slide_out_bottom );
            	anim.setDuration(1000);
            	currentView.startAnimation(anim);
            }

            super.setCurrentTab(index);

            currentView=(FrameLayout) findViewById(android.R.id.tabcontent);

            if (currentView !=null){
            	
            	Animation anim=AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_in_bottom);
            	anim.setDuration(1000);
                currentView.startAnimation(anim);
            }

        }
    }

}
