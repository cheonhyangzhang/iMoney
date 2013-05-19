package cenhiang.imoney;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.text.format.Time;

public class iMoneyData extends SQLiteOpenHelper{
	private static final int Version = 1;
	
	
	//必须有构造函数
	public iMoneyData(Context context, String name, CursorFactory factory, int version){
		super(context, name, factory, version);
	}
	public iMoneyData(Context context, String name){
		this(context,name,Version);
	}
	public iMoneyData(Context context, String name, int version){
		this(context, name, null, version);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("Create a TimeDatabase");
		db.execSQL("create table settings (currenttab int,additionexpand int,id int,groupnumber int,maxgroupnumber int,defaultcountry int,firstrun)");
		db.execSQL("insert into settings (currenttab,additionexpand,id,groupnumber ,maxgroupnumber ,defaultcountry,firstrun ) Values(0,0,0,2,2,0,1)");
				db.execSQL("create table expensegroup (id int,name varchar(20),secondgroupnumber int,secondmaxgroupnumber int,inuse int)");
		//dbdb.execSQL("insert into expensegroup (id ,nextid ,name ,secondgroupnumber ,secondmaxgroupnumber ,inuse ) Values(2,3,'零食',0,0,1)");
		
		
      
		db.execSQL("create table secondgroup (groupid int,secondgroupid int,name varchar(20),inuse int)");
		//db.execSQL("insert into secondGroup (groupid ,secondgroupid ,nextid,name ,inuse ) Values(1,1,'早饭',1)");
		//db.execSQL("insert into secondGroup (groupid ,secondgroupid ,nextid,name ,inuse ) Values(1,2,'午饭',1)");
		//db.execSQL("insert into secondGroup (groupid ,secondgroupid ,nextid,name ,inuse ) Values(1,3,'晚饭',1)");
		
		Time tempTime = new Time();
		tempTime.setToNow();
		db.execSQL("create table "+"expensedetail"+tempTime.year+tempTime.month+"(groupid int, secondgroupid int,day int,weekday int,expense money, addition varchar(40))");
		
		
	}
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
	
}