package com.jddmxgg.ofertassteam;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Constants
{
	public static final String TAG = "RssApp";
	public static final String ADD_ID = "";
	public static final String[] COLORS = { "#33B5E5", "#AA66CC", "#99CC00", "#FFBB33", "#FF4444" };
	private static final boolean DEBUG = true;

	public static void debug(String message)
	{
		if (DEBUG)
			Log.d(TAG, message);
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