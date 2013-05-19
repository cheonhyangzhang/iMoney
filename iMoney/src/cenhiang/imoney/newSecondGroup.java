package cenhiang.imoney;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class newSecondGroup extends Activity{
	private Button newsecondgroupCancel = null;
	private Button newsecondgroupOk = null;
	private EditText newsecondgroupName = null;
	private int currentGroupId=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newsecondgroup);
		newsecondgroupCancel = (Button) findViewById(R.id.newsecondgroupCancel);
		newsecondgroupOk = (Button) findViewById(R.id.newsecondgroupOk);
		newsecondgroupName = (EditText) findViewById(R.id.newsecondgroupName);
		newsecondgroupCancel.setOnClickListener(new newsecondgroupCancelListener());
		newsecondgroupOk.setOnClickListener(new newsecondgroupOkListener());
		Intent intent=getIntent();
		currentGroupId = intent.getIntExtra("currentGroupId", 0);
		
	}
	class newsecondgroupOkListener implements OnClickListener{
		public void onClick(View v){
			String name = null;
			int secondgroupnumber=0;int secondmaxgroupnumber=0;
			name=newsecondgroupName.getText().toString();
			iMoneyData dbHelper = new iMoneyData(newSecondGroup.this,"iMoneyData");
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.query("expensegroup", new String[]{"secondgroupnumber","secondmaxgroupnumber","id"}, "id=?", new String[]{""+currentGroupId}, null, null, null);
			while (cursor.moveToNext()){
				secondgroupnumber = cursor.getInt(cursor.getColumnIndex("secondgroupnumber"));
				secondmaxgroupnumber = cursor.getInt(cursor.getColumnIndex("secondmaxgroupnumber"));
			}
			ContentValues values = new ContentValues();
			values.put("secondgroupnumber", secondgroupnumber+1);
			values.put("secondmaxgroupnumber", secondmaxgroupnumber+1);
			db.update("expensegroup", values, "id=?", new String[]{""+currentGroupId});
			cursor.close();
			ContentValues values2 = new ContentValues();
			values2.put("name", name);
			values2.put("secondgroupid", secondmaxgroupnumber+1);
			values2.put("groupid", currentGroupId);
			values2.put("inuse", 1);
			db.insert("secondgroup", null, values2);
			db.close();
			finish();
			
		}
	}
	class newsecondgroupCancelListener implements OnClickListener{
		public void onClick(View v){
			finish();
		}
	}

}



