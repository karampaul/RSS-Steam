package com.jddmxgg.ofertassteam;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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
	private static final String RSS_LINK_OFERTASDEUNPANDA = "http://ofertasdeunpanda.com/feed/";
	private static final String RSS_LINK_VAYAANSIAS = "http://www.vayaansias.com/feeds/posts/default?alt=rss";

	public static final String ITEMS = "items";
	public static final String RECEIVER = "receiver";
	List<RssItem> rssItems = null;

	private String mColor = Constants.Colors.PURPLE.getColor();
	private ArrayList<Constants.Colors> Colors = new ArrayList<Constants.Colors>(Arrays.asList(Constants.Colors.values()));
	private int pos = 0;

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
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_OFERTASDEUNPANDA)));
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_VAYAANSIAS)));
			
			Collections.reverse(rssItems);
			Collections.sort(rssItems, new DayComparer());
			Collections.sort(rssItems, new MonthComparer());
			Collections.reverse(rssItems);

			setColors();
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

	private void setColors()
	{
		for (int i = 0; i < rssItems.size(); i++)
		{
			if (i > 0)
				if (!rssItems.get(i).getDay().equals(rssItems.get(i - 1).getDay()))
					pos++;
			if (pos >= Colors.size())
				pos = 0;

			mColor = Colors.get(pos).getColor();
			rssItems.get(i).setColor(mColor);
		}
	}

	public class MonthComparer implements Comparator<RssItem>
	{
		@Override
		public int compare(RssItem item1, RssItem item2)
		{
			Integer value1 = Integer.valueOf(item1.getMonth());
			Integer value2 = Integer.valueOf(item2.getMonth());
			return value1.compareTo(value2);
		}
	}

	public class DayComparer implements Comparator<RssItem>
	{
		@Override
		public int compare(RssItem item1, RssItem item2)
		{
			Integer value1 = Integer.valueOf(item1.getDay());
			Integer value2 = Integer.valueOf(item2.getDay());
			return value1.compareTo(value2);
		}
	}
}