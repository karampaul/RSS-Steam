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
	private static final String RSS_LINK_OFERTASDEUNPANDA = "http://ofertasdeunpanda.com/feed/";
	private static final String RSS_LINK_VAYAANSIAS = "http://www.vayaansias.com/feeds/posts/default?alt=rss";
	private static final String RSS_LINK_PRUEBA = "http://jedelwey2.wordpress.com/feed/";

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
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_OFERTASDEUNPANDA)));
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_VAYAANSIAS)));
			rssItems.addAll(parser.parse(getInputStream(RSS_LINK_PRUEBA)));
			
			Collections.sort(rssItems, new DayComparer());
			Collections.sort(rssItems, new MonthComparer());
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

	public class MonthComparer implements Comparator<RssItem>
	{
		@Override
		public int compare(RssItem item1, RssItem item2)
		{

			return item1.getMonth().compareTo(item2.getMonth());
		}
	}

	public class DayComparer implements Comparator<RssItem>
	{
		@Override
		public int compare(RssItem item1, RssItem item2)
		{

			return item1.getDay().compareTo(item2.getDay());
		}
	}
}