//package com.androidhive.androidsqlite;
package com.uai.tfi.domoticarg.dal;
 
import java.util.ArrayList;
import java.util.List;

import com.uai.tfi.domoticarg.model.Device;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "TFI";
 
    // Devices table name
    private static final String TABLE_DEVICES = "Dispositivos";
 
    // Devices Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_ONL_ID = "onlineID";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DEVICES_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DEVICES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_ONL_ID + " TEXT" + ")";
        db.execSQL(CREATE_DEVICES_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEVICES);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new device
    public void addDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, device.getName()); // Device Name
        values.put(KEY_ONL_ID, device.getOnlineID()); // Device Phone
 
        // Inserting Row
        db.insert(TABLE_DEVICES, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single device
    public Device getDevice(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_DEVICES, new String[] { KEY_ID,
                KEY_NAME, KEY_ONL_ID }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Device device = new Device(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return device
        return device;
    }
     
    // Getting All Devices
    public List<Device> getAllDevices() {
        List<Device> DeviceList = new ArrayList<Device>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DEVICES;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Device device = new Device();
                device.setID(Integer.parseInt(cursor.getString(0)));
                device.setName(cursor.getString(1));
                device.setOnlineID(cursor.getString(2));
                // Adding device to list
                DeviceList.add(device);
            } while (cursor.moveToNext());
        }
 
        // return device list
        return DeviceList;
    }
 
    // Updating single device
    public int updateDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, device.getName());
        values.put(KEY_ONL_ID, device.getOnlineID());
 
        // updating row
        return db.update(TABLE_DEVICES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(device.getID()) });
    }
 
    // Deleting single device
    public void deleteDevice(Device device) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEVICES, KEY_ID + " = ?",
                new String[] { String.valueOf(device.getID()) });
        db.close();
    }
 
 
    // Getting devices Count
    public int getDevicesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DEVICES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}