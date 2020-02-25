package com.lite.pits_jawwal.pitstracklite.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lite.pits_jawwal.pitstracklite.Custom_Event.Event_item;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ruba-PITS on 10/5/2017.
 */

public class Insert_notifications {
    private Context _contex;
    private static CreateDb _db;
    public Insert_notifications(Context context) {
        _contex=context;
        _db = new CreateDb(context);
    }
    public void insert(String msg,String type,String time,String date) {
        SQLiteDatabase db = _db.getWritableDatabase();
        ContentValues values = new ContentValues();
            values.put(CreateDb.Columns_Name.COLUMN_NOTI_MESG,msg);
            values.put(CreateDb.Columns_Name.COLUMN_NOTI_TYPE,type);
            values.put(CreateDb.Columns_Name.COLUMN_NOTI_time,time);
            values.put(CreateDb.Columns_Name.COLUMN_NOTI_date,date);
            db.insert(CreateDb.Columns_Name.TABLE_NOTIFICATION, null, values);

    }
    public List<Event_item> All_Notifications(String date) {
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM notification WHERE date="+"\""+date+"\"", null);
        if (cursor == null) {
            return null;
        }
        List<Event_item> list = new ArrayList<>();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            list.add(new Event_item("SMS",cursor.getString(1),"",cursor.getString(2),"","",""));
            cursor.moveToNext();
        }
        return list;
    }
}
