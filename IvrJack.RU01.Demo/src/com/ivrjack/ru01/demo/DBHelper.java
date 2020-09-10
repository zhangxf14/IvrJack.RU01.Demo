package com.ivrjack.ru01.demo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 *@author Sai
 *Created on 2014年9月10日 下午21:53:39
 *类说明：数据库
 */
public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context) {
		this(context, Constants.DBNAME, null, Constants.VERSION);
	}
	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+Constants.TB_USER+"(" +
				Constants.USERCODE_OUTPUT+" varchar(20) PRIMARY KEY," +
				Constants.USERNAME_OUTPUT+" varchar(20)" +
				");");
		db.execSQL("CREATE TABLE "+Constants.TB_PRODUCT+"(" +
				Constants.UID_OUTPUT+" varchar(20)," +
				Constants.USERCODE_OUTPUT+" varchar(20)," +
				Constants.ID_OUTPUT+" varchar(20)," +
				Constants.ISSUEDATE_OUTPUT+" Long," +
				Constants.DELIVER_OUTPUT+" varchar(20)," +
				Constants.RECEIVER_OUTPUT+" varchar(20)," +
				Constants.TRANSPORT_OUTPUT+" varchar(20)," +
				Constants.GOODSNAME_OUTPUT+" varchar(20)," +
				Constants.GOODSQUANTITY_OUTPUT+" Double," +
				Constants.TRANSPORTDAY_OUTPUT+" Integer," +
				Constants.GOODSSTATE_OUTPUT+" Integer," +
				Constants.INITDATE_OUTPUT+" Long," +
				Constants.INTOSTORAGEDATE_OUTPUT+" Long," +
				Constants.OUTSTORGEDATE_OUTPUT+" Long," +
				Constants.SIGNDATE_OUTPUT+" Long," +
				Constants.SPECIFICATION_OUTPUT+" varchar(20)," +
				Constants.BATCHNO_OUTPUT+" varchar(20)," +
				Constants.EXPIRYDATE_OUTPUT+" Long," +
				Constants.PRODUCEDATE_OUTPUT+" Long," +
				Constants.APPROVALNO_OUTPUT+" varchar(20)," +
				Constants.PIATS+" varchar(20)," +
				Constants.DATETIME_OUTPUT+" Long," +
				Constants.INTERVAL+" Integer," +
				Constants.MAXTEMPERATURE+" Double," +
				Constants.MINTEMPERATURE+" Double," +
				Constants.TEMPERATURE_OUTPUT+" Double," +
				Constants.BATTERYVOLTAGE+" Double," +
				Constants.STATUS+" Integer," +
				Constants.ACTIVE+" varchar(20)," +
				Constants.NUMMEASUREMENTS+" Integer," +
				Constants.NUMEXCEEDED+" Integer," +				
				Constants.CHECKFROMINTERNET+" Integer," +
				Constants.TEMPERATUREMAX+" Double," +
				Constants.TEMPERATUREMIN+" Double," +
				Constants.TEMPERATUREMEAN+" Double," +
				Constants.TRANKINGNO_OUTPUT+" varchar(20)" +
//				"PRIMARY KEY(" +Constants.UID_OUTPUT+","+Constants.USERCODE_OUTPUT+")"+
				");");
		db.execSQL("CREATE TABLE "+Constants.TB_TEMPERATURE+"(" +
				Constants.UID_OUTPUT+" varchar(20)," +
				Constants.DATETIME_OUTPUT+" Long," +
				Constants.TEMPERATURE_OUTPUT+" Double," +
				Constants.STATUS+" Integer," +
				Constants.MAXTEMPERATURE+" Double," +
				Constants.MINTEMPERATURE+" Double" +
				");");
		
		db.execSQL("CREATE TABLE "+Constants.TB_DEVICE+"(" +
				Constants.DEVICE_NAME+" varchar(20) PRIMARY KEY," +
				Constants.DEVICE_ADDRESS+" varchar(30)" +
				");");
		
		db.execSQL("CREATE TABLE "+Constants.TB_PARAMETERSET+"(" +
				Constants.PARA_STARTTYPE+" int ," +
				Constants.PARA_DELAYTIME+" varchar(30)," +
				Constants.PARA_LOGINTERVAL+" varchar(30)," +
				Constants.PARA_UPPERLIMIT+" varchar(30)," +
				Constants.PARA_LOWERLIMIT+" varchar(30)," +
				Constants.PARA_STORAGERULE+" int," +
				Constants.PARA_LOGFORM+" int" +
				
				");");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
