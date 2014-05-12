package com.jddmxgg.ofertassteam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends SherlockFragmentActivity
{
	private boolean useLogo = false;
	private boolean showHomeUp = false;
	private RssFragment mFragment;
	private Intent mIntent;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

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
		//		final MenuItem refresh = (MenuItem) menu.findItem(R.id.menu_refresh);
		//
		//		refresh.setOnMenuItemClickListener(this);

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
				break;
			case R.id.menu_option_about_us:
				ExpandibleListViewAdapter listAdapter = new ExpandibleListViewAdapter(this, listDataHeader, listDataChild);
				final Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.about_us);
				dialog.setTitle(getResources().getString(R.string.app_name));
				Button exit = (Button) dialog.findViewById(R.id.btn_about_us);
				ExpandableListView lv = (ExpandableListView) dialog.findViewById(R.id.expandableListView);
				lv.setAdapter(listAdapter);
				

				lv.setOnChildClickListener(new OnChildClickListener()
				{

					@Override
					public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
					{
						final String jedel = "com.iniris.preciodelaluz";
						final String skillath = "com.skillath.supersonicfingers";// getPackageName() from Context or Activity object
						try
						{
							if (groupPosition == 0)
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + jedel)));
							else
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + skillath)));
						}
						catch (android.content.ActivityNotFoundException anfe)
						{
							if (groupPosition == 0)
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + jedel)));
							else
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + skillath)));
						}
						return false;
					}
				});

				exit.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				dialog.show();
				break;
			case R.id.menu_option_salir:
				finish();
				break;
			case R.id.menu_option_filter:
				item.collapseActionView();
				if (mFragment != null)
					mFragment.showFilter(this);
				break;
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