package com.jddmxgg.ofertassteam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class DescriptionActivity extends Activity implements OnClickListener
{

	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvDate;
	private Button btnGoToPage;
	private ImageView imgDescriptionWeb;

	private Uri uri;
	private String title;
	private String description;
	private String date;
	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description_layout);

		tvTitle = (TextView) findViewById(R.id.descriptionTitle);
		tvDescription = (TextView) findViewById(R.id.itemDescription);
		tvDate = (TextView) findViewById(R.id.itemDate);
		btnGoToPage = (Button) findViewById(R.id.btnGoToPage);
		imgDescriptionWeb = (ImageView) findViewById(R.id.descriptionImage);
		LinearLayout layout = (LinearLayout) findViewById(R.id.articulo);

		//Publicidad 
		adView = new AdView(this, AdSize.BANNER, Constants.ADMOB_PUBLISHER_ID);
		layout.addView(adView);
		AdRequest request = new AdRequest();
		adView.loadAd(request);
		adView.setVisibility(View.GONE);
		new CountDownTimer(3000, 1000)
		{

			@Override
			public void onTick(long millisUntilFinished)
			{
			}

			@Override
			public void onFinish()
			{
				adView.setVisibility(View.VISIBLE);
			}
		}.start();
		Context context = this.getApplicationContext();
		//FIN PUBLICIDAD
		//Analitycs
		EasyTracker tracker = EasyTracker.getInstance(context);
		tracker.set(Fields.SCREEN_NAME, "Descripcion Activity");
		tracker.send(MapBuilder.createAppView().build());
		//Fin analytics

		title = getIntent().getExtras().getString("title");
		description = getIntent().getExtras().getString("description");
		uri = Uri.parse(getIntent().getExtras().getString("uri"));
		date = getIntent().getExtras().getString("date");

		manageDescriptionByFeed();

		tvTitle.setText(title);
		tvDate.setText(date);
		tvDescription.setText(Html.fromHtml(description));
		btnGoToPage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		//analytics
		EasyTracker tracker = EasyTracker.getInstance(v.getContext());
		tracker.set(Fields.SCREEN_NAME, "Abriendo Enlace");
		tracker.send(MapBuilder.createAppView().build());
		//fin analytics
	}
	
	private void manageDescriptionByFeed()
	{
		if (uri.toString().substring(0, 28).equals("http://feedproxy.google.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.huntgames);
			description = description.replaceAll("<img(.*?)\\>", "");
			description = description.replaceAll("&lt", "<");
			description = description.replaceAll("&gt", ">");
			description = description.replaceAll("</li>", "<br><br>");
			description = description.replaceAll("<li>", "");
			description = description.replaceAll("<ul>", "");
			description = description.replaceAll("</ul>", "");
		}
		if (uri.toString().substring(0, 28).equals("http://ofertasdeunpanda.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.ofertasdeunpanda);
			description = description.replaceAll("<img(.*?)\\>", "");
			description = description.replaceAll("&lt", "<");
			description = description.replaceAll("&gt", ">");
			description = description.replaceAll("</li>", "<br><br>");
			description = description.replaceAll("<li>", "");
			description = description.replaceAll("<ul>", "");
			description = description.replaceAll("</ul>", "");
		}
		if (uri.toString().substring(0, 23).equals("http://steamofertas.com"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.steamofertas);
			description = description.replaceAll("<img(.*?)\\>", "");
			description = description.replaceAll("&lt", "<");
			description = description.replaceAll("&gt", ">");
			description = description.replaceAll("</li>", "<br><br>");
			description = description.replaceAll("<li>", "");
			description = description.replaceAll("<ul>", "");
			description = description.replaceAll("</ul>", "");
			description = description.replaceAll(" •", "<br>•");
			description = description.replaceAll(" - ", "<br>-");
			description = description.replaceAll("Paga", "<br><br>Paga");
		}
		if (uri.toString().substring(0, 26).equals("http://www.vayaansias.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.vayaansias);
			description = description.replaceAll("<img(.*?)\\>", "");
			description = description.replaceAll("&lt", "<");
			description = description.replaceAll("&gt", ">");
			description = description.replaceAll("</li>", "<br><br>");
			description = description.replaceAll("<li>", "");
			description = description.replaceAll("<ul>", "");
			description = description.replaceAll("</ul>", "");
		}
	}
}
