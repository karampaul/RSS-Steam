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
	private final String description;

	public RssItem(String title, String link, String color, String description)
	{
		this.title = title;
		this.link = link;
		this.color = color;
		this.description = description;
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

	public String getDescription()
	{
		return description;
	}

}