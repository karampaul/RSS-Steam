package com.jddmxgg.ofertassteam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
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
	private boolean useLogo = false;
	private boolean showHomeUp = false;
	private RssFragment mFragment;
	public static MenuItem refresh;
	public static ActionBar mActionBar;
	private Intent mIntent;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;
	private final Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mActionBar = getSupportActionBar();

		// set defaults for logo & home up
		mActionBar.setDisplayHomeAsUpEnabled(showHomeUp);
		mActionBar.setDisplayUseLogoEnabled(useLogo);

		prepareListData();

		if (savedInstanceState == null)
			addRssFragment();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		getSupportMenuInflater().inflate(R.menu.main_menu, menu);

		// set up a listener for the refresh item
		refresh = (MenuItem) menu.findItem(R.id.menu_refresh);
		//
		refresh.setOnMenuItemClickListener(new OnMenuItemClickListener()
		{
			
			@Override
			public boolean onMenuItemClick(MenuItem item)
			{
				handler.postDelayed(new Runnable()
				{
					public void run()
					{
						refresh.setActionView(null);
					}
				}, 2000);
				return false;
			}
		});

		return super.onCreateOptionsMenu(menu);
	}

	private void addRssFragment()
	{
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		mFragment = new RssFragment();
		transaction.add(R.id.fragment_container, mFragment);
		transaction.commit();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		super.onSaveInstanceState(outState);
		outState.putBoolean("fragment_added", true);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case R.id.menu_refresh:
				item.setActionView(R.layout.indeterminate_progress_action);
				if (Constants.internetConnectionEnabled(this))
					new GetDataTask().execute();
				else
				{
					AlertDialog.Builder dialog = new AlertDialog.Builder(this);
					dialog.setMessage(getResources().getString(R.string.msg_no_internet));
					dialog.setPositiveButton(getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});
					dialog.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener()
					{

						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							finish();
						}
					});
					dialog.show();
				}
				return true;
			case R.id.menu_option_about_us:
				if (mFragment != null)
					mFragment.showAboutUS(this);
				return true;
			case R.id.menu_option_salir:
				finish();
				break;
			case R.id.menu_option_filter:
				item.collapseActionView();
				if (mFragment != null)
					mFragment.showFilter(this);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
		return false;
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]>
	{
		@Override
		protected void onPostExecute(String[] result)
		{
			super.onPostExecute(result);
		}

		@Override
		protected String[] doInBackground(Void... params)
		{
			if (mFragment != null)
				mFragment.reloadService(mIntent);
			return null;
		}
	}

	private void prepareListData()
	{
		listDataHeader = new ArrayList<String>();
		listDataChild = new HashMap<String, List<String>>();

		// Adding child data
		listDataHeader.add(getResources().getString(R.string.jedelwey));
		listDataHeader.add(getResources().getString(R.string.skillath));

		// Adding child data
		List<String> jedel = new ArrayList<String>();
		jedel.add(getResources().getString(R.string.elpreciodelaluz));

		List<String> skillath = new ArrayList<String>();
		skillath.add(getResources().getString(R.string.SupersonicFingers));

		listDataChild.put(listDataHeader.get(0), jedel); // Header, Child data
		listDataChild.put(listDataHeader.get(1), skillath);
	}
}