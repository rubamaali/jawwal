package com.lite.pits_jawwal.pitstracklite.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.lite.pits_jawwal.pitstracklite.Custom_Event.Event_item;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ruba-PITS on 10/7/2017.
 */

public class Insert_events {
    private Context _contex;
    private static CreateDb _db;
    public Insert_events(Context context) {
        _contex=context;
        _db = new CreateDb(context);
    }
    public void insert(List<Event_item> result,String _date) {
        try {
            SQLiteDatabase db = _db.getWritableDatabase();
            db.delete(CreateDb.Columns_Name.TABLE_EVENT, null, null);
            ContentValues values = new ContentValues();
            for (int i = 0; i < result.size(); i++) {
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_NAME, result.get(i).getevent_name());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_VEHICLE, result.get(i).getVehicle_name());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_ADDRESS, result.get(i).getAddress());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_SPEED, result.get(i).getSpeed());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_LON, result.get(i).getlon());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_LAT, result.get(i).getlat());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_time, result.get(i).getTime());
                values.put(CreateDb.Columns_Name.COLUMN_EVENT_date, _date);
                db.insert(CreateDb.Columns_Name.TABLE_EVENT, null, values);

            }
        }catch (Exception e){}
    }
    public List<Event_item> All_events(String date) {
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM event WHERE date="+"\""+date+"\"", null);
        if (cursor == null) {
            return null;
        }
        else {
        }
        List<Event_item> list = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(new Event_item(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(7),cursor.getString(4),cursor.getString(5),cursor.getString(6)));
            cursor.moveToNext();
        }
        return list;
    }
}
