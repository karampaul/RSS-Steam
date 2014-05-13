package com.jddmxgg.ofertassteam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class AboutUsActivity extends SherlockActivity
{
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		prepareListData();
		setContentView(R.layout.about_us);

		final ActionBar ab = getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(true);

		ExpandibleListViewAdapter listAdapter = new ExpandibleListViewAdapter(this, listDataHeader, listDataChild);
		ExpandableListView lv = (ExpandableListView) findViewById(R.id.expandableListView);
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
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
