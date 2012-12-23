package sgd.dostuff;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

public class SpotDataSource
{

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private DateFormat df;
	public static String TAG = "dostuff";

	// private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
	// MySQLiteHelper.COLUMN_COMMENT };

	public SpotDataSource(Context context)
	{
		dbHelper = new MySQLiteHelper(context);
		df = DateFormat.getDateTimeInstance(0, 0, Locale.US);
	}

	public void open() throws SQLException
	{
		database = dbHelper.getWritableDatabase();
	}

	public void close()
	{
		dbHelper.close();
	}

	public Spot createSpot(double lat, double lon, Date date, int ptval, String name)
	{
		if (date == null)
		{
			// This kills the database
			/*
			 * ContentValues values = new ContentValues();
			 * values.put(MySQLiteHelper.COLUMN_LAT, lat);
			 * values.put(MySQLiteHelper.COLUMN_LON, lon);
			 * values.put(MySQLiteHelper.COLUMN_NAME, name);
			 * values.put(MySQLiteHelper.COLUMN_PTVAL, ptval);
			 * //values.put(MySQLiteHelper.COLUMN_DATE, ""); long insertId =
			 * database.insert(MySQLiteHelper.TABLE_SPOTS, null, values); // To
			 * show how to query Cursor cursor =
			 * database.query(MySQLiteHelper.TABLE_SPOTS, null,
			 * MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
			 * null); cursor.moveToFirst();
			 */

			return null;
		}
		else
		{
			String dstring = df.format(date);

			Log.v(TAG, dstring);

			ContentValues values = new ContentValues();
			values.put(MySQLiteHelper.COLUMN_LAT, lat);
			values.put(MySQLiteHelper.COLUMN_LON, lon);
			values.put(MySQLiteHelper.COLUMN_DATE, dstring);
			values.put(MySQLiteHelper.COLUMN_PTVAL, ptval);
			values.put(MySQLiteHelper.COLUMN_NAME, name);
			values.put(MySQLiteHelper.COLUMN_ISDONE, 0);
			long insertId = database.insert(MySQLiteHelper.TABLE_SPOTS, null, values);

			Cursor cursor = database.query(MySQLiteHelper.TABLE_SPOTS, null, MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
					null);

			cursor.moveToFirst();

			return cursorToSpot(cursor);
		}
	}

	public Spot getSpotByID(long id)
	{

		Cursor cursor = database.query(MySQLiteHelper.TABLE_SPOTS, null, MySQLiteHelper.COLUMN_ID + " = " + id, null, null, null, null);
		cursor.moveToFirst();
		return cursorToSpot(cursor);

	}

	public void changeSpot(Spot spot)
	{
		// Places all spot values into the update clause
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_LAT, spot.getLat());
		values.put(MySQLiteHelper.COLUMN_LON, spot.getLon());
		// String dstring = df.format(spot.getDate());
		values.put(MySQLiteHelper.COLUMN_PTVAL, spot.getPtval());
		values.put(MySQLiteHelper.COLUMN_NAME, spot.getName());
		values.put(MySQLiteHelper.COLUMN_PTSGOT, spot.getPtsgot());
		values.put(MySQLiteHelper.COLUMN_ISDONE, spot.isDone() ? 1 : 0);

		database.update(MySQLiteHelper.TABLE_SPOTS, values, MySQLiteHelper.COLUMN_ID + " = " + spot.getId(), null);

	}

	public void deleteSpot(Spot spot)
	{
		long id = spot.getId();
		Log.v(TAG, "deleting this ! ! !");
		database.delete(MySQLiteHelper.TABLE_SPOTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
		// database.delete(MySQLiteHelper.TABLE_SPOTS, null, null);
	}

	public List<Spot> getAllSpots()
	{
		List<Spot> spots = new ArrayList<Spot>();
		Cursor cursor = database.query(MySQLiteHelper.TABLE_SPOTS, null, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast())
		{
			Spot spot = cursorToSpot(cursor);
			spots.add(spot);
			cursor.moveToNext();
		}
		cursor.close();
		return spots;
	}

	/**
	 * Returns all spots sorted by date
	 * 
	 * @author Nick
	 * 
	 */
	public List<Spot> getAllDateSorted()
	{
		List<Spot> spots = getAllSpots();
		Collections.sort(spots, new SortByTime());
		return spots;
	}

	public List<Spot> getWeekOfSpots(){
		List<Spot> spots = getAllDateSorted();
		
		Date curDate = new Date();
		
		Spot next = getNextSpot(curDate);
		List<Spot> s = null;
		if (next != null){
			Log.d(TAG, "NEXT INDEX: " + spots.indexOf(next));
			s = spots.subList(spots.indexOf(next), spots.size());
		}
		if (s == null) s = spots; 
		
		return s;
	}
	
	
	/**
	 * Going to have to change when we add more functionaility to spot...
	 * 
	 * @param date
	 *            the date you want to get the next spot from.
	 * @return the spot that is closest forward in time to the date passed in
	 */
	public Spot getNextSpot(Date date)
	{

		// This assumes sorting by date actually works...
		List<Spot> spots = getAllDateSorted();

		Spot nextSpot = null;
		for (Spot s : spots)
		{
			Date spotDate = s.getDate();
			if (!s.isDone() && !spotDate.before(date) && s.getPtval() != s.getPtsgot()){
				nextSpot = s;
				break;
			}
		}

		return nextSpot;
	}

	public class SortByTime implements Comparator<Spot>
	{

		public int compare(Spot o1, Spot o2)
		{
			return o1.getDate().compareTo(o2.getDate());
		}
	}

	private Spot cursorToSpot(Cursor cursor)
	{
		Spot spot = new Spot();

		// These values must not change or else bad things will happen TM
		spot.setId(cursor.getLong(0));
		spot.setLat(cursor.getDouble(1));
		spot.setLon(cursor.getDouble(2));

		String datestring = cursor.getString(3);

		spot.setPtval(cursor.getLong(4));
		spot.setName(cursor.getString(5));
		if (cursor.getColumnCount() >= 7)
		{
			spot.setPtsgot(cursor.getLong(6));
		}
		// because java is my second language- don't need the ternary, it's just a boolean
		if(cursor.getColumnCount() >= 8)
		{
			spot.setDone(cursor.getLong(7) == 1);
		}

		Date date = null;
		try
		{
			date = df.parse(datestring);
		}
		catch (ParseException e)
		{
			Log.v(TAG, "date parse failed");
		}
		spot.setDate(date);
		
		spot.determineStatus();

		return spot;
	}

	// General helper methods
	public int calculateTotalPtVal()
	{
		List<Spot> spots = getAllSpots();

		int total = 0;
		for (Spot s : spots)
		{
			total += s.getPtval();
		}
		return total;
	}

	public int calculateTotalPtsGot()
	{
		List<Spot> spots = getAllSpots();

		int total = 0;
		for (Spot s : spots)
		{
			total += s.getPtsgot();
		}
		return total;
	}

}
