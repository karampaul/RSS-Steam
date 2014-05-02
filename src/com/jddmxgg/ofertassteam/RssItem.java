package com.jddmxgg.ofertassteam;

/**
 * A representation of an rss item from the list.
 * 
 * @author Veaceslav Grec
 * 
 */
public class RssItem
{

	private final String mTitle;
	private final String mLink;
	private final String mColor;
	private final String mDescription;
	private final String mMonth;
	private final String mDay;

	public RssItem(String title, String link, String color, String description, String month, String day)
	{
		mTitle = title;
		mLink = link;
		mColor = color;
		mDescription = description;
		mMonth = month;
		mDay = day;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public String getLink()
	{
		return mLink;
	}

	public String getColor()
	{
		return mColor;
	}

	public String getDescription()
	{
		return mDescription;
	}

	public String getMonth()
	{
		return mMonth;
	}

	public String getDay()
	{
		return mDay;
	}

}