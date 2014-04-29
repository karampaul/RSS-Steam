package com.jddmxgg.ofertassteam;

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
}