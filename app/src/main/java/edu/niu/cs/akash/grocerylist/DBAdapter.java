package edu.niu.cs.akash.grocerylist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by  on 5/4/2016.
 */
public class DBAdapter {
    private static final String TAG="DBAdapter";

    private static final String KEY_ROWID = "_id",
            KEY_DESCRIPTION= "desc",
            KEY_ISDONE = "isdone";

    private static final String[] ALL_KEYS = new String[]{KEY_ROWID,KEY_DESCRIPTION,KEY_ISDONE};

    public static final int COL_ROWID = 0,
                            COL_DESCRIPTION = 1,
                            COL_ISDONE = 2;

    private static final String DATABASE_NAME = "MyDB",
            DATABASE_TABLE = "testTable";

    private static final int DATABASE_VERSION = 1;

    private static final String CREATE_SQL = "create table " + DATABASE_TABLE
            + "(" + KEY_ROWID + " integer primary key autoincrement, "
            + KEY_DESCRIPTION + " text not null, "
            + KEY_ISDONE + " integer not null " + ");";

    private DBHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context context){
        myDBHelper = new DBHelper (context);
    }

    public DBAdapter open(){
        db = myDBHelper.getWritableDatabase();
        return this;
    }//end of open

    public void close(){
        myDBHelper.close();
    }

    public long insertRow( String name, Integer studNum ){
        ContentValues rowValues = new ContentValues();

        rowValues.put(KEY_DESCRIPTION, name);
        rowValues.put(KEY_ISDONE, studNum);

        return db.insert(DATABASE_TABLE, null, rowValues);

    }//end of insertRow


    public boolean deleteRow( long rowID ){
        String where = KEY_ROWID + "=" + rowID;

        return db.delete(DATABASE_TABLE,where, null)!= 0;
    }//end of deleteRow

    public void deleteAll(){
        Cursor cursor = getAllRows();
        long rowID = cursor.getColumnIndexOrThrow(KEY_ROWID);

        boolean result = cursor.moveToFirst();
        while(result){
            deleteRow(cursor.getInt((int)rowID));
            result = cursor.moveToNext();
        }
    }//end of deleteAll

    public Cursor getAllRows(){
        String where = null;
        Cursor cursor = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor getRow(long rowID){
        String where = KEY_ROWID + "=" + rowID;
        Cursor cursor = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);

        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }//end of getRow

    public boolean updateItem(Grocery_Item changeItem) {
        ContentValues row = new ContentValues();
        int id = changeItem.getId();
        row.put(KEY_ISDONE, 1);
        return db.update(DATABASE_TABLE, row, KEY_ROWID + "=" + id, null) > 0;
    }

    //Inner Class
    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from " + oldVersion + " to " + newVersion
                    + ", which will destroy all old data");

            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE );
            onCreate(db);
        }
    }//end of DBHelper


}
