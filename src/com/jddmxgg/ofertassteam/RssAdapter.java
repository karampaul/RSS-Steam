package com.jddmxgg.ofertassteam;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
			title = title.substring(0, 80) + "...";
		}

		holder.itemTitle.setText(title);
		holder.viewColor.setBackgroundColor(Color.parseColor(items.get(position).getColor()));
		
		String day = "";
		String month = "";
		day = items.get(position).getDay();
		month =  items.get(position).getMonth();
		if(Integer.parseInt(day) < 10){
			day = "0" + day;
		}
		if(Integer.parseInt(month) < 10){
			month = "0" + month;
		}
		holder.itemDate.setText(day + "/" + month);
		
		//Prueba fotos
		String link = items.get(position).getLink();
		String domain = "";
		if(link.substring(0,28).equals("http://feedproxy.google.com/"))
		{
			domain = "Huntgames";
			//holder.imageFeed.setImageResource(R.drawable.cerdo_ico);
		}
		if(link.substring(0,28).equals("http://ofertasdeunpanda.com/"))
		{
			domain = "Ofertas de un panda";
			//holder.imageFeed.setImageResource(R.drawable.panda_ico);
		}
		if(link.substring(0,23).equals("http://steamofertas.com"))//No rula
		{
			domain = "Steam Ofertas";
			//holder.imageFeed.setImageResource(R.drawable.mono_ico);
		}
		if(link.substring(0,26).equals("http://www.vayaansias.com/"))
		{
			domain = "Vaya ansias";
			//holder.imageFeed.setImageResource(R.drawable.ansias_ico);
		}
		Log.d("domain",domain);
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