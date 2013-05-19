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

public class newGroup extends Activity{
	private Button newgroupCancel = null;
	private Button newgroupOk = null;
	private EditText newgroupName = null;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newgroup);
		newgroupCancel = (Button) findViewById(R.id.newgroupCancel);
		newgroupOk = (Button) findViewById(R.id.newgroupOk);
		newgroupName = (EditText) findViewById(R.id.newgroupName);
		newgroupCancel.setOnClickListener(new newGroupCancelListener());
		newgroupOk.setOnClickListener(new newGroupOkListener());
		
	}
	class newGroupOkListener implements OnClickListener{
		public void onClick(View v){
			String name = null;
			name=newgroupName.getText().toString();
			int groupnumber=0;int maxgroupnumber=0;
			iMoneyData dbHelper = new iMoneyData(newGroup.this,"iMoneyData");
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.query("settings", new String[]{"groupnumber","maxgroupnumber","id"}, "id=?", new String[]{"0"}, null, null, null);
			while (cursor.moveToNext()){
				groupnumber = cursor.getInt(cursor.getColumnIndex("groupnumber"));
				maxgroupnumber = cursor.getInt(cursor.getColumnIndex("maxgroupnumber"));
			}
			ContentValues values = new ContentValues();
			values.put("groupnumber", groupnumber+1);
			values.put("maxgroupnumber", maxgroupnumber+1);
			db.update("settings", values, "id=?", new String[]{"0"});
			cursor.close();
			ContentValues values2 = new ContentValues();
			values2.put("name", name);
			values2.put("id", maxgroupnumber+1);
			values2.put("secondgroupnumber", 0);
			values2.put("secondmaxgroupnumber", 0);
			values2.put("inuse", 1);
			db.insert("expensegroup", null, values2);
			db.close();
			finish();
			
		}
	}
	class newGroupCancelListener implements OnClickListener{
		public void onClick(View v){
			finish();
		}
	}

}



