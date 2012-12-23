package sgd.dostuff;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	public static final String TABLE_SPOTS = "spots";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_LAT = "lat";
	public static final String COLUMN_LON = "lon";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_PTVAL = "ptval";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_PTSGOT = "ptsgot";
	public static final String COLUMN_ISDONE = "isdone";
	
	private static final String DATABASE_NAME = "spots.db";
	private static final int DATABASE_VERSION = 1;

	// Database creation sql statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_SPOTS + "( " + COLUMN_ID
			+ " integer primary key autoincrement, " 
			+ COLUMN_LAT + " real, "
			+ COLUMN_LON + " real, "
			+ COLUMN_DATE + " text, "
			+ COLUMN_PTVAL + " integer, " 
			+ COLUMN_NAME + " text, "
			+ COLUMN_PTSGOT + " INTEGER DEFAULT(0),"
			+ COLUMN_ISDONE + " INTEGER DEFAULT(0));";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//Log.w(MySQLiteHelper.class.getName(),
		//		"Upgrading database from version " + oldVersion + " to "
		//				+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS" + TABLE_SPOTS);
		onCreate(db);
	}

}