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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jddmxgg.ofertassteam.PullToRefreshListView.OnRefreshListener;

public class RssFragment extends Fragment implements OnItemClickListener
{

	private ProgressBar progressBar;
	private PullToRefreshListView listView;
	private View view;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (view == null)
		{
			view = inflater.inflate(R.layout.fragment_layout, container, false);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
			listView = (PullToRefreshListView) view.findViewById(R.id.listView);
			listView.setOnRefreshListener(new OnRefreshListener()
			{

				@Override
				public void onRefresh()
				{
					new GetDataTask().execute();
				}

			});
			listView.setOnItemClickListener(this);
			startService();
		}
		else
		{
			// If we are returning from a configuration change:
			// "view" is still attached to the previous view hierarchy
			// so we need to remove it and re-attach it to the current one
			ViewGroup parent = (ViewGroup) view.getParent();
			parent.removeView(view);
		}
		return view;
	}

	private void startService()
	{
		intent = new Intent(getActivity(), RssService.class);
		intent.putExtra(RssService.RECEIVER, resultReceiver);
		getActivity().startService(intent);
	}

	private void reloadService(Intent i)
	{
		getActivity().stopService(i);
		getActivity().startService(intent);
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
			progressBar.setVisibility(View.GONE);
			List<RssItem> items = (List<RssItem>) resultData.getSerializable(RssService.ITEMS);
			if (items != null)
			{
				RssAdapter adapter = new RssAdapter(getActivity(), items);
				listView.setAdapter(adapter);
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
		intent.putExtra("date", item.getDate());
		startActivity(intent);
	}

	private class GetDataTask extends AsyncTask<Void, Void, String[]>
	{
		@Override
		protected void onPostExecute(String[] result)
		{
			//mListItems.addFirst("Added after refresh...");
			// Call onRefreshComplete when the list has been refreshed.
			listView.onRefreshComplete();
			super.onPostExecute(result);
		}

		@Override
		protected String[] doInBackground(Void... params)
		{
			reloadService(intent);
			return null;
		}
	}
}