package com.jddmxgg.ofertassteam;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class RssFragment extends Fragment implements OnItemClickListener, OnClickListener
{

	private ProgressBar mProgressBar;
	private ListView mListView;
	private ImageButton mRefreshButton;
	private View mView;
	private Intent mIntent;
	private RotateAnimation mRotateAnimation;
	private Bitmap mBitmap;
	private AdView adView;


	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (mView == null)
		{
			mView = inflater.inflate(R.layout.fragment_layout, container, false);
			mProgressBar = (ProgressBar) mView.findViewById(R.id.progressBar);
			mListView = (ListView) mView.findViewById(R.id.listView);
			mRefreshButton = (ImageButton) mView.findViewById(R.id.btnRefresh);
			mRefreshButton.setEnabled(false);
			
			 //Inicio meter publicidad 
	        adView = new AdView(getActivity(), AdSize.BANNER, Constants.ADMOB_PUBLISHER_ID);
	        LinearLayout layout = (LinearLayout) mView.findViewById(R.id.listaprincipal);
	        layout.addView(adView);
	        AdRequest request = new AdRequest();
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
			mRefreshButton.setOnClickListener(this);
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
			mIntent = new Intent(getActivity(), RssService.class);
			mIntent.putExtra(RssService.RECEIVER, resultReceiver);
			getActivity().startService(mIntent);
		}
		else
		{
			AlertDialog.Builder dialog = new Builder(getActivity());
			dialog.setMessage(getActivity().getResources().getString(R.string.msg_no_internet));
			dialog.setPositiveButton(getActivity().getResources().getString(android.R.string.ok), new DialogInterface.OnClickListener()
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

	private void reloadService(Intent i)
	{
		getActivity().stopService(i);
		getActivity().startService(mIntent);
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
			mProgressBar.setVisibility(View.GONE);
			List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
			if (items != null)
			{
				RssAdapter adapter = new RssAdapter(getActivity(), items);
				mListView.setAdapter(adapter);
				mRefreshButton.setEnabled(true);
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
//		intent.putExtra("title", item.getTitle());
//		intent.putExtra("description", item.getDescription());
//		intent.putExtra("uri", item.getLink());
		intent.putExtra("position", position);
		
		String day = "";
		String month = "";
		day = item.getDay();
		month =  item.getMonth();
		if(Integer.parseInt(day) < 10){
			day = "0" + day;
		}
		if(Integer.parseInt(month) < 10){
			month = "0" + month;
		}
				
		intent.putExtra("date", day + "/" + month);
		startActivity(intent);
		getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]>
	{
		@Override
		protected void onPostExecute(String[] result)
		{
			super.onPostExecute(result);
			mRotateAnimation.cancel();
		}

		@Override
		protected String[] doInBackground(Void... params)
		{
			reloadService(mIntent);
			return null;
		}
	}

	@Override
	public void onClick(View v)
	{
		if (v.getId() == R.id.btnRefresh)
		{
			mRefreshButton.startAnimation(mRotateAnimation);
			new GetDataTask().execute();
		}
	}
	
	public static void next()
	{
		
	}
	
}