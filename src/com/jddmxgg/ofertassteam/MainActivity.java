package com.jddmxgg.ofertassteam;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends FragmentActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		if (savedInstanceState == null)
		{
			addRssFragment();
		}
		EasyTracker.getInstance(this).activityStart(this);
	}

	private void addRssFragment()
	{
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		RssFragment fragment = new RssFragment();
		transaction.add(R.id.fragment_container, fragment);
		transaction.commit();
		//Google Analytics
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putBoolean("fragment_added", true);
	}
	
	
	
	
	/****************
	//@Override
	  public void onStart() {
	    super.onStart();
	    ... // The rest of your onStart() code.
	    EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	  }

	  //@Override
	  public void onStop() {
	    super.onStop();
	    ... // The rest of your onStop() code.
	    EasyTracker.getInstance(this).activityStop(this);  // Add this method.
	  }
	****************/
	
	
}