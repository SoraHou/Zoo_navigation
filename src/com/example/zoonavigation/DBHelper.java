package com.example.zoonavigation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context context) {
		super(context, "ToxicApp", null, 3);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE account ("
			+ "id INTEGER primary key autoincrement,"
			+ "user TEXT,"
			+ "pass TEXT)"
		);
		
		db.execSQL("CREATE TABLE event_list ("
			+ "id INTEGER primary key autoincrement,"
			+ "IMEI TEXT,"
			+ "accident_no TEXT,"
			+ "accident_name TEXT,"
			+ "longitude TEXT,"
			+ "latitude TEXT,"
			+ "chemical TEXT,"
			+ "etype TEXT,"
			+ "evalue TEXT,"
			+ "eunit TEXT,"
			+ "filename TEXT,"
			+ "is_upload INTEGER)"
		);

		db.execSQL("CREATE TABLE skype_list ("
			+ "id INTEGER primary key autoincrement,"
			+ "name TEXT,"
			+ "phone TEXT)"
		);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS event_list");
		db.execSQL("DROP TABLE IF EXISTS skype_list");
		db.execSQL("DROP TABLE IF EXISTS account");
		
		onCreate(db);
	}
	
	public Cursor readAccount() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("account", null, null, null, null, null, null);
		
		return cursor;
	}
	
	public long insertAccount(String user, String pass) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("user", user);
		cv.put("pass", pass);

		return db.insert("account", null, cv);
	}
	
	public void deleteAccount() {
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete("account", null, null);
	}
	
	public Cursor readSkypeContact() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("skype_list", null, null, null, null, null, null);
		
		return cursor;
	}
	
	public long insertSkyprContact(String name, String phone) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("name", name);
		cv.put("phone", phone);

		return db.insert("skype_list", null, cv);
	}
	
	public void deleteSkypeContact(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		String[] args = { Integer.toString(id) };
		
		db.delete("skype_list", "id=?", args);
	}
	
	public Cursor readUploadedEvent() {
		String[] args = {"1"};
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("event_list", null, "is_upload=?", args, null, null, null);
		
		return cursor;
	}
	
	public Cursor readEvent() {
		String[] args = {"0"};
		
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query("event_list", null, "is_upload=?", args, null, null, null);
		
		return cursor;
	}
	
	public void upadteEvent(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		String[] args = { Integer.toString(id) };
		
		ContentValues cv = new ContentValues();
		cv.put("is_upload", 1);
		
		db.update("event_list", cv, "id=?", args);
	}

	public long insertEvent(String IMEI, String accidentNo, String accidentName, String longitude,
			String latitude, String chemical, String etype, String evalue,
			String eunit, String filename, int isUpload) {
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put("IMEI", IMEI);
		cv.put("accident_no", accidentNo);
		cv.put("accident_name", accidentName);
		cv.put("longitude", longitude);
		cv.put("latitude", latitude);
		cv.put("chemical", chemical);
		cv.put("etype", etype);
		cv.put("evalue", evalue);
		cv.put("eunit", eunit);
		cv.put("filename", filename);
		cv.put("is_upload", isUpload);

		return db.insert("event_list", null, cv);
	}

	public void deleteEvent(int id) {
		SQLiteDatabase db = this.getWritableDatabase();

		String[] args = { Integer.toString(id) };
		
		db.delete("event_list", "id=?", args);
	}
}