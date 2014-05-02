package com.jddmxgg.ofertassteam;

import java.util.List;

import android.content.Intent;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class RssFragment extends Fragment implements OnItemClickListener, OnClickListener
{

	private ProgressBar mProgressBar;
	private ListView mListView;
	private ImageButton mRefreshButton;
	private View mView;
	private Intent mIntent;
	private RotateAnimation mRotateAnimation;

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
			
			mRotateAnimation = new RotateAnimation(0f, 360f, 15f, 15f);
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
		mIntent = new Intent(getActivity(), RssService.class);
		mIntent.putExtra(RssService.RECEIVER, resultReceiver);
		getActivity().startService(mIntent);
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
		intent.putExtra("title", item.getTitle());
		intent.putExtra("description", item.getDescription());
		intent.putExtra("uri", item.getLink());
		intent.putExtra("date", item.getMonth());
		startActivity(intent);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]>
	{
		@Override
		protected void onPostExecute(String[] result)
		{
			//mListItems.addFirst("Added after refresh...");
			// Call onRefreshComplete when the list has been refreshed.
			//listView.onRefreshComplete();
			//mRotateAnimation.cancel();
			Constants.debug("POSTEXCUTEEEEEEEEEEEEEEE");
			super.onPostExecute(result);
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
			Constants.debug("CLIIIIIIIIIIIIIIIIIIIIIIIICK");
			new GetDataTask().execute();
		}
	}
}