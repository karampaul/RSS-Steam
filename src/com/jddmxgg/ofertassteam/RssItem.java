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
	private final String mDescription;
	private final String mMonth;
	private final String mDay;
	private final String mColor;

	public RssItem(String title, String link, String description, String month, String day, String color)
	{
		mTitle = title;
		mLink = link;
		mDescription = description;
		mMonth = month;
		mDay = day;
		mColor = color;
	}

	public String getTitle()
	{
		return mTitle;
	}

	public String getLink()
	{
		return mLink;
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

	public String getColor()
	{
		return mColor;
	}
}