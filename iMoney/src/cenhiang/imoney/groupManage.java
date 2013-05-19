package cenhiang.imoney;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class groupManage extends ListActivity{
	private Button groupManageBack= null;
	private Button groupManageAdd= null;
	private LinearLayout groupmanageNameBox= null;
	private int maxGroupNumber= 17;
	private String currentTabName = null;
	private int currentGroupId = 0;
	private int currentsecondGroupId = 0;
	private TextView groupmanageName = null;
	private int isDetail=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.groupmanage);
        groupManageBack=(Button)findViewById(R.id.groupManageBack); 
        groupManageAdd=(Button)findViewById(R.id.groupmanageAdd); 
        groupmanageName = (TextView)findViewById(R.id.groupmanageName);
        groupmanageNameBox = (LinearLayout) findViewById(R.id.groupmanageNameBox);
        groupManageAdd.setOnClickListener(new groupManageAddListener());
        groupManageBack.setOnClickListener(new groupManageBackListener());
		
	}
	
	
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("detail    "+isDetail);
		if (isDetail ==0){
			groupmanageNameBox.setVisibility(View.GONE);
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();       
			HashMap<String, String> [] maps = new HashMap[maxGroupNumber];//    	
        	iMoneyData dbHelper = new iMoneyData(groupManage.this,"iMoneyData");
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = db.query("expensegroup", new String[]{"name","id","inuse"}, "inuse=?", new String[]{"1"}, null, null, null);
			int mapsIndex=0;
			while (cursor.moveToNext()){
				maps[mapsIndex] = new HashMap<String, String>();
				maps[mapsIndex].put("groupName", cursor.getString(cursor.getColumnIndex("name")));
				list.add(maps[mapsIndex]);
				mapsIndex = mapsIndex + 1;
	    	}
			cursor.close();
			SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.groupmanagelv, new String[]{"groupName"}, new int[]{R.id.groupName});
    	
			setListAdapter(listAdapter);	
			db.close();
		}
		//isDetail ==1 
		else{
			 groupmanageNameBox.setVisibility(View.VISIBLE);
			 groupmanageName.setText(currentTabName);
             ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();       
			 HashMap<String, String> [] maps = new HashMap[maxGroupNumber];//
			 
			 iMoneyData dbHelper = new iMoneyData(groupManage.this,"iMoneyData");
			 SQLiteDatabase db = dbHelper.getWritableDatabase();
			 Cursor cursor = db.query("secondgroup", new String[]{"groupid","name","inuse"}, "groupid=? and inuse=?", new String[]{""+currentGroupId,""+"1"}, null, null, null);
			 int mapsIndex=0;
			 while (cursor.moveToNext()){
					maps[mapsIndex] = new HashMap<String, String>();
					maps[mapsIndex].put("groupName", cursor.getString(cursor.getColumnIndex("name")));
					list.add(maps[mapsIndex]);
					mapsIndex = mapsIndex + 1;
					}
			 cursor.close();
			 SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.groupmanagelv, new String[]{"groupName"}, new int[]{R.id.groupName});
				    	
			 setListAdapter(listAdapter);	
			 db.close();
					
			
		}
		
	}
	 
	 protected void onListItemClick(ListView l, View v, int position, long id){
	    	super.onListItemClick(l, v, position, id);
	    	if (isDetail == 0 ){
	    		isDetail = 1;
	    		iMoneyData dbHelper = new iMoneyData(groupManage.this,"iMoneyData");
    			SQLiteDatabase db = dbHelper.getWritableDatabase();
    			Cursor cursor = db.query("expensegroup", new String[]{"name","id","inuse"}, "inuse=?",new String[]{"1"}, null, null, null);
    			int index =0;
    			int found =0;
    			while (cursor.moveToNext()){
    				if (index == position){
    					currentGroupId=cursor.getInt(cursor.getColumnIndex("id"));
    					currentTabName=cursor.getString(cursor.getColumnIndex("name"));
    					found=1;
    					break;
    				}
    				else{
    					index++;
    				}
    			}
    			cursor.close();
    			db.close();
	    		groupManage.this.onResume();
	    	}
	    	else if (isDetail == 1)
	    	{
	    		//nothing
	    	}
	 }
	
	class groupManageAddListener implements OnClickListener{
		public void onClick(View v){
			// 判断是否大于16个?
			if (isDetail ==0){
				Intent intent = new Intent();
				intent.setClass(groupManage.this, newGroup.class);
				groupManage.this.startActivity(intent);
			}
			//is Detail ==1
			else{
				Intent intent = new Intent();
				intent.putExtra("currentGroupId", currentGroupId);
				intent.setClass(groupManage.this, newSecondGroup.class);
				groupManage.this.startActivity(intent);
			}
		}
	}
	
	class groupManageBackListener implements OnClickListener{
		public void onClick(View v){
			if (isDetail ==1){
				isDetail =0;
				groupManage.this.onResume();
			}
			else{
			finish();
			}
		}
	}

}

