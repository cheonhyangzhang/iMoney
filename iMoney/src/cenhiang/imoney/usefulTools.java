package cenhiang.imoney;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.Time;

public class usefulTools {
	private String chyear = "Äê";
	private String chmonth = "ÔÂ";
	private String chday = "ÈÕ";
	
	public String dateGenerate(int year,int month,int day){
		
		String date=null;
		date=year+chyear+month+chmonth+day+chday;
		
		return date;
	}
	public String dateGenerate(int year,int month){
		
		String date=null;
		date=year+chyear+month+chmonth;
		
		return date;
	}
	public String dateGenerate(int day){
		
		String date=null;
		date=day+chday;
		
		return date;
	}
	public void insertExpenseDetails(Context context,int groupid,int secondgroupid,double expense,String addition){
		
		 iMoneyData dbHelper = new iMoneyData (context,"iMoneyData");
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
		 Time tempTime = new Time();
		 tempTime.setToNow();
		 ContentValues values = new ContentValues();
		 values.put("groupid", groupid);
		 values.put("secondgroupid", secondgroupid);
		 values.put("day", tempTime.monthDay);
		 values.put("weekday", tempTime.weekDay);
		 values.put("expense", expense);
		 values.put("addition", addition); 
		
		 db.insert("expensedetail"+tempTime.year+tempTime.month, null, values);
		 db.close();
	}
	public void insertSecondGroup(Context context,int groupid,int secondgroupid,String name,int inuse){
		 int secondgroupnumber=0,secondmaxgroupnumber=0;
		 iMoneyData dbHelper = new iMoneyData (context,"iMoneyData");
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
		 Cursor cursor = db.query("expensegroup", new String[] { "secondgroupnumber","secondmaxgroupnumber ","id" },"id=?", new String[] { groupid+"" }, null, null, null);
			while (cursor.moveToNext()) {
				secondgroupnumber= cursor.getInt(cursor.getColumnIndex("secondgroupnumber"));
				secondmaxgroupnumber= cursor.getInt(cursor.getColumnIndex("secondmaxgroupnumber"));
			}
		 ContentValues values = new ContentValues();
			values.put("groupid", groupid);
			values.put("secondgroupid",secondgroupid );
			values.put("name", name);
			values.put("inuse",inuse );
			db.insert("secondgroup", null, values);
			values = new ContentValues();
			values.put("secondgroupnumber", secondgroupnumber+1);
			values.put("secondmaxgroupnumber", secondmaxgroupnumber+1);
			db.update("expensegroup", values, "id=?", new String[]{groupid+""});	
		db.close();
	}
	public void insertGroup(Context context,int id,String name,int secondgroupnumber,int secondmaxgroupnumber,int inuse){
		 System.out.println("usefulTools insertGroup start!");
		 iMoneyData dbHelper = new iMoneyData (context,"iMoneyData");
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
		 ContentValues values = new ContentValues();
     	 values.put("id", id);
		 values.put("name", name);
		 values.put("secondgroupnumber",secondgroupnumber );
		 values.put("secondmaxgroupnumber",secondmaxgroupnumber );
		 values.put("inuse",inuse );
		 db.insert("expensegroup", null, values);
		 db.close();
	}
}
