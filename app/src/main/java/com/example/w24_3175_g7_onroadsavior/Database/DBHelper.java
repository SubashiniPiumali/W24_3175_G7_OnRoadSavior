package com.example.w24_3175_g7_onroadsavior.Database;

import static java.time.LocalDate.now;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.w24_3175_g7_onroadsavior.Model.ServiceProvider;
import com.example.w24_3175_g7_onroadsavior.Model.UserHelperClass;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "onRoadSaviorDB";

    private static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User ( ID TEXT PRIMARY KEY,Name VARCHAR(255) NOT NULL,Username VARCHAR(100) NOT NULL,Email VARCHAR(255) NOT NULL, Phone_NO VARCHAR(20), User_Type VARCHAR(255) NOT NULL, Created_Date LOCALDATE NOT NULL)");

        db.execSQL("CREATE TABLE ServiceProvider (ID TEXT PRIMARY KEY, Location VARCHAR(255) NOT NULL, BreakDownType VARCHAR(255), Rating REAL DEFAULT 0\n)");

        db.execSQL("CREATE TABLE BreakDownRequest ( ID INTEGER PRIMARY KEY AUTOINCREMENT,Created_Date TEXT NOT NULL,Updated_Date TEXT, User_ID TEXT NOT NULL, Provider_ID TEXT NOT NULL, Breakdown_Type TEXT,Location TEXT,Description TEXT, Image TEXT, Status VARCHAR(100) NOT NULL ,FOREIGN KEY (User_ID) REFERENCES User(ID),FOREIGN KEY (Provider_ID) REFERENCES ServiceProvider(ID))");

        db.execSQL("CREATE TABLE Notification ( ID INTEGER PRIMARY KEY AUTOINCREMENT,Created_Date LOCALDATE NOT NULL,Updated_Date LOCALDATE, RECIVER_ID Text NOT NULL, Message TEXT, Status VARCHAR(100) NOT NULL ,FOREIGN KEY (RECIVER_ID) REFERENCES User(ID))");

        db.execSQL("CREATE TABLE EmergencyContact (ID INTEGER PRIMARY KEY AUTOINCREMENT, Contact_Name VARCHAR(255) NOT NULL, Contact_Number VARCHAR(255)\n)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS User");
        db.execSQL("DROP TABLE IF EXISTS ServiceProvider");
        db.execSQL("DROP TABLE IF EXISTS BreakDownRequest");
        db.execSQL("DROP TABLE IF EXISTS EmergencyContact");
        onCreate(db);
    }

    //CRUD - Table: User
    public boolean addUser(String uId, String fullName, String username, String email, String contactNumber, String userType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date createdDate = new Date();

        contentValues.put("ID", uId);
        contentValues.put("Name", fullName);
        contentValues.put("Username", username);
        contentValues.put("Email", email);
        contentValues.put("Phone_NO", contactNumber);
        contentValues.put("User_Type", userType);
        contentValues.put("Created_Date", String.valueOf(createdDate));

        long result = db.insert("User", null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public UserHelperClass getUserData(String uId){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM User WHERE ID='"+uId+"'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            String fullName = cursor.getString(1);
            String username = cursor.getString(2);
            String email = cursor.getString(3);
            String contactNum = cursor.getString(4);
            String userType = cursor.getString(5);

            UserHelperClass user = new UserHelperClass();
            user.setFullName(fullName);
            user.setUserName(username);
            user.setEmail(email);
            user.setContactNumber(contactNum);
            user.setUserType(userType);

            return user;
        }

        return null;
    }


    // Clear user table
    public void clearUserTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("User", null, null);
        db.close();
    }


    //CRUD - Table: ServiceProvider
    public boolean addServiceProvider(String uId, String location, String serviceType){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        Date createdDate = new Date();

        contentValues.put("ID", uId);
        contentValues.put("Location", location);
        contentValues.put("BreakDownType",serviceType);

        long result = db.insert("ServiceProvider", null, contentValues);

        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public UserHelperClass getServiceProviderData(String uId){

        SQLiteDatabase db = this.getWritableDatabase();

        String query = "SELECT * FROM ServiceProvider WHERE ID='"+uId+"'";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){

            String location = cursor.getString(1);
            String serviceType = cursor.getString(2);

            UserHelperClass serviceProvider = new UserHelperClass();
            serviceProvider.setLocation(location);
            serviceProvider.setServiceType( serviceType);

            return serviceProvider;
        }

        return null;
    }


    // Clear Service Provider table
    public void clearServiceProviderTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("ServiceProvider", null, null);
        db.close();
    }


    public void addRequest(String createdDate, String updatedDate, String userID, String providerID, String breakdownType, String address, String description, String image, String status) {
        SQLiteDatabase db = this.getWritableDatabase();

        Log.d("breakdownType",breakdownType);
        Log.d("Location",address);

        db.execSQL("INSERT INTO BreakDownRequest (Created_Date, Updated_Date, User_ID, Provider_ID, Breakdown_Type, Location, Description, Image, Status) VALUES ( '"+createdDate+"',  '"+createdDate+"',  '"+userID+"',  '"+providerID+"',  '"+breakdownType+"',  '"+address+"',  '"+description+"',  '"+image+"',  '"+status+"')\n");
        db.execSQL("INSERT INTO Notification (Created_Date, Updated_Date, RECIVER_ID, Message, Status) VALUES ('"+createdDate+"', '"+updatedDate+"','"+providerID+"', 'You have a new request',  'Pending')\n");

    }

    /*public Cursor getBreakdownRequestData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select *  from BreakDownRequest", null);
        return  cursor;
    }*/

    public Cursor getBreakdownRequestDataForUser(String userId){
        SQLiteDatabase DB = this.getWritableDatabase();
        String query = "SELECT * FROM BreakDownRequest WHERE User_ID = ?";
        Cursor cursor = DB.rawQuery(query, new String[]{userId});
        return cursor;
    }

    public void updateProviderRating(String providerId, float rating) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Rating", rating);
        db.update("ServiceProvider", values, "ID=?", new String[]{providerId});
    }

    public float getProviderRating(String providerId) {
        float rating = -1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Rating FROM ServiceProvider WHERE ID = ?", new String[]{providerId});

        if (cursor != null && cursor.moveToFirst()) {
            rating = cursor.getFloat(0);
            cursor.close();
        }

        return rating;
    }

    public float getRating(String uId) {
        SQLiteDatabase db = this.getWritableDatabase();
        float rate = 0.0F;
        String query = "SELECT Rating FROM ServiceProvider WHERE ID = ?";
        String[] selectionArgs = { uId }; // Pass uId as selection argument

        Cursor cursor = db.rawQuery(query, selectionArgs);
        rate = cursor.getColumnIndex("rate"); // Get the rate from the cursor
        cursor.close();

        return rate;
    }

    public Cursor getRequestData(String uid) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select b.Breakdown_Type, b.Location, b.Description, b.Created_Date, b.Updated_Date, b.Image, b.User_ID, b.Provider_ID," +
                "u.Name,u.Phone_No, b.ID, b.Status, b.Image  from BreakDownRequest as b INNER JOIN User as u ON b.user_id = u.id WHERE (Status ='Pending' OR Status ='Accept') and Provider_Id = '" + uid + "' ORDER BY Updated_Date DESC", null);
        return cursor;
    }

    public Cursor getRequestHistoryData(String uid) {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select b.Breakdown_Type, b.Location, b.Description, b.Created_Date, b.Updated_Date, b.Image, b.User_ID, b.Provider_ID," +
                "u.Name,u.Phone_No, b.ID, b.Status, b.Image  from BreakDownRequest as b INNER JOIN User as u ON b.user_id = u.id WHERE Status ='Done' OR Status ='Reject' and Provider_Id = '" + uid + "' ORDER BY Updated_Date DESC", null);
        return cursor;
    }

    public void acceptRequest(int breakDownRequestId, String userId, String createdDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE BreakDownRequest SET Status = 'Accept' WHERE ID = " + breakDownRequestId + "");
        db.execSQL("INSERT INTO Notification (Created_Date, Updated_Date, RECIVER_ID, Message, Status) VALUES ('"+createdDate+"', '"+createdDate+"','"+userId+"', 'Your request is accepted',  'Pending')\n");

    }

    public void rejectRequest(int breakDownRequestId, String userId, String createdDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE BreakDownRequest SET Status = 'Reject' WHERE ID = " + breakDownRequestId + "");
        db.execSQL("INSERT INTO Notification (Created_Date, Updated_Date, RECIVER_ID, Message, Status) VALUES ('2024-04-5', NULL,'"+userId+"', 'Your have new request',  'Pending')\n");
        db.execSQL("INSERT INTO Notification (Created_Date, Updated_Date, RECIVER_ID, Message, Status) VALUES ('"+createdDate+"', '"+createdDate+"','"+userId+"', 'Your request is rejeced',  'Pending')\n");

    }

    public boolean updateStatus(String breakDownRequestId,String userId, String createdDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("UPDATE BreakDownRequest SET Status = 'Done' WHERE ID = " + breakDownRequestId + "");
            db.execSQL("INSERT INTO Notification (Created_Date, Updated_Date, RECIVER_ID, Message, Status) VALUES ('"+createdDate+"', '"+createdDate+"','"+userId+"', 'Your request is completed',  'Pending')\n");

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public int getCountOfServiceRequested(String uId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM BreakDownRequest WHERE User_ID = ?";
        String[] selectionArgs = { uId }; // Pass uId as selection argument

        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count;

    }

    public int getCountOfServiceProvided(String uId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM BreakDownRequest WHERE Provider_ID = ?";
        String[] selectionArgs = { uId }; // Pass uId as selection argument

        Cursor cursor = db.rawQuery(query, selectionArgs);
        int count = 0;
        count = cursor.getCount();
        cursor.close();
        return count;
    }
    public Cursor getNotificationDetails(String uid){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select ID, Message, Updated_Date from Notification WHERE RECIVER_ID = '"+uid + "' ORDER BY Updated_Date DESC", null);

        return  cursor;
    }

    public void updateNotificationStatus(int notificationId, String currentDateAndTime) {
        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL("UPDATE Notification SET Status = 'Viewed', Updated_Date = '"+currentDateAndTime+"' WHERE ID = "+notificationId);
    }

    public List<ServiceProvider> getAllProviders() {
        List<ServiceProvider> providers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM ServiceProvider", null);
        if (cursor != null) {
            try {
                int idIndex = cursor.getColumnIndexOrThrow("ID");
                int locationIndex = cursor.getColumnIndexOrThrow("Location");
                int breakdownTypeIndex = cursor.getColumnIndexOrThrow("BreakDownType");
                int ratingIndex = cursor.getColumnIndexOrThrow("Rating");

                while (cursor.moveToNext()) {
                    ServiceProvider provider = new ServiceProvider();
                    provider.setId(cursor.getString(idIndex));
                    provider.setLocation(cursor.getString(locationIndex));
                    provider.setBreakdownType(cursor.getString(breakdownTypeIndex));
                    provider.setRating(cursor.getFloat(ratingIndex));

                    String providerId = cursor.getString(idIndex);
                    String providerName = getProviderNameById(providerId);
                    provider.setName(providerName);

                    providers.add(provider);
                }
            } finally {
                cursor.close();
            }
        }

        return providers;
    }

    @SuppressLint("Range")
    public List<ServiceProvider> getProvidersByCity(String city) {
        List<ServiceProvider> providers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] selectionArgs = {city};
        Cursor cursor = db.rawQuery("SELECT * FROM ServiceProvider WHERE Location = ?", selectionArgs);
        if (cursor != null) {
            try {
                int idIndex = cursor.getColumnIndexOrThrow("ID");
                int locationIndex = cursor.getColumnIndexOrThrow("Location");
                int breakdownTypeIndex = cursor.getColumnIndexOrThrow("BreakDownType");
                int ratingIndex = cursor.getColumnIndexOrThrow("Rating");

                while (cursor.moveToNext()) {
                    ServiceProvider provider = new ServiceProvider();
                    provider.setId(cursor.getString(idIndex));
                    provider.setLocation(cursor.getString(locationIndex));
                    provider.setBreakdownType(cursor.getString(breakdownTypeIndex));
                    provider.setRating(cursor.getFloat(ratingIndex));


                    String providerId = cursor.getString(idIndex);
                    String providerName = getProviderNameById(providerId);
                    provider.setName(providerName);

                    providers.add(provider);
                }
            } finally {
                cursor.close();
            }
        }

        db.close();

        return providers;
    }

    private String getProviderNameById(String providerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String providerName = null;

        Cursor cursor = db.rawQuery("SELECT Name FROM User WHERE ID = ?", new String[]{providerId});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndexOrThrow("Name");
                    providerName = cursor.getString(nameIndex);
                }
            } finally {
                cursor.close();
            }
        }

        return providerName;
    }

    @SuppressLint("Range")
    public String getUserName(String userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Name FROM User WHERE ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        String userName = "";
        if (cursor != null && cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
        }
        return userName;
    }

    @SuppressLint("Range")
    public String getProviderName(String providerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Name FROM User WHERE ID = ?";
        Cursor cursor = db.rawQuery(query, new String[]{providerId});
        String providerName = "";
        if (cursor != null && cursor.moveToFirst()) {
            providerName = cursor.getString(cursor.getColumnIndex("Name"));
            cursor.close();
        }
        return providerName;
    }

    public String getProviderLocation(String providerId) {
        String providerLocation = "";
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String providerLocationQuery = "SELECT Location FROM ServiceProvider WHERE ID = '" + providerId + "'";
            Cursor providerLocationCursor = db.rawQuery(providerLocationQuery, null);
            if (providerLocationCursor != null && providerLocationCursor.moveToFirst()) {
                int providerLocationIndex = providerLocationCursor.getColumnIndex("Location");
                providerLocation = (providerLocationIndex != -1) ? providerLocationCursor.getString(providerLocationIndex) : "";
                providerLocationCursor.close();
            }
        }
        return providerLocation;
    }

    public Map<String, String> getProviderIdAndStatus() {
        Map<String, String> providerIdAndStatusMap = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null) {
            String query = "SELECT Provider_ID, Status FROM BreakDownRequest";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") String providerId = cursor.getString(cursor.getColumnIndex("Provider_ID"));
                    @SuppressLint("Range") String status = cursor.getString(cursor.getColumnIndex("Status"));
                    providerIdAndStatusMap.put(providerId, status);
                } while (cursor.moveToNext());
                cursor.close();
            }
        }
        return providerIdAndStatusMap;
    }

     @SuppressLint("Range")
    public String getLatestCreatedDateForUser(String userID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  Created_Date FROM  BreakDownRequest WHERE User_ID = ? ORDER BY ID DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{userID});

        String latestDate = "None received";
        if (cursor.moveToFirst()) {
            latestDate = cursor.getString(cursor.getColumnIndex("Created_Date"));
        }

        cursor.close();
        return latestDate;
    }

    public void addEmergencyContact() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO EmergencyContact (Contact_Name, Contact_Number) VALUES ( 'Emergency Hotline','911')\n");
        db.execSQL("INSERT INTO EmergencyContact (Contact_Name, Contact_Number) VALUES ( 'Report a spill','1-800-663-3456')\n");
        db.execSQL("INSERT INTO EmergencyContact (Contact_Name, Contact_Number) VALUES ( 'ICBC','1-800-663-3051')\n");
        db.execSQL("INSERT INTO EmergencyContact (Contact_Name, Contact_Number) VALUES ( 'Video Relay Services','1-604-215-5101')\n");
        db.execSQL("INSERT INTO EmergencyContact (Contact_Name, Contact_Number) VALUES ( 'Wildfire','1-800-663-5555')\n");
    }

    public Cursor getEmergencyContact(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select *  from EmergencyContact", null);
        return  cursor;
    }

    public void clearEmergencyContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("EmergencyContact", null, null);
        db.close();
    }

}
