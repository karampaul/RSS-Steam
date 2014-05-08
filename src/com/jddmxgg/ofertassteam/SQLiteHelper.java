package com.jddmxgg.ofertassteam;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper
{

	String mCreateTable = "CREATE TABLE FEED(id INTEGER, title TEXT, description TEXT, link TEXT, month TEXT, day TEXT, color TEXT, PRIMARY KEY(id))";

	public SQLiteHelper(Context context, String name, CursorFactory factory, int version)
	{
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(mCreateTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS FEED");
		db.execSQL(mCreateTable);
	}

	public List<RssItem> getValues()
	{
		List<RssItem> items = new ArrayList<RssItem>();

		String selectQuery = "SELECT * FROM FEED";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		if (cursor.moveToFirst())
		{
			do
			{
				RssItem item = new RssItem(cursor.getString(1), cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6));
				items.add(item);
			} while (cursor.moveToNext());
		}

		cursor.close();
		return items;
	}

	public void insertValues(List<RssItem> items)
	{
		String sql, title, description, link, month, day, color;

		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS FEED");
		db.execSQL(mCreateTable);

		for (RssItem item : items)
		{
			title = item.getTitle();
			description = item.getDescription();
			link = item.getLink();
			month = item.getMonth();
			day = item.getDay();
			color = item.getColor();
			sql = "INSERT INTO FEED(title,description, link, month, day, color) VALUES (" + title + "," + description + "," + link + "," + month + "," + day + "," + color + ")";
			db.execSQL(sql);
		}

	}
}
