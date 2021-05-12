package com.emsphere.commando4.kirloskarempowerapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import com.emsphere.commando4.kirloskarempowerapp.markhistory.EmployeeMarkHistory;
import com.emsphere.commando4.kirloskarempowerapp.pojo.EmployeeDeviceStatusPoJo;
import com.emsphere.commando4.kirloskarempowerapp.pojo.OffLineAttendancePoJo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by commando1 on 8/1/2017.
 */

public class KirloskarEmpowerDatabase extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "KirloskarEmpowerDB.db";
    static final int DATABASE_VERSION =2;
    SQLiteDatabase ReadDB = this.getReadableDatabase();
    SQLiteDatabase WriteDB = this.getWritableDatabase();
    ContentValues initialValues = new ContentValues();
    Cursor c;
    String res;

    private static final String TABLE_NAME1 = "AttendanceMark1";

    private static final String id = "id";
    private static final String mobile_attendance_emp_id = "Mobile_Attendance_Emp_Id";
    private static final String date = "Date";
    private static final String in_time = "In_Time";
    private static final String out_time = "Out_Time";
    private static final String in_time_latitude = "In_Time_Latitude";
    private static final String out_time_latitude = "Out_Time_Latitude";
    private static final String in_time_longitude = "In_Time_Longitude";
    private static final String out_time_longitude = "Out_Time_Longitude";
    private static final String in_photo = "In_Photo";
    private static final String out_photo = "Out_Photo";
    private static final String in_remark = "In_Remark";
    private static final String out_remark = "Out_Remark";
    private static final String is_sync = "Is_Sync";


    private static final String TABLE_NAME4 = "OfflineAttendanceMark1";
    private static final String id_mark = "id";

    private static final String swipe_Time = "swipeTime";
    private static final String latitude = "latitude";
    private static final String longitude = "longitude";
    private static final String door = "door";
    private static final String location_Address = "locationAddress";
    private static final String swipe_ImageFileName = "swipeImageFileName";
    private static final String isOnline_Swipe = "isOnlineSwipe";
    private static final String remark = "remark";
    private static final String lProvider = "LocationProvider";



    //last punch history....
    private static final String TABLE_NAME2 = "MarkAttendanceHistory1";

    private static final String mark_id = "id";
    private static final String mark_attendance_emp_id = "Mark_Attendance_Emp_Id";
    private static final String mark_date = "Mark_Date";
    private static final String mark_time = "Mark_Time";
    private static final String mark_status = "Mark_Status";
    //.......................
    //Personal Details....
    private static final String TABLE_NAME3 = "PersonalDetails1";
    private static final String Employee_ID = "EmployeeID";
    private static final String First_Name = "FirstName";
    private static final String Last_Name = "LastName";
    private static final String Employee_Image = "EmployeeImage";
    private static final String Gender = "Gender";
    private static final String OfficalEmail_ID = "OfficalEmailID";
    private static final String Designation_Name = "DesignationName";
    private static final String Birth_Date = "BirthDate";

    //Personal Details....
    private static final String TABLE_NAME5 = "EmployeeDeviceStatus";
    private static final String RecordID = "id";
    private static final String Date_Time = "DateTime";
    private static final String GPS_Status = "GPSStatus";
    private static final String Internate_Status = "InternateStatus";
    private static final String Battery_Status = "BatteryStatus";
    private static final String Device_Switch_Off_Status = "DeviceSwitchOffStatus";


    public KirloskarEmpowerDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME1
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,Mobile_Attendance_Emp_Id TEXT,Date TEXT,In_Time TEXT,Out_Time TEXT,In_Time_Latitude TEXT,Out_Time_Latitude TEXT,In_Time_Longitude TEXT,Out_Time_Longitude TEXT,In_Photo TEXT,Out_Photo TEXT,In_Remark TEXT,Out_Remark TEXT,Is_Sync TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME2
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,Mark_Attendance_Emp_Id TEXT,Mark_Date TEXT,Mark_Time TEXT,Mark_Status TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME3
                + " (EmployeeID INTEGER PRIMARY KEY ,FirstName TEXT,LastName TEXT,EmployeeImage TEXT,Gender TEXT,OfficalEmailID TEXT,DesignationName TEXT,BirthDate TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME4
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,swipeTime TEXT,latitude TEXT,longitude TEXT,door TEXT,locationAddress TEXT,swipeImageFileName TEXT,isOnlineSwipe TEXT,remark TEXT,LocationProvider TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_NAME5
                + " (id INTEGER PRIMARY KEY AUTOINCREMENT,DateTime TEXT,GPSStatus TEXT,InternateStatus TEXT,BatteryStatus TEXT,DeviceSwitchOffStatus TEXT);");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME4);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME5);
        onCreate(db);
    }

    public void deleteAllData() {

        SQLiteDatabase ReadDB = this.getReadableDatabase();
    }
    public void deleteAllTablesFromDtabase() {

        SQLiteDatabase ReadDB = this.getReadableDatabase();
    }

    public String InsertMobileAttendance(String mobile_attendance_emp_id1,
                                         String date1, String in_time1, String out_time1,
                                        String in_time_latitude1, String in_time_longitude1,
                                         String out_time_latitude1, String out_time_longitude1,
                                         String in_photo1, String out_photo1, String in_remark1, String out_remark1, String is_sync1) {

        SQLiteDatabase db = this.getWritableDatabase();

        initialValues.clear();
        initialValues.put(mobile_attendance_emp_id, mobile_attendance_emp_id1);
        initialValues.put(date, date1);
        initialValues.put(in_time, in_time1);
        initialValues.put(out_time, out_time1);
        initialValues.put(in_time_latitude, in_time_latitude1);
        initialValues.put(in_time_longitude, in_time_longitude1);
        initialValues.put(out_time_latitude, out_time_latitude1);
        initialValues.put(out_time_longitude, out_time_longitude1);
        initialValues.put(in_photo, in_photo1);
        initialValues.put(out_photo, out_photo1);
        initialValues.put(in_remark, in_remark1);
        initialValues.put(out_remark, out_remark1);
        initialValues.put(is_sync, is_sync1);

        Log.i("initialvalues", "photo" + initialValues);

        long rowId = db.insert(TABLE_NAME1, null, initialValues);

        return rowId + "";
    }



    //last punch history...

    public String InsertMarkAttendanceHistory(String mobile_attendance_emp_id1,
                                              String date1, String time1, String status1) {

        SQLiteDatabase db = this.getWritableDatabase();

        long rowId = 0;
        initialValues.clear();
        initialValues.put(mark_attendance_emp_id, mobile_attendance_emp_id1);
        initialValues.put(mark_date, date1);
        initialValues.put(mark_time, time1);
        initialValues.put(mark_status, status1);

       // Log.i("initialvalues", "mark_attendance_history" + initialValues);


        rowId = db.insert(TABLE_NAME2, null, initialValues);
        Log.d("","InsertedRows:" + rowId);

        return rowId + "";
    }


    public ArrayList<LinkedHashMap<String, String>> getLastPunchesHistoryData() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<LinkedHashMap<String, String>> menuItems = new ArrayList<LinkedHashMap<String, String>>();
        String st = "", from_d = "";
        String str1 = " Select * From " + TABLE_NAME2 + " Order by " + mark_id +
                " desc limit 1,5";
        Cursor c = db.rawQuery(str1, null);
        res = " ";
        if (c.moveToFirst()) {
            do {
                LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
                String date1 = c.getString(c.getColumnIndexOrThrow(mark_date));
                String time1 = c.getString(c.getColumnIndexOrThrow(mark_time));
                map.put("1", date1);
                map.put("2", c.getString(c.getColumnIndexOrThrow(mark_status)));
                menuItems.add(map);
            } while (c.moveToNext());
        }
        c.close();
        return menuItems;
    }


    public LinkedHashMap<String,String>GetLast()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String str1 = " Select * From " + TABLE_NAME2 + " Order by " + mark_id +
                " desc limit 1,5";
        Cursor c = db.rawQuery(str1, null);
        LinkedHashMap<String, String> map=null;
        res = " ";
        if (c.moveToFirst()) {
            do {
                map = new LinkedHashMap<String, String>();

                String date1 = c.getString(c.getColumnIndexOrThrow(mark_date));
                String time1 = c.getString(c.getColumnIndexOrThrow(mark_time));
                map.put("punchdate", date1);
                map.put("punchstatus", c.getString(c.getColumnIndexOrThrow(mark_status)));
                //menuItems.add(map);
            } while (c.moveToNext());
        }
        c.close();
        return map;

    }

    public ArrayList<EmployeeMarkHistory>GetLastPunches()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String str1 = " Select * From " + TABLE_NAME2 + " Order by " + mark_id +
                " desc limit 0,10";
        Cursor c = db.rawQuery(str1, null);
        String header_datetxt= "<b> <font color='#1c2039'>Date</font> </b> ";

        String header_punchstatustxt="<b><font color='#1c2039'>IN/OUT </font></b>";
        String status = "<b><font color='#1c2039'>Status</font></b>";

        ArrayList<EmployeeMarkHistory> map=new ArrayList<>();
        EmployeeMarkHistory employeeMarkHistory1 =new EmployeeMarkHistory();
        employeeMarkHistory1.setDate(header_datetxt);
        employeeMarkHistory1.setData(header_punchstatustxt);
        employeeMarkHistory1.setStatus(status);

        map.add(employeeMarkHistory1);

        if (c.moveToFirst()) {
            do {
                EmployeeMarkHistory employeeMarkHistory =new EmployeeMarkHistory();

                 employeeMarkHistory.setDate(c.getString(c.getColumnIndexOrThrow(mark_date)));


                employeeMarkHistory.setData(c.getString(c.getColumnIndexOrThrow(mark_status)));
                map.add(employeeMarkHistory);
            } while (c.moveToNext());
        }
        c.close();
        return map;

    }

    public Spanned getLastEntry() {
        SQLiteDatabase db = this.getReadableDatabase();
        String str_last_entry = "";
        String sql = " Select * From " + TABLE_NAME2 + " Order by " + mark_id +
                " desc limit 1";
        Cursor c = db.rawQuery(sql,null);
        int num = c.getCount();

        if (num > 0) {
            c.moveToFirst();
            if((c.getString(c.getColumnIndexOrThrow(mark_status)).toString().equals("IN")))
            {
                str_last_entry = c.getString(c.getColumnIndexOrThrow(mark_date)) + " " + c.getString(c.getColumnIndexOrThrow(mark_time)) + " " +"<font color='#30b257'> "+c.getString(c.getColumnIndexOrThrow(mark_status))+"</font>";
            }
           else if((c.getString(c.getColumnIndexOrThrow(mark_status)).toString().equals("OUT")))
            {
                str_last_entry = c.getString(c.getColumnIndexOrThrow(mark_date)) + " " + c.getString(c.getColumnIndexOrThrow(mark_time)) + " "+"  <font color='#d3233e'>" + c.getString(c.getColumnIndexOrThrow(mark_status))+"</font";
            }
           // "<font color='#30b257'>    <font color='#d3233e'>
        }
        c.close();
        return Html.fromHtml(str_last_entry);
    }


    public String InsertPersonalDetails(int employee_ID,
                                              String first_Name, String last_Name, String employee_Image,String _Gender,String officalEmail_ID,String designation_Name,String BirthDate) {

        SQLiteDatabase db = this.getWritableDatabase();

        long rowId = 0;
        initialValues.clear();
        initialValues.put(Employee_ID, employee_ID);
        initialValues.put(First_Name, first_Name);
        initialValues.put(Last_Name, last_Name);
        initialValues.put(Employee_Image, employee_Image);
        initialValues.put(Gender, _Gender);
        initialValues.put(OfficalEmail_ID, officalEmail_ID);
        initialValues.put(Designation_Name, designation_Name);
        initialValues.put(Birth_Date, BirthDate);

        Log.i("initialvalues", "mark_attendance_history" + initialValues);


        rowId = db.insert(TABLE_NAME3, null, initialValues);
        Log.d("","InsertedRows:" + rowId);

        return rowId + "";
    }


    public LinkedHashMap<String, String> getPersonalDetails(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<LinkedHashMap<String, String>> menuItems = new ArrayList<LinkedHashMap<String, String>>();
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        String st = "", from_d = "";
        String str1 = " Select * From " + TABLE_NAME3 + " Where " +Employee_ID +"='" + id + "'";
        //String str2 ="SELECT * FROM tbl1 WHERE name = '"+name+"'"  //" where " + PEOPLE_RATION_NUMBER + "='" + rationNumber + "'"
        Cursor c = db.rawQuery(str1, null);
        res = " ";
        if (c.moveToFirst()) {

                //LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
               // String date1 = c.getString(c.getColumnIndexOrThrow(mark_date));
                //String time1 = c.getString(c.getColumnIndexOrThrow(mark_time));
            map.put("FirstName",c.getString(c.getColumnIndexOrThrow(First_Name)));
            map.put("Last_Name",c.getString(c.getColumnIndexOrThrow(Last_Name)));
            map.put("Employee_Image",c.getString(c.getColumnIndexOrThrow(Employee_Image)));
            map.put("Gender",c.getString(c.getColumnIndexOrThrow(Gender)));
            map.put("OfficalEmail_ID",c.getString(c.getColumnIndexOrThrow(OfficalEmail_ID)));
            map.put("Designation_Name",c.getString(c.getColumnIndexOrThrow(Designation_Name)));
            map.put("Birth_Date",c.getString(c.getColumnIndexOrThrow(Birth_Date)));



                //menuItems.add(map);

        }
        c.close();
        return map;
    }



    public String InsertSwapDetails(String swipe_time, String _latitude, String _longitude,String _door,String _location_Address,String _swipe_ImageFileName,String _isOnline_Swipe,String _remark,String _lprovider) {

        SQLiteDatabase db = this.getWritableDatabase();

        long rowId = 0;
        initialValues.clear();
       // initialValues.put(id_mark, "");
        initialValues.put(swipe_Time, swipe_time);
        initialValues.put(latitude, _latitude);
        initialValues.put(longitude, _longitude);
        initialValues.put(door, _door);
        initialValues.put(location_Address, _location_Address);
        initialValues.put(swipe_ImageFileName, _swipe_ImageFileName);
        initialValues.put(isOnline_Swipe, _isOnline_Swipe);
        initialValues.put(remark,_remark);
        initialValues.put(lProvider,_lprovider);

       // Log.i("initialvalues", "mark_attendance" + initialValues);


        rowId = db.insert(TABLE_NAME4, null, initialValues);
        Log.d("","InsertedRows:" + rowId);

        return rowId + "";
    }


    public List<OffLineAttendancePoJo> getAttendenceList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME4, null);
        List<OffLineAttendancePoJo> empList = new ArrayList<OffLineAttendancePoJo>();
        if (c != null && c.moveToFirst()) {
            do {

                OffLineAttendancePoJo MarkData=new OffLineAttendancePoJo();
                MarkData.setId_mark(c.getString(c.getColumnIndex(id_mark)));
                MarkData.setSwipeTime(c.getString(c.getColumnIndex(swipe_Time)));
                MarkData.setLatitude(c.getString(c.getColumnIndex(latitude)));
                MarkData.setLongitude(c.getString(c.getColumnIndex(longitude)));
                MarkData.setDoor(c.getString(c.getColumnIndex(door)));
                MarkData.setLocationAddress(c.getString(c.getColumnIndex(location_Address)));
                MarkData.setSwipeImageFileName(c.getString(c.getColumnIndex(swipe_ImageFileName)));
                MarkData.setIsOnlineSwipe(c.getString(c.getColumnIndex(isOnline_Swipe)));
                MarkData.setRemark(c.getString(c.getColumnIndex(remark)));
                MarkData.setLocationProvider(c.getString(c.getColumnIndex(lProvider)));


                empList.add(MarkData);
            } while (c.moveToNext());
        }
        return  empList;
    }
    public void deleteMarkDataRecord(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME4, id_mark + "=" + code, null);
    }


    public String InsertDeviceStatusDetails(String datetime, String gpsstatus, String internatestatus,String batterystatus,String Deviceonoff) {

        SQLiteDatabase db = this.getWritableDatabase();

        long rowId = 0;
        initialValues.clear();
        // initialValues.put(id_mark, "");
        initialValues.put(Date_Time, datetime);
        initialValues.put(GPS_Status, gpsstatus);
        initialValues.put(Internate_Status, internatestatus);
        initialValues.put(Battery_Status,batterystatus);
        initialValues.put(Device_Switch_Off_Status, Deviceonoff);
        rowId = db.insert(TABLE_NAME5, null, initialValues);
        Log.d("","InsertedRows:" + rowId);

        return rowId + "";
    }

    public void DeleteDeviceStatusDataRecord(String code){
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME5, RecordID + "=" + code, null);
    }

    public List<EmployeeDeviceStatusPoJo> getDeviceStatusList(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM "+TABLE_NAME5, null);
        List<EmployeeDeviceStatusPoJo> empList = new ArrayList<EmployeeDeviceStatusPoJo>();
        if (c != null && c.moveToFirst()) {
            do {

                EmployeeDeviceStatusPoJo DeviceStatusData=new EmployeeDeviceStatusPoJo();
                DeviceStatusData.setRecordid(c.getString(c.getColumnIndex(RecordID)));
                DeviceStatusData.setDatetime(c.getString(c.getColumnIndex(Date_Time)));
                DeviceStatusData.setIsgpsenable(c.getString(c.getColumnIndex(GPS_Status)));
                DeviceStatusData.setIsinternateenable(c.getString(c.getColumnIndex(Internate_Status)));
                DeviceStatusData.setIsbatterylow(c.getString(c.getColumnIndex(Battery_Status)));
                DeviceStatusData.setDeviceswitchoff(c.getString(c.getColumnIndex(Device_Switch_Off_Status)));



                empList.add(DeviceStatusData);
            } while (c.moveToNext());
        }
        return  empList;
    }

    public int getCountOfOfflineRecords(){
        SQLiteDatabase db = this.getReadableDatabase();
        long numRows = DatabaseUtils.queryNumEntries(db, TABLE_NAME4);
        return (int) numRows;
    }



}
