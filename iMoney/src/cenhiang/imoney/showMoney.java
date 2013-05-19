package cenhiang.imoney;

import java.util.ArrayList;
import java.util.HashMap;


import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.format.Time;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class showMoney extends ListActivity implements OnTouchListener, OnGestureListener{
	//	intent should contain the following things
	//  int year  int month int day
	//  int type    here 1 means orderbyday 2 means orderbyweek 3 means orderbymonth 4 means detailsDisplay
	private int maxDayExpense = 17;
	private int maxDetailExpense = 50;
	private int currentTab = 1;//here 1 means orderbyday 2 means orderbyweek 3 means orderbymonth 4 means detailsDisplay
	private int isDetail = 0;//0 means group while 1 means in detail show the expense
	private int detailGroupId = 0;
	private String detailName = null;
	private int year = 0;
	private int month = 0;
	private int day = 0;
	private Button showmoneyOrderByDay = null;
	private Button showmoneyOrderByWeek= null;
	private Button showmoneyOrderByMonth= null;
	private LinearLayout detailGroupNameBox = null;
	private TextView showmoneyDate = null;
	private usefulTools uftools = new usefulTools();
	private TextView detailGroupName = null; 
	private Button showmoneyBack = null;
	private ListView listview =null;
	GestureDetector mGestureDetector; //定义手势监听器
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmoney);
		showmoneyDate = (TextView) findViewById(R.id.showmoneyDate);
        showmoneyBack=(Button)findViewById(R.id.showmoneyBack); 
        showmoneyOrderByDay =(Button)findViewById(R.id.showmoneyOrderByDay ); 
        showmoneyOrderByWeek=(Button)findViewById(R.id.showmoneyOrderByWeek); 
        showmoneyOrderByMonth=(Button)findViewById(R.id.showmoneyOrderByMonth); 
        listview = (ListView) findViewById(android.R.id.list);
        
        detailGroupName =(TextView) findViewById(R.id.detailGroupName);
        detailGroupNameBox = (LinearLayout)findViewById(R.id.detailGroupnameBox);
        showmoneyBack.setOnClickListener(new showmoneyBackListener());
        showmoneyOrderByDay.setOnClickListener(new showmoneyOrderByDayListener());
        showmoneyOrderByWeek.setOnClickListener(new showmoneyOrderByWeekListener());
        showmoneyOrderByMonth.setOnClickListener(new  showmoneyOrderByMonthListener());
        mGestureDetector = new GestureDetector((OnGestureListener) this);
		
        ListView listViewFling = (ListView)findViewById(android.R.id.list);  
        listViewFling.setOnTouchListener(this);  
        listViewFling.setLongClickable(true);       
        
        
    	Intent intent = getIntent();
    	currentTab = intent.getIntExtra("type", 1);
		year = intent.getIntExtra("year", 0);	
		month = intent.getIntExtra("month", 0);	
		day = intent.getIntExtra("day", 0);	
	}
	class showmoneyOrderByDayListener implements OnClickListener{
		public void onClick(View v){
			currentTab = 1;
			isDetail=0;
			showMoney.this.onResume();
		}
	}
	class showmoneyOrderByWeekListener implements OnClickListener{
		public void onClick(View v){
			currentTab = 2;
			isDetail=0;
			showMoney.this.onResume();
		}
	}
	class showmoneyOrderByMonthListener implements OnClickListener{
		public void onClick(View v){
			currentTab = 3;
			isDetail=0;
			showMoney.this.onResume();
		}
	}
	

	class showmoneyBackListener implements OnClickListener{
		public void onClick(View v){
			if (isDetail ==1){
				isDetail =0;
				showMoney.this.onResume();
			}
			else{
			finish();
			}
		}
	}

    protected void onListItemClick(ListView l, View v, int position, long id){
    	String tempuse=null,itemName=null;
    	tempuse = l.getItemAtPosition(position).toString();
    	int startindex=0,endindex=0;
    	startindex=tempuse.lastIndexOf("=")+1;
    	endindex=tempuse.length()-1;
    	itemName=tempuse.substring(startindex, endindex);
    	super.onListItemClick(l, v, position, id);
    	if (isDetail ==0){
    			iMoneyData dbHelper = new iMoneyData(showMoney.this,"iMoneyData");
    			SQLiteDatabase db = dbHelper.getWritableDatabase();
    			Cursor cursor = db.query("expensegroup", new String[]{"name","id"}, "name=?",new String[]{itemName}, null, null, null);
    			detailName=itemName;
    			while (cursor.moveToNext()){
    					detailGroupId=cursor.getInt(cursor.getColumnIndex("id"));
    			}
    			cursor.close();
    			isDetail = 1;
    			showMoney.this.onResume();
	   			db.close();
    		
    	}// end if isDetail == 0 
    	
    }
	
	@Override
	protected void onResume() {
		System.out.println(" test number"+Math.round(3*3.2*10)/10.0);
		super.onResume();
		System.out.println("currentTab is "+currentTab);
	
		Time tempTime = new Time();
		tempTime.setToNow();
		showmoneyOrderByDay.setBackgroundResource(R.drawable.switchmiddleleft);
		showmoneyOrderByWeek.setBackgroundResource(R.drawable.switchmiddle);
		showmoneyOrderByMonth.setBackgroundResource(R.drawable.switchmiddleright);
		
		if (currentTab == 1){
			System.out.println("left whitee");
			showmoneyOrderByDay.setBackgroundResource(R.drawable.switchmiddleleftwhite);
			showmoneyDate.setText(uftools.dateGenerate(year, month+1, day));
			if (tempTime.year==year&&tempTime.month==month&&tempTime.monthDay==day){
				Toast.makeText(showMoney.this, R.string.today, Toast.LENGTH_SHORT).show();
			
			}
		}
		else if (currentTab == 2){
			showmoneyOrderByWeek.setBackgroundResource(R.drawable.switchmiddlewhite);

			System.out.println("middle white");
		
		}
		else if (currentTab == 3){
			showmoneyOrderByMonth.setBackgroundResource(R.drawable.switchmiddlerightwhite);
			System.out.println("right white");
			showmoneyDate.setText(uftools.dateGenerate(year, month+1));
			if (tempTime.year==year&&tempTime.month==month){
				Toast.makeText(showMoney.this, R.string.thisMonth, Toast.LENGTH_SHORT).show();
			
			}
		}
		
		
		//details
		if (isDetail == 1){
			detailGroupNameBox.setVisibility(View.VISIBLE);
			detailGroupName.setText(detailName);
			System.out.println("currentTab=1");
			ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();       
		    HashMap<String, String> [] maps = new HashMap[maxDetailExpense];//
		    
		    iMoneyData dbHelper = new iMoneyData(showMoney.this,"iMoneyData");
			SQLiteDatabase db = dbHelper.getWritableDatabase();
			Cursor cursor = null;
			if (currentTab == 1){ 
				cursor = db.query("expensedetail"+year+month, new String[]{"day","groupid","secondgroupid","addition","expense"}, "day=? and groupid=?", new String[]{""+day,""+""+detailGroupId}, null, null, null);
				
			}
			//byweek
			else if (currentTab == 2){
		
			}
			//byday
			else if (currentTab == 3){
				cursor = db.query("expensedetail"+year+month, new String[]{"day","groupid","secondgroupid","addition","expense"}, "groupid=?", new String[]{""+detailGroupId}, null, null, null);
			}
			int mapsIndex=0;
			while (cursor.moveToNext()){
				    System.out.println("found one!");
					System.out.println(cursor.getInt(cursor.getColumnIndex("groupid")));
					System.out.println(cursor.getInt(cursor.getColumnIndex("secondgroupid")));
					String secondgroupname = null;
					maps[mapsIndex] = new HashMap<String, String>();
					if (currentTab!=1){
						maps[mapsIndex].put("dayWD",uftools.dateGenerate(cursor.getInt(cursor.getColumnIndex("day"))));
					}
					maps[mapsIndex].put("additionD", cursor.getString(cursor.getColumnIndex("addition")));
					Cursor cursor2 = db.query("secondgroup", new String[]{"name","groupid","inuse","secondgroupid"}, "groupid=? and secondgroupid=?", new String[]{""+cursor.getInt(cursor.getColumnIndex("groupid")),""+cursor.getInt(cursor.getColumnIndex("secondgroupid"))}, null, null, null);
					while (cursor2.moveToNext()){
						secondgroupname = cursor2.getString(cursor2.getColumnIndex("name"));
					}
					cursor2.close();
					
					maps[mapsIndex].put("expenseNameD", secondgroupname);
					
					maps[mapsIndex].put("totalmoneyD", ""+cursor.getDouble(cursor.getColumnIndex("expense")));
					list.add(maps[mapsIndex]);
					mapsIndex = mapsIndex + 1;
			    }
				cursor.close();
				SimpleAdapter listAdapter =null;
				if (currentTab ==1){
					listAdapter = new SimpleAdapter(this, list, R.layout.showmoneylistviewd, new String[]{"additionD","expenseNameD","totalmoneyD"}, new int[]{R.id.additionD,R.id.expenseNameD,R.id.totalmoneyD});
				}
				else{
					listAdapter = new SimpleAdapter(this, list, R.layout.showmoneylistviewd, new String[]{"additionD","expenseNameD","totalmoneyD","dayWD"}, new int[]{R.id.additionD,R.id.expenseNameD,R.id.totalmoneyD,R.id.dayWD});
					
				}
				setListAdapter(listAdapter);	
				db.close();
		
			 
			
			
			
		}//iddetail == 1
		else{ 
			
			detailGroupNameBox.setVisibility(View.GONE);
			//orderbyday
			//additionBox.setVisibility(View.GONE);
			
				ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();       
		    	HashMap<String, String> [] maps = new HashMap[maxDayExpense];//
		    	
		    	
		        iMoneyData dbHelper = new iMoneyData(showMoney.this,"iMoneyData");
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				Cursor cursor = db.query("expensegroup", new String[]{"name","id","inuse"}, "inuse=?", new String[]{"1"}, null, null, null);
			
				double daymoney =0;
				int daynumber = 0;
				int mapsIndex = 0;
				daymoney=0;daynumber=0;
				while (cursor.moveToNext()){
					int groupnumber;
					double totalmoney;
					groupnumber=0;totalmoney=0;
					maps[mapsIndex] = new HashMap<String, String>();
					maps[mapsIndex].put("expenseName", cursor.getString(cursor.getColumnIndex("name")));
					
					try{
						Cursor cursor2=null;
						if (currentTab == 1){ 	
							cursor2 = db.query("expensedetail"+year+month, new String[]{"day","groupid","expense"}, "day=? and groupid=?", new String[]{""+day,""+cursor.getInt(cursor.getColumnIndex("id"))}, null, null, null);
							
						}
						//byweek
						else if (currentTab == 2){
						}
						//bymonth
						else if (currentTab == 3){
							cursor2 = db.query("expensedetail"+year+month, new String[]{"day","groupid","expense"}, "groupid=?", new String[]{""+cursor.getInt(cursor.getColumnIndex("id"))}, null, null, null);
							
						}	
						
						
					
						while (cursor2.moveToNext()){
							groupnumber++;
							daynumber++;
							totalmoney=totalmoney+cursor2.getDouble(cursor2.getColumnIndex("expense"));
							daymoney=daymoney+cursor2.getDouble(cursor2.getColumnIndex("expense"));
						}
					cursor2.close();
					}
					catch(Exception e){
						groupnumber=0;totalmoney=0;
					}
					maps[mapsIndex].put("groupnumber", ""+groupnumber);
					maps[mapsIndex].put("totalmoney", ""+(Math.round(totalmoney*10)/10.0));
					if (totalmoney!=0){
						list.add(maps[mapsIndex]);
						mapsIndex = mapsIndex + 1;
					}
			    }
				maps[mapsIndex] = new HashMap<String, String>();
				maps[mapsIndex].put("groupnumber", ""+daynumber);
				
				maps[mapsIndex].put("totalmoney", ""+(Math.round(daymoney*10)/10.0));
				maps[mapsIndex].put("expenseName", this.getString(R.string.stat));
				list.add(maps[mapsIndex]);
				cursor.close();
				SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.showmoneylistview, new String[]{"expenseName","groupnumber","totalmoney"}, new int[]{R.id.expenseName,R.id.groupnumber,R.id.totalmoney});
		    	
				setListAdapter(listAdapter);	
				db.close();
				
				
			
			 
			
			
		}//else 
        
       
	}//onResume




	private int verticalMinDistance = 30;
    private int minVelocity         = 5;

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    	if (isDetail ==0){
        if (e1.getX() - e2.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	
        	Time originTime = new Time();
        	if (currentTab == 1){
        		originTime.set(day+1, month, year);
        	}
        	else if (currentTab == 2){
        		originTime.set(day+7, month, year);
        	}
        	else if (currentTab == 3){
        		originTime.set(day, month+1, year);
        	}
        	originTime.normalize(false);
        	
        	iMoneyData dbHelper = new iMoneyData(showMoney.this,"iMoneyData");
        	SQLiteDatabase db = dbHelper.getWritableDatabase();

			int monthExist = 1;
			try{
				db.query("expensedetail"+originTime.year+originTime.month, new String[]{"groupid"}, null, null, null, null, null);
			}
			catch(Exception e){
				monthExist = 0;	
			}
			if (monthExist ==1){
				finish();
				Intent intent = new Intent();
				intent.putExtra("year", originTime.year);
				intent.putExtra("month", originTime.month);
				intent.putExtra("day", originTime.monthDay);
				intent.putExtra("type",currentTab);
				intent.setClass(showMoney.this, showMoney.class);
				finish();
				showMoney.this.startActivity(intent);        	
        		}
			else{
					Toast.makeText(this, R.string.noNextMonth, Toast.LENGTH_SHORT).show();
        	}
			db.close();
			
        }
        	
            //Toast.makeText(this, "向左手势", Toast.LENGTH_SHORT).show();
        else if (e2.getX() - e1.getX() > verticalMinDistance && Math.abs(velocityX) > minVelocity) {
        	
        	Time originTime = new Time();
        	if (currentTab == 1){
        		originTime.set(day-1, month, year);
        	}
        	else if (currentTab == 2){
        		originTime.set(day-7, month, year);
        	}
        	else if (currentTab == 3){
        		originTime.set(day, month-1, year);
        	}
        	originTime.normalize(false);
        	
        	iMoneyData dbHelper = new iMoneyData(showMoney.this,"iMoneyData");
        	SQLiteDatabase db = dbHelper.getWritableDatabase();

			int monthExist = 1;
			try{
				db.query("expensedetail"+originTime.year+originTime.month, new String[]{"groupid"}, null, null, null, null, null);
			}
			catch(Exception e){
				monthExist = 0;	
			}
			if (monthExist ==1){
				finish();
				Intent intent = new Intent();
				intent.putExtra("year", originTime.year);
				intent.putExtra("month", originTime.month);
				intent.putExtra("day", originTime.monthDay);
				intent.putExtra("type",currentTab);
				intent.setClass(showMoney.this, showMoney.class);
				finish();
				showMoney.this.startActivity(intent);        	
        		}
			else{
					Toast.makeText(this, R.string.noBeforeMonth, Toast.LENGTH_SHORT).show();
        	}
			db.close();
			
			//Toast.makeText(this, "向右手势", Toast.LENGTH_SHORT).show();
        }
        
    	}

       
        return false;
    }
    
	public boolean onTouch(View v, MotionEvent event) {
		return mGestureDetector.onTouchEvent(event);
	}

	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}
}