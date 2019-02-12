package com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.db;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amazonaws.compute.ap_northeast_2.ec2_54_180_85_255.simplememo.data.Memo;

import java.util.ArrayList;
import java.util.Calendar;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE Memo (id INTEGER PRIMARY KEY AUTOINCREMENT, content TEXT, time NUMERIC);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertMemo(String content) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "Insert into Memo values (null, '"+ content +"', '"+ Calendar.getInstance().getTimeInMillis() +"');";
        Log.d("jtrlog", query);
        db.execSQL(query);
    }

    public ArrayList<Memo> getMemoList0(String searchword) {
        ArrayList<Memo> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "";
        if (searchword.equals("")) {
            query = "select * from Memo Order by id desc";
        }
        else {
            query = "select * from Memo where content like '%" + searchword + "%' Order by id desc";
        }
        Log.d("jtrlog", query);
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String content = cursor.getString(1);
            Long time = cursor.getLong(2);
            Memo item = new Memo(id, content, time);
            items.add(item);
            Log.d("jtrlog", "loadDB:" + item.toString());
        }
        cursor.close();
        return items;
    }

    public ArrayList<Memo> getMemoList1(String searchword) {
        ArrayList<Memo> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "";
        if (searchword.equals("")) {
            query = "select * from Memo Order by time asc";
        }
        else {
            query = "select * from Memo where content like '%" + searchword + "%' Order by time asc";
        }
        Log.d("jtrlog", query);
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String content = cursor.getString(1);
            Long time = cursor.getLong(2);
            Memo item = new Memo(id, content, time);
            items.add(item);
            Log.d("jtrlog", "loadDB:" + item.toString());
        }
        cursor.close();
        return items;
    }

    public ArrayList<Memo> getMemoList2(String searchword) {
        ArrayList<Memo> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "";
        if (searchword.equals("")) {
            query = "select * from Memo Order by time desc";
        }
        else {
            query = "select * from Memo where content like '%" + searchword + "%' Order by time desc";
        }
        Log.d("jtrlog", query);
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String content = cursor.getString(1);
            Long time = cursor.getLong(2);
            Memo item = new Memo(id, content, time);
            items.add(item);
            Log.d("jtrlog", "loadDB:" + item.toString());
        }
        cursor.close();
        return items;
    }

    public Memo getMemo(Long idinsert) {
        Memo item = new Memo();
        SQLiteDatabase db = getReadableDatabase();
        String query = "select * from Memo Where id=" + idinsert;
        Log.d("jtrlog", query);
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            Long id = cursor.getLong(0);
            String content = cursor.getString(1);
            Long time = cursor.getLong(2);
            item = new Memo(id, content, time);
            Log.d("jtrlog", "loadDB:" + item.toString());
        }
        cursor.close();
        return item;
    }

    public void updateMemo(Long id, String content) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "update Memo set content='"+ content +"', time='"+ Calendar.getInstance().getTimeInMillis() +"' where id="+ id +";";
        Log.d("jtrlog", query);
        db.execSQL(query);
    }

    public void delMemo(Long id) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "delete from Memo where id="+ id +";";
        Log.d("jtrlog", query);
        db.execSQL(query);
    }
}
