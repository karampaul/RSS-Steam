package com.jddmxgg.ofertassteam;

import android.util.Log;

public class Constants
{
	public static final String TAG = "RssApp";
	public static final String ADD_ID = "";
	private static final boolean DEBUG = true;

	public static void debug(String message)
	{
		if (DEBUG)
			Log.d(TAG, message);
	}
}