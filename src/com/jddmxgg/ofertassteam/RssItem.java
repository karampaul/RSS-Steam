package com.jddmxgg.ofertassteam;

/**
 * A representation of an rss item from the list.
 * 
 * @author Veaceslav Grec
 * 
 */
public class RssItem
{

	private final String title;
	private final String link;
	private final String color;

	public RssItem(String title, String link, String color)
	{
		this.title = title;
		this.link = link;
		this.color = color;
	}

	public String getTitle()
	{
		return title;
	}

	public String getLink()
	{
		return link;
	}

	public String getColor()
	{
		return color;
	}

}