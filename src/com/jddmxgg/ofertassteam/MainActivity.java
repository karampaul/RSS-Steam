package com.jddmxgg.ofertassteam;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SherlockFragmentActivity
{
	private final Handler handler = new Handler();
	private boolean useLogo = false;
	private boolean showHomeUp = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		final ActionBar ab = getSupportActionBar();

		// set defaults for logo & home up
		ab.setDisplayHomeAsUpEnabled(showHomeUp);
		ab.setDisplayUseLogoEnabled(useLogo);

		if (savedInstanceState == null)
			addRssFragment();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);

		// set up a listener for the refresh item
		final MenuItem refresh = (MenuItem) menu.findItem(R.id.menu_refresh);
		refresh.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			// on selecting show progress spinner for 1s
			public boolean onMenuItemClick(MenuItem item)
			{
				// item.setActionView(R.layout.progress_action);
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						refresh.setActionView(null);
					}
				}, 1000);
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
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
}