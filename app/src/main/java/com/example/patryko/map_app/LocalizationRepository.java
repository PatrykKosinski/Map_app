package com.example.patryko.map_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocalizationRepository {

    private final SQLiteDatabase database;

    LocalizationRepository(Context context){
        File mDatabaseFile = context.getDatabasePath("patryk.db").getAbsoluteFile();
        database = SQLiteDatabase.openOrCreateDatabase(mDatabaseFile, null);
        database.execSQL("CREATE TABLE IF NOT EXISTS Locations(Id VARCHAR PRIMARY KEY, Name VARCHAR, Description VARCHAR, Radius NUMERIC, Lat NUMERIC, Lng NUMERIC);");
    }
    public void addLocation(Localization localization) {
        ContentValues insertValues = new ContentValues();
        insertValues.put("Id", localization.getId().toString());
        insertValues.put("Name", localization.getName());
        insertValues.put("Description", localization.getDesc());
        insertValues.put("Radius", localization.getRadius());
        insertValues.put("Lat", localization.getLatitude());
        insertValues.put("Lng", localization.getLongitude());
        database.insert("Locations", null, insertValues);
    }

    public ArrayList<Localization> getAllPositions(){
        Cursor cursor = database.rawQuery("select * from Locations", null);
        ArrayList<Localization> listResult = new ArrayList<>();

        if (cursor.moveToFirst()) {

            while (cursor.isAfterLast() == false) {
                String id = cursor.getString(cursor.getColumnIndex("Id"));
                String name = cursor.getString(cursor.getColumnIndex("Name"));
                String desc = cursor.getString(cursor.getColumnIndex("Description"));
                double radius = cursor.getDouble(cursor.getColumnIndex("Radius"));
                double lat = cursor.getDouble(cursor.getColumnIndex("Lat"));
                double lng = cursor.getDouble(cursor.getColumnIndex("Lng"));
                listResult.add(new Localization(UUID.fromString(id),name, desc, radius, lat, lng));
                cursor.moveToNext();
            }
        }

        return listResult;
    }

    public Localization getPositionsById(String positionId){
        String[] columns = {"Id","Name","Description", "Radius", "Lat","Lng"};
        String[] whereArgs = {positionId};
        Cursor cursor = database.query("Locations",columns,"Id=?",whereArgs,null,null,null);
        Localization result = null;

        if (cursor.moveToFirst()) {
            String id = cursor.getString(cursor.getColumnIndex("Id"));
            String name = cursor.getString(cursor.getColumnIndex("Name"));
            String desc = cursor.getString(cursor.getColumnIndex("Description"));
            double radius = cursor.getDouble(cursor.getColumnIndex("Radius"));
            double lat = cursor.getDouble(cursor.getColumnIndex("Lat"));
            double lng = cursor.getDouble(cursor.getColumnIndex("Lng"));
            result = new Localization(UUID.fromString(id),name, desc, radius, lat, lng);
        }

        return result;
    }




}
