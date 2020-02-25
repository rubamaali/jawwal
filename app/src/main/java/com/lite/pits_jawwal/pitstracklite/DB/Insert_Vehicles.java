package com.lite.pits_jawwal.pitstracklite.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lite.pits_jawwal.pitstracklite.List.Vehicles_list;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ruba-PITS on 10/2/2017.
 */

public class Insert_Vehicles {
    private Context _contex;
    private static CreateDb _db;
    public Insert_Vehicles(Context context) {
        _contex=context;
        _db = new CreateDb(context);
    }
    public void insert(List<Vehicles_list> result) {
        SQLiteDatabase db = _db.getWritableDatabase();
        db.delete(CreateDb.Columns_Name.TABLE_NAME,null, null);
        ContentValues values = new ContentValues();
        for(int i=0;i<result.size();i++) {
            values.put(CreateDb.Columns_Name.COLUMN_NAME,result.get(i).getName());
            values.put(CreateDb.Columns_Name.COLUMN_NAME_id,result.get(i).getDeviceid());
            values.put(CreateDb.Columns_Name.COLUMN_LAST_UPDATE,result.get(i).getLast_update());
            values.put(CreateDb.Columns_Name.COLUMN_ACTIVE,result.get(i).getKey());
            values.put(CreateDb.Columns_Name.COLUMN_ADDRESS,result.get(i).getAddress());
            values.put(CreateDb.Columns_Name.COLUMN_LAT,result.get(i).getLat());
            values.put(CreateDb.Columns_Name.COLUMN_LON,result.get(i).getLon());
            values.put(CreateDb.Columns_Name.COLUMN_ICON,result.get(i).getIcon());
            values.put(CreateDb.Columns_Name.COLUMN_SPEED,result.get(i).getSpeed());
            values.put(CreateDb.Columns_Name.COLUMN_DRIVER_NAME,result.get(i).getDriver_name());
            values.put(CreateDb.Columns_Name.COLUMN_DRIVER_PHONE,result.get(i).getPhone());
            db.insert(CreateDb.Columns_Name.TABLE_NAME, null, values);

        }
    }
    public void Delete_all(){
        SQLiteDatabase db = _db.getReadableDatabase();
        db.delete(CreateDb.Columns_Name.TABLE_NAME,"", null);
        db.delete(CreateDb.Columns_Name.TABLE_NOTIFICATION,null, null);
    }
    public List<Vehicles_list> All_Vehicles() {
        SQLiteDatabase db = _db.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM vehicles", null);
        if (cursor == null) {
            return null;
        }
        List<Vehicles_list> list = new ArrayList<>();
        cursor.moveToFirst();
        int i=0;
        while(!cursor.isAfterLast()) {
            list.add(new Vehicles_list (cursor.getString(3),cursor.getString(1),cursor.getString(4),cursor.getString(5),cursor.getString(2),cursor.getString(6),cursor.getString(7),cursor.getString(8),i,cursor.getString(9),cursor.getString(10),cursor.getString(11)));
            i++;
            cursor.moveToNext();
        }
        return list;

    }
}

