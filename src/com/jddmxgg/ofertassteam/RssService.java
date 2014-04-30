package com.jddmxgg.ofertassteam;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class RssService extends IntentService
{

	private static final String RSS_LINK_HUNTGAMES = "http://feeds.feedburner.com/Huntgames_es?format=xml";
	private static final String RSS_LINK_STEAMOFERTAS = "http://steamofertas.com/feed/";
	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";
	List<RssItem> rssItems = null;

	public RssService()
	{
		super("RssService");
	}

	@Override
	protected void onHandleIntent(Intent intent)
	{
		loadRSS();

		Bundle bundle = new Bundle();
		bundle.putSerializable(ITEMS, (Serializable) rssItems);
		ResultReceiver receiver = intent.getParcelableExtra(RECEIVER);
		receiver.send(0, bundle);
	}

	public void loadRSS()
	{
		Log.d(Constants.TAG, "Service started");
		rssItems = null;
		try
		{
			RssParser parser = new RssParser();
			rssItems = parser.parse(getInputStream(RSS_LINK_HUNTGAMES));
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_STEAMOFERTAS)));
			Collections.sort(rssItems, new CustomComparator());
			Collections.reverse(rssItems);
		}
		catch (XmlPullParserException e)
		{
			Log.w(e.getMessage(), e);
		}
		catch (IOException e)
		{
			Log.w(e.getMessage(), e);
		}

	}

	public InputStream getInputStream(String link)
	{
		try
		{
			URL url = new URL(link);
			return url.openConnection().getInputStream();
		}
		catch (IOException e)
		{
			Log.w(Constants.TAG, "Exception while retrieving the input stream", e);
			return null;
		}
	}

	public class CustomComparator implements Comparator<RssItem>
	{
		@Override
		public int compare(RssItem item1, RssItem item2)
		{
			return item1.getDate().compareTo(item2.getDate());
		}
	}
}