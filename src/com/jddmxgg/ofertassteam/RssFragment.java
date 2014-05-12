package com.jddmxgg.ofertassteam;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Button;
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mDBHelper = new SQLiteHelper(getActivity().getApplicationContext(), "Feed", null, 1);
		setRetainInstance(true);
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
		}
		else
		{
			mSplashScreen.startAnimation(mAnimation);
			List<RssItem> items = mDBHelper.getValues();
			if (items != null && !items.isEmpty())
			{

				RssAdapter adapter = new RssAdapter(getActivity(), items);
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
			}

		}
	}

	public void reloadService(Intent i)
	{
		if (i != null)
			getActivity().stopService(i);
		startService();
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
			List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
			if (items != null)
			{
				RssAdapter adapter = new RssAdapter(getActivity(), items);
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
		dialog.setContentView(R.layout.filter);
		dialog.setTitle(getActivity().getResources().getString(R.string.filter));
		Button ok = (Button) dialog.findViewById(R.id.filter_ok);
		ok.setOnClickListener(new OnClickListener()
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