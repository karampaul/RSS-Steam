package com.jddmxgg.ofertassteam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class RssFragment extends SherlockFragment implements OnItemClickListener, AnimationListener
{
	private SQLiteHelper mDBHelper;
	private LinearLayout mSplashScreen;
	private ListView mListView;
	private View mView;
	private Intent mIntent;
	private RotateAnimation mRotateAnimation;
	private Bitmap mBitmap;
	private AdView adView;
	private Animation mAnimation;
	private CheckBox mOfertasDeUnPanda, mVayaAnsias, mSteamOfertas, mHuntgames;
	private List<RssItem> mAllItems;
	private List<String> listDataHeader;
	private HashMap<String, List<String>> listDataChild;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDBHelper = new SQLiteHelper(getActivity().getApplicationContext(), "Feed", null, 1);
		setRetainInstance(true);
		MainActivity.refresh.setEnabled(false);
	}

	@Override
	public void onPause()
	{
		adView.pause();
		super.onPause();
	}

	@Override
	public void onResume()
	{
		super.onResume();
		adView.resume();
	}

	@Override
	public void onDestroy()
	{
		adView.destroy();
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mView == null)
		{
			mView = inflater.inflate(R.layout.fragment_layout, container, false);
			mListView = (ListView) mView.findViewById(R.id.listView);
			mSplashScreen = (LinearLayout) mView.findViewById(R.id.splashScreen);
			mAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fade_out);

			//Inicio meter publicidad 
			adView = new AdView(getActivity());
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId(Constants.ADMOB_PUBLISHER_ID);
			LinearLayout layout = (LinearLayout) mView.findViewById(R.id.listaprincipal);
			layout.addView(adView);
			AdRequest request = new AdRequest.Builder().build();
			adView.loadAd(request);
			//Fin meter publicidad 

			mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_refresh);

			int height = mBitmap.getHeight();
			int width = mBitmap.getWidth();

			mRotateAnimation = new RotateAnimation(0f, 360f, width / 2, height / 2);
			mRotateAnimation.setInterpolator(new LinearInterpolator());
			mRotateAnimation.setRepeatCount(Animation.INFINITE);
			mRotateAnimation.setDuration(5000);

			mListView.setOnItemClickListener(this);
			mAnimation.setAnimationListener(this);
			startService();
		}
		else
		{
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) mView.getParent();
			parent.removeView(mView);
		}
		return mView;
	}

	private void startService()
	{
		if (Constants.internetConnectionEnabled(getActivity()))
		{
			RssService.setDatabase(mDBHelper);
			if (mIntent == null)
				mIntent = new Intent(getActivity(), RssService.class);
			mIntent.putExtra(RssService.RECEIVER, resultReceiver);
			getActivity().startService(mIntent);
			MainActivity.refresh.setEnabled(true);
		}
		else
		{
			mSplashScreen.startAnimation(mAnimation);
			mAllItems = mDBHelper.getValues();
			if (mAllItems != null && !mAllItems.isEmpty())
			{
				RssAdapter adapter = new RssAdapter(getActivity(), mAllItems);
				mListView.setAdapter(adapter);
			}
			else
			{
				AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
				dialog.setMessage(getActivity().getResources().getString(R.string.msg_no_internet));
				dialog.setPositiveButton(getActivity().getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				});
				dialog.setNegativeButton(getActivity().getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener()
				{

					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						getActivity().finish();
					}
				});
				dialog.show();
				MainActivity.refresh.setEnabled(true);
			}

		}
	}

	public void reloadService(Intent i)
	{
		MainActivity.refresh.setEnabled(false);
		if (i != null)
			getActivity().stopService(i);
		startService();
		if(mSplashScreen.getVisibility() != View.GONE)
			mSplashScreen.setVisibility(View.GONE);
	}

	/**
	 * Once the {@link RssService} finishes its task, the result is sent to this
	 * ResultReceiver.
	 */
	private final ResultReceiver resultReceiver = new ResultReceiver(new Handler())
	{
		@SuppressWarnings("unchecked")
		@Override
		protected void onReceiveResult(int resultCode, Bundle resultData)
		{
			mSplashScreen.startAnimation(mAnimation);
			mAllItems = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
			if (mAllItems != null)
			{
				RssAdapter adapter = new RssAdapter(getActivity(), mAllItems);
				mListView.setAdapter(adapter);
			}
			else
			{
				Toast.makeText(getActivity(), "An error occured while downloading the rss feed.", Toast.LENGTH_LONG).show();
			}
		};
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		RssAdapter adapter = (RssAdapter) parent.getAdapter();
		RssItem item = (RssItem) adapter.getItem(position);

		Intent intent = new Intent(parent.getContext(), DescriptionActivity.class);
		intent.putExtra("position", position);

		String day = "";
		String month = "";
		day = item.getDay();
		month = item.getMonth();
		if (Integer.parseInt(day) < 10)
			day = "0" + day;
		if (Integer.parseInt(month) < 10)
			month = "0" + month;

		intent.putExtra("date", day + "/" + month);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_left_activity, R.anim.slide_out_left_activity);
	}

	public void showFilter(Context context)
	{
		final Dialog dialog = new Dialog(context);
		dialog.dismiss();
		dialog.setContentView(R.layout.filter);
		dialog.setTitle(getActivity().getResources().getString(R.string.filter));

		mVayaAnsias = (CheckBox) dialog.findViewById(R.id.ch_vayaansias);
		mHuntgames = (CheckBox) dialog.findViewById(R.id.ch_huntgames);
		mOfertasDeUnPanda = (CheckBox) dialog.findViewById(R.id.ch_panda);
		mSteamOfertas = (CheckBox) dialog.findViewById(R.id.ch_steamofertas);

		mVayaAnsias.setChecked(true);
		mHuntgames.setChecked(true);
		mOfertasDeUnPanda.setChecked(true);
		mSteamOfertas.setChecked(true);

		Button ok = (Button) dialog.findViewById(R.id.filter_ok);
		ok.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				filter(mVayaAnsias.isChecked(), mHuntgames.isChecked(), mOfertasDeUnPanda.isChecked(), mSteamOfertas.isChecked());
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	public void showAboutUS(Context context)
	{
		prepareListData();
		ExpandibleListViewAdapter listAdapter = new ExpandibleListViewAdapter(context, listDataHeader, listDataChild);
		final Dialog dialog = new Dialog(context);
		dialog.dismiss();
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

	public void filter(boolean vayaansias, boolean huntgames, boolean ofertasdeunpanda, boolean steamofertas)
	{
		List<RssItem> filteredList = new ArrayList<RssItem>();
		if (mAllItems != null)
		{
			for (RssItem item : mAllItems)
			{
				if (huntgames)
					if (item.getLink().substring(0, 28).equals("http://feedproxy.google.com/"))
						filteredList.add(item);
				if (vayaansias)
					if (item.getLink().substring(0, 26).equals("http://www.vayaansias.com/"))
						filteredList.add(item);
				if (ofertasdeunpanda)
					if (item.getLink().substring(0, 28).equals("http://ofertasdeunpanda.com/"))
						filteredList.add(item);
				if (steamofertas)
					if (item.getLink().substring(0, 23).equals("http://steamofertas.com"))
						filteredList.add(item);
			}
		}
		if (filteredList != null)
			mListView.setAdapter(new RssAdapter(getActivity().getApplicationContext(), filteredList));
	}

	@Override
	public void onAnimationStart(Animation arg)
	{

	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		mSplashScreen.setVisibility(View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{

	}

}