package com.jddmxgg.ofertassteam;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class RssParser
{

	// We don't use namespaces
	private final String ns = null;
	SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);

	public List<RssItem> parse(InputStream inputStream) throws XmlPullParserException, IOException
	{
		try
		{
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readFeed(parser);
		}
		finally
		{
			inputStream.close();
		}
	}

	@SuppressWarnings("deprecation")
	private List<RssItem> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, null, "rss");
		int pos = 0;
		String title = null;
		String link = null;
		String description = null;
		String date = null;
		List<RssItem> items = new ArrayList<RssItem>();
		while (parser.next() != XmlPullParser.END_DOCUMENT)
		{
			if (parser.getEventType() != XmlPullParser.START_TAG)
			{
				continue;
			}
			String name = parser.getName();
			if (name.equals("title"))
			{
				title = readTitle(parser);
			}
			else if (name.equals("link"))
			{
				link = readLink(parser);
			}
			else if (name.equals("description"))
			{
				description = readDescription(parser);
			}
			else if (name.equals("pubDate"))
			{
				date = readDate(parser);
				try
				{
					Date d = formatter.parse(date);
					date = d.getDate() + "/" + (d.getMonth() + 1);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
			if (title != null && link != null && description != null)
			{
				// Aqui habrí que mirar de meter vayaansias y ofertas de un panda.
				if (!title.equals("Huntgames.es") && !title.equals("SteamOfertas") && !title.equals("Ofertas de un Panda") && !title.equals("-=VayaAnsias=-"))
				{

					RssItem item = new RssItem(title, link, Constants.COLORS[pos], description, date);
					items.add(item);
					pos++;
					if (pos >= Constants.COLORS.length)
						pos = 0;
				}
				title = null;
				link = null;
				description = null;
				date = null;
			}
		}
		return items;
	}

	//Pick the link to the web 
	private String readLink(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "link");
		String link = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "link");
		return link;
	}

	//Pick the date to the web 
	private String readDate(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "pubDate");
		String date = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "pubDate");
		return date;
	}

	//Pick the title of article
	private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "title");
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "title");
		return title;
	}

	//Pick the description of article 
	private String readDescription(XmlPullParser parser) throws XmlPullParserException, IOException
	{
		parser.require(XmlPullParser.START_TAG, ns, "description");
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, "description");
		return description;
	}

	// For the tags title and link, extract their text values.
	private String readText(XmlPullParser parser) throws IOException, XmlPullParserException
	{
		String result = "";
		if (parser.next() == XmlPullParser.TEXT)
		{
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

}