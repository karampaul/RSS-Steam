package com.jddmxgg.ofertassteam;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RssAdapter extends BaseAdapter
{

	private final List<RssItem> items;
	private final Context context;

	public RssAdapter(Context context, List<RssItem> items)
	{
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount()
	{
		return items.size();
	}

	@Override
	public Object getItem(int position)
	{
		return items.get(position);
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
			convertView = View.inflate(context, R.layout.rss_item, null);
			holder = new ViewHolder();
			holder.itemTitle = (TextView) convertView.findViewById(R.id.itemTitle);
			holder.viewColor = (View) convertView.findViewById(R.id.view_color);
			holder.itemDate = (TextView) convertView.findViewById(R.id.date);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		String title = items.get(position).getTitle();
		if (title.length() > 80)
		{
			title = title.substring(0, 80) + "&#8230;";
		}

		holder.itemTitle.setText(title);
		holder.viewColor.setBackgroundColor(Color.parseColor(items.get(position).getColor()));
		
		String dia = "";
		String mes = "";
		dia = items.get(position).getDay();
		mes =  items.get(position).getMonth();
		if(Integer.parseInt(dia) < 10){
			dia = "0" + dia;
		}
		if(Integer.parseInt(mes) < 10){
			mes = "0" + mes;
		}
		holder.itemDate.setText(dia + "/" + mes);
		
		return convertView;
	}

	static class ViewHolder
	{
		TextView itemTitle;
		TextView itemDate;
		View viewColor;
	}
}