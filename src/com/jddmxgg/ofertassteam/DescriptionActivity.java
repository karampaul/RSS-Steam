package com.jddmxgg.ofertassteam;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class DescriptionActivity extends Activity implements OnClickListener
{

	private TextView tvTitle;
	private TextView tvDescription;
	private Button tvGoToPage;

	private Uri uri;
	private String title;
	private String description;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description_layout);

		tvTitle = (TextView) findViewById(R.id.itemTitle);
		tvDescription = (TextView) findViewById(R.id.itemDescription);
		tvGoToPage = (Button) findViewById(R.id.btnGoToPage);

		title = getIntent().getExtras().getString("title");
		description = getIntent().getExtras().getString("description");
		uri = Uri.parse(getIntent().getExtras().getString("uri"));

		tvTitle.setText(title);
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
