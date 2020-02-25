package com.lite.pits_jawwal.pitstracklite.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Ruba-PITS on 6/29/2017.
 */

public class CreateDb extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 2;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final String DATABASE_NAME = "Data.db";
    private static final String SQL_CREATE_Vehicles =
            "CREATE TABLE " + Columns_Name.TABLE_NAME + " (" +
                    Columns_Name._ID + " INTEGER PRIMARY KEY," +
                    Columns_Name.COLUMN_NAME + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_NAME_id+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_LAST_UPDATE+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_ACTIVE+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_ADDRESS+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_LAT+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_LON+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_ICON+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_SPEED+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_DRIVER_NAME+ TEXT_TYPE +COMMA_SEP +
                    Columns_Name.COLUMN_DRIVER_PHONE+ TEXT_TYPE +
                    " )";


    private static final String SQL_CREATE_NOTIFICATION =
            "CREATE TABLE " + Columns_Name.TABLE_NOTIFICATION + " (" +
                    Columns_Name._ID + " INTEGER PRIMARY KEY," +
                    Columns_Name.COLUMN_NOTI_MESG + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_NOTI_time + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_NOTI_date + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_NOTI_TYPE+ TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_EVENTS=
            "CREATE TABLE " + Columns_Name.TABLE_EVENT + " (" +
                    Columns_Name._ID + " INTEGER PRIMARY KEY," +
                    Columns_Name.COLUMN_EVENT_NAME + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_VEHICLE + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_ADDRESS + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_SPEED + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_LON + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_LAT + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_time + TEXT_TYPE + COMMA_SEP +
                    Columns_Name.COLUMN_EVENT_date+ TEXT_TYPE +
                    " )";


    private static final String SQL_DELETE_Vehicles="DROP TABLE IF EXISTS " + Columns_Name.TABLE_NAME;
    private static final String SQL_DELETE_NOTIFICATIONS="DROP TABLE IF EXISTS " + Columns_Name.TABLE_NOTIFICATION;
    private static final String SQL_DELETE_EVENTS="DROP TABLE IF EXISTS " + Columns_Name.TABLE_EVENT;
    public CreateDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_Vehicles);
        sqLiteDatabase.execSQL(SQL_CREATE_NOTIFICATION);
        sqLiteDatabase.execSQL(SQL_CREATE_EVENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_Vehicles);
        sqLiteDatabase.execSQL(SQL_DELETE_NOTIFICATIONS);
        sqLiteDatabase.execSQL(SQL_DELETE_EVENTS);
        onCreate(sqLiteDatabase);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    public static abstract class Columns_Name implements BaseColumns {

        public static final String TABLE_NAME = "vehicles";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NAME_id = "id";
        public static final String COLUMN_LAST_UPDATE= "lastupdate";
        public static final String COLUMN_ACTIVE= "active";
        public static final String COLUMN_ADDRESS= "address";
        public static final String COLUMN_LAT= "lat";
        public static final String COLUMN_LON= "lon";
        public static final String COLUMN_ICON= "icon";
        public static final String COLUMN_SPEED= "speed";
        public static final String COLUMN_DRIVER_NAME= "drivername";
        public static final String COLUMN_DRIVER_PHONE= "phone";

        ////////////  notification columns ///////////////////////////
        public static final String TABLE_NOTIFICATION = "notification";
        public static final String COLUMN_NOTI_MESG = "msg";
        public static final String COLUMN_NOTI_TYPE = "type";
        public static final String COLUMN_NOTI_time = "time";
        public static final String COLUMN_NOTI_date = "date";

        ////////////  event columns ///////////////////////////
        public static final String TABLE_EVENT = "event";
        public static final String COLUMN_EVENT_NAME = "event_name";
        public static final String COLUMN_EVENT_VEHICLE = "vehicle";
        public static final String COLUMN_EVENT_ADDRESS = "address";
        public static final String COLUMN_EVENT_SPEED = "speed";
        public static final String COLUMN_EVENT_LON = "lon";
        public static final String COLUMN_EVENT_LAT = "lat";
        public static final String COLUMN_EVENT_time = "time";
        public static final String COLUMN_EVENT_date = "date";




    }
}
