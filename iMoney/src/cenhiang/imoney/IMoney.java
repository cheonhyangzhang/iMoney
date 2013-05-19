package cenhiang.imoney;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;

public class IMoney extends Activity {
	/** Called when the activity is first created. */
	private int firstRun = 0;
	private usefulTools uftools = new usefulTools();
	private String[] groupCollection = new String[]{"","≥‘∑π","¡„ ≥"};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imoney);
		iMoneyData dbHelper = new iMoneyData(IMoney.this, "iMoneyData");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("settings", new String[] { "firstrun", "id" },"id=?", new String[] { "0" }, null, null, null);
		while (cursor.moveToNext()) {
			firstRun = cursor.getInt(cursor.getColumnIndex("firstrun"));
		}
		if (firstRun == 1) {
			ContentValues values = new ContentValues();
			values.put("firstrun", 0);
			db.update("settings", values, "id=?", new String[] { "0" });
			
			for (int i = 1; i <= 2; i++) {
				uftools.insertGroup(IMoney.this, i, groupCollection[i], 0, 0, 1);
			}
			uftools.insertSecondGroup(IMoney.this, 1,1, "‘Á∑π", 1);
			uftools.insertSecondGroup(IMoney.this, 1,2,  "ŒÁ∑π", 1);
			uftools.insertSecondGroup(IMoney.this, 1,3,  "ÕÌ∑π", 1);
		}
		
		//check table
		 Time tempTime = new Time();
		 tempTime.setToNow();
		try {
			db.query("expensedetail"+tempTime.year+tempTime.month, new String[]{"groupid"}, null, null, null, null, null);
			System.out.println("check ok");
		}
		catch(Exception e){
			System.out.println("check create new table");
			db.execSQL("create table "+"expensedetail"+tempTime.year+tempTime.month+"(groupid int, secondgroupid int,day int,weekday int,expense money, addition varchar(40))");
		}
		
		
		cursor.close();
		db.close();
		finish();

		Intent intent = new Intent(IMoney.this, main.class);
		IMoney.this.startActivity(intent);
	}
}