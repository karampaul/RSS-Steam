package com.jddmxgg.ofertassteam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Constants
{
	public static final String TAG = "Ofertas Steam";
	public static final String ADD_ID = "";
	private static final boolean DEBUG = false;
	public static final String ADMOB_PUBLISHER_ID = "ca-app-pub-2689062467644026/9100777993";

	
	public static void debug(String message)
	{
		if (DEBUG)
			Log.d(TAG, message);
	}

	public static enum Colors
	{
		GREEN	("#00FF00"),
		INDIGO	("#4B0082"),//
		YELLOW	("#FFFF00"),
		PURPLE	("#8F00FF"),//
		ORANGE	("#FF7F00"),
		BLUE	("#0000FF"),//
		RED		("#FF0000");

		String mColorCode;

		private Colors(String colorCode)
		{
			mColorCode = colorCode;
		}

		public String getColor()
		{
			return mColorCode;
		}
	}

	public static boolean internetConnectionEnabled(Context contexto)
	{
		ConnectivityManager conMgr = (ConnectivityManager) contexto.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
}