package com.jddmxgg.ofertassteam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class DescriptionActivity extends Activity implements OnClickListener
{

	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvDate;
	private Button tvGoToPage;

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
		tvGoToPage = (Button) findViewById(R.id.btnGoToPage);
		LinearLayout layout = (LinearLayout) findViewById(R.id.articulo);

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

		title = getIntent().getExtras().getString("title");
		description = getIntent().getExtras().getString("description");
		uri = Uri.parse(getIntent().getExtras().getString("uri"));
		date = getIntent().getExtras().getString("date");

		description = description.replaceAll("<img(.*?)\\>", "");

		tvTitle.setText(title);
		tvDate.setText(date);
		tvDescription.setText(Html.fromHtml(description));
		tvGoToPage.setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
	}
}
