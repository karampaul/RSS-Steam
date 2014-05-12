package com.jddmxgg.ofertassteam;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RssAdapter extends BaseAdapter
{

	private final List<RssItem> mItems;
	private final Context mContext;
	
	public static List<RssItem> mStaticItems;

	public RssAdapter(Context context, List<RssItem> items)
	{
		this.mItems = items;
		this.mContext = context;
		RssAdapter.mStaticItems = items;
	}

	@Override
	public int getCount()
	{
		return mItems.size();
	}

	@Override
	public Object getItem(int position)
	{
		return mItems.get(position);
	}

	@Override
	public long getItemId(int id)
	{
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
			convertView = View.inflate(mContext, R.layout.rss_item, null);
			holder = new ViewHolder();
			holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.viewColor = (View) convertView.findViewById(R.id.view_color);
			holder.itemDate = (TextView) convertView.findViewById(R.id.date);
			holder.imageFeed = (ImageView) convertView.findViewById(R.id.feedImage);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		String title = mItems.get(position).getTitle();
		if (title.length() > 80)
			title = title.substring(0, 80) + "...";

		holder.itemTitle.setText(title);
		holder.viewColor.setBackgroundColor(Color.parseColor(mItems.get(position).getColor()));

		String day = "";
		String month = "";
		day = mItems.get(position).getDay();
		month = mItems.get(position).getMonth();
		if (Integer.parseInt(day) < 10)
			day = "0" + day;
		if (Integer.parseInt(month) < 10)
			month = "0" + month;
		holder.itemDate.setText(day + "/" + month);

		//Prueba fotos
		String link = mItems.get(position).getLink();
		if (link.substring(0, 28).equals("http://feedproxy.google.com/"))
			holder.imageFeed.setImageResource(R.drawable.huntgames);
		if (link.substring(0, 28).equals("http://ofertasdeunpanda.com/"))
			holder.imageFeed.setImageResource(R.drawable.ofertasdeunpanda);
		if (link.substring(0, 23).equals("http://steamofertas.com"))
			holder.imageFeed.setImageResource(R.drawable.steamofertas);
		if (link.substring(0, 26).equals("http://www.vayaansias.com/"))
			holder.imageFeed.setImageResource(R.drawable.vayaansias);
		//FIN prueba fotos

		return convertView;
	}
	
	static class ViewHolder
	{
		TextView itemTitle;
		TextView itemDate;
		View viewColor;
		ImageView imageFeed;
	}
}