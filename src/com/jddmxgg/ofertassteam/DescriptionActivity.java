package com.jddmxgg.ofertassteam;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
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
import com.jddmxgg.ofertassteam.SimpleGestureFilter.SimpleGestureListener;

public class DescriptionActivity extends Activity implements OnClickListener, SimpleGestureListener, AnimationListener
{

	private TextView tvTitle;
	private TextView tvDescription;
	private TextView tvDate;
	private Button btnGoToPage;
	private ImageView imgDescriptionWeb;
	private View mDescriptionColorView;
	private AdView adView;
	private Animation mHideSlideLeft;
	private Animation mShowSlideLeft;
	private Animation mShowSlideRight;
	private Animation mHideSlideRight;
	private LinearLayout mLayout;
	private LinearLayout mAnimationLayout;

	private Uri mUri;
	private String mTitle;
	private String mDescription;
	private String mDate;
	private String mColor;

	private int mPosition;

	private SimpleGestureFilter mDetector;

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
		mDescriptionColorView = (View) findViewById(R.id.descriptionColor);
		mLayout = (LinearLayout) findViewById(R.id.articulo);
		mAnimationLayout = (LinearLayout) findViewById(R.id.animationLayout);

		mHideSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left);
		mShowSlideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
		mShowSlideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
		mHideSlideRight = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_right);

		mDetector = new SimpleGestureFilter(this, this);

		//Publicidad 
		adView = new AdView(this, AdSize.BANNER, Constants.ADMOB_PUBLISHER_ID);
		mLayout.addView(adView);
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

		mPosition = getIntent().getExtras().getInt("position");

		getItemInPosition(RssAdapter.mStaticItems, mPosition);

		btnGoToPage.setOnClickListener(this);
		mHideSlideLeft.setAnimationListener(this);
		mShowSlideLeft.setAnimationListener(this);
		mHideSlideRight.setAnimationListener(this);
		mShowSlideRight.setAnimationListener(this);
	}

	@Override
	public void onClick(View v)
	{
		Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
		startActivity(intent);
		//analytics
		EasyTracker tracker = EasyTracker.getInstance(v.getContext());
		tracker.set(Fields.SCREEN_NAME, "Abriendo Enlace");
		tracker.send(MapBuilder.createAppView().build());
		//fin analytics
	}

	private void loadTexts()
	{
		manageDescriptionByFeed();

		tvTitle.setText(mTitle);
		tvDate.setText(mDate);
		mDescriptionColorView.setBackgroundColor(Color.parseColor(mColor));
		tvDescription.setText(Html.fromHtml(mDescription));
	}

	private void manageDescriptionByFeed()
	{
		if (mUri.toString().substring(0, 28).equals("http://feedproxy.google.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.huntgames);
			mDescription = mDescription.replaceAll("<img(.*?)\\>", "");
			mDescription = mDescription.replaceAll("&lt", "<");
			mDescription = mDescription.replaceAll("&gt", ">");
			mDescription = mDescription.replaceAll("</li>", "<br><br>");
			mDescription = mDescription.replaceAll("<li>", "");
			mDescription = mDescription.replaceAll("<ul>", "");
			mDescription = mDescription.replaceAll("</ul>", "");
			mDescription = mDescription.replaceAll("Acceso anticipado.", "Acceso anticipado.<br><br>");
			mDescription = mDescription.replaceAll("Pues eso.", "Pues eso.<br><br>");
			mDescription = mDescription.replaceAll("Con la ", "<br>Con la ");
			mDescription = mDescription.replaceAll("Y como ", "<br><br>Y como ");
			mDescription = mDescription.replaceAll("Instucciones en los comentarios del enlace.", "Instrucciones en los comentarios del enlace.<br>");
			mDescription = mDescription.replaceAll("\\.", ".<br>");
		}
		if (mUri.toString().substring(0, 28).equals("http://ofertasdeunpanda.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.ofertasdeunpanda);
			mDescription = mDescription.replaceAll("<img(.*?)\\>", "");
			mDescription = mDescription.replaceAll("&lt", "<");
			mDescription = mDescription.replaceAll("&gt", ">");
			mDescription = mDescription.replaceAll("</li>", "<br><br>");
			mDescription = mDescription.replaceAll("<li>", "");
			mDescription = mDescription.replaceAll("<ul>", "");
			mDescription = mDescription.replaceAll("</ul>", "");
			mDescription = mDescription.replaceAll("\\)", ")<br>");
			mDescription = mDescription.replaceAll("Archivado en:", "<br><br>Archivado en:<br>");
			mDescription = mDescription.replaceAll("\\(Cambio", "<br>(Cambio");
			mDescription = mDescription.replaceAll("€ ", "€<br>• ");
			mDescription = mDescription.replaceAll("horas:", "horas:<br><br>• ");
			mDescription = mDescription.replaceAll("• <br><br>", "<br><br>");
			mDescription = mDescription.replaceAll(". Chaos", "• Chaos");
			mDescription = mDescription.replaceAll("1:", "1:<br><br>• ");
			mDescription = mDescription.replaceAll(" €\\)", " €)<br>");
			mDescription = mDescription.replaceAll("bundle:", "bundle:<br><br>");
			mDescription = mDescription.replaceAll(" Precios sin aplicar Gala Points:", "Precios sin aplicar Gala Points:<br><br>");
		}
		if (mUri.toString().substring(0, 23).equals("http://steamofertas.com"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.steamofertas);
			mDescription = mDescription.replaceAll("<img(.*?)\\>", "");
			mDescription = mDescription.replaceAll("&lt", "<");
			mDescription = mDescription.replaceAll("&gt", ">");
			mDescription = mDescription.replaceAll("</li>", "<br><br>");
			mDescription = mDescription.replaceAll("<li>", "");
			mDescription = mDescription.replaceAll("<ul>", "");
			mDescription = mDescription.replaceAll("</ul>", "");
			mDescription = mDescription.replaceAll(" •", "<br>•");
			mDescription = mDescription.replaceAll(" - ", "<br>-");
			mDescription = mDescription.replaceAll("Paga", "<br><br>Paga");
			mDescription = mDescription.replaceAll(" para: ", " para:<br>");
			mDescription = mDescription.replaceAll("€ ", "€<br>");
			mDescription = mDescription.replaceAll("amazon.com", "amazon.com<br>");
			mDescription = mDescription.replaceAll("Contraofertas de amazon.com", "Contraofertas de amazon.com<br>");
			mDescription = mDescription.replaceAll("Oferta diaria en Gamersgate:", "Oferta diaria en Gamersgate:<br><br>");

		}
		if (mUri.toString().substring(0, 26).equals("http://www.vayaansias.com/"))
		{
			imgDescriptionWeb.setImageResource(R.drawable.vayaansias);
			mDescription = mDescription.replaceAll("<img(.*?)\\>", "");
			mDescription = mDescription.replaceAll("&lt", "<");
			mDescription = mDescription.replaceAll("&gt", ">");
			mDescription = mDescription.replaceAll("</li>", "<br><br>");
			mDescription = mDescription.replaceAll("<li>", "");
			mDescription = mDescription.replaceAll("<ul>", "");
			mDescription = mDescription.replaceAll("</ul>", "");
			mDescription = mDescription.replaceAll("</ul>", "");
			mDescription = mDescription.replaceAll("</a> -", "</a><br>");
			mDescription = mDescription.replaceAll("\\) \\(", ")<br>(");
			mDescription = mDescription.replaceAll("<br /><div", "<div");
			mDescription = mDescription.replaceAll("</div><br /><a", "</div><a");
			mDescription = mDescription.replaceAll("\\(<font", "<br>(<font");
			mDescription = mDescription.replaceAll("\\(<FONT", "<br>(<FONT");
			mDescription = mDescription.replaceAll("<br>-- <a", "<br><a");
			mDescription = mDescription.replaceAll("<br>--- <a", "<br><a");
			mDescription = mDescription.replaceAll("<br>- <a", "<br><a");
			mDescription = mDescription.replaceAll(". Oferta de 24 horas.", "<br><br>Oferta de 24 horas.<br>");
			mDescription = mDescription.replaceAll("Resto de items y juegos sueltos al 90%", "<br><br>Resto de items y juegos sueltos al 90%");

		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		this.mDetector.onTouchEvent(ev);
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onSwipe(int direction)
	{
		switch (direction)
		{
			case SimpleGestureFilter.SWIPE_LEFT:
				if (mPosition < RssAdapter.mStaticItems.size() - 1)
				{
					mPosition++;
					mLayout.startAnimation(mHideSlideLeft);
				}
				break;
			case SimpleGestureFilter.SWIPE_RIGHT:
				if (mPosition > 0)
				{
					mPosition--;
					mLayout.startAnimation(mHideSlideRight);
				}
				break;
		}
	}

	@Override
	public void onDoubleTap()
	{
	}

	public void getItemInPosition(List<RssItem> items, int pos)
	{
		String day = "";
		String month = "";
		day = items.get(pos).getDay();
		month = items.get(pos).getMonth();
		if (Integer.parseInt(day) < 10)
			day = "0" + day;
		if (Integer.parseInt(month) < 10)
			month = "0" + month;

		mTitle = items.get(pos).getTitle();
		mDate = day + "/" + month;
		mDescription = items.get(pos).getDescription();
		mUri = Uri.parse(items.get(pos).getLink());
		mColor = items.get(pos).getColor();

		loadTexts();
	}

	@Override
	public void onAnimationStart(Animation animation)
	{
		mAnimationLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, tvTitle.getHeight()));
	}

	@Override
	public void onAnimationEnd(Animation animation)
	{
		if (animation == mHideSlideLeft)
		{
			mLayout.startAnimation(mShowSlideLeft);
			getItemInPosition(RssAdapter.mStaticItems, mPosition);
		}
		else if (animation == mShowSlideLeft || animation == mShowSlideRight)
			mAnimationLayout.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, tvTitle.getHeight()));
		else if(animation == mHideSlideRight)
		{
			mLayout.startAnimation(mShowSlideRight);
			getItemInPosition(RssAdapter.mStaticItems, mPosition);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation)
	{
	}
}
