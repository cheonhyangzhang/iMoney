package cenhiang.imoney;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class main extends Activity {
    /** Called when the activity is first created. */
    private usefulTools uftools = new usefulTools();
	private int bottonWidth= 70	;
	private int itemBetween = 1;
	private int itemNum = 0;
	private int currentTab = 0;//0 represents normalgroup 1 represents favouritegroup 2 represents secondgroup 
	private int currentTabGroupId = 0;
	private int currentSecondGroupId = 0;
	private Button mainNormalGroup = null;
	private LinearLayout mainGroup = null;
	private Button mainFavouriteGroup= null;
	private TextView mainLabelText = null;
	private TextView mainGetAddition = null;
	private EditText mainGetExpense = null;
	private Button  mainRecord = null;
	private LinearLayout additionGetBox = null;
	private LinearLayout mainSee = null;
	private Button mainExpand= null;
	private int additionExpand=0;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mainNormalGroup = (Button) findViewById(R.id.mainNormalGroup);
        mainFavouriteGroup  = (Button) findViewById(R.id.mainFavouriteGroup );
        mainLabelText = (TextView) findViewById(R.id.mainLabelText);
        mainGetExpense = (EditText) findViewById(R.id.mainGetExpense);
        mainGroup=(LinearLayout)findViewById(R.id.mainGroup); 
        mainRecord = (Button) findViewById(R.id.mainRecord);
        mainExpand= (Button) findViewById(R.id.mainExpand);
        mainGetAddition = (TextView) findViewById(R.id.mainGetAddition);
        
        mainSee = (LinearLayout) findViewById(R.id.mainSee);
        additionGetBox = (LinearLayout) findViewById(R.id.additionGetBox);
        mainExpand.setOnClickListener(new mainExpandListener());
        mainGroup.setOnClickListener(new mainGroupBackListener());
        mainSee.setOnClickListener(new mainSeeListener()); 
        mainNormalGroup.setOnClickListener(new mainNormalGroupListener()); 
        mainFavouriteGroup.setOnClickListener(new mainFavouriteGroupListener()); 
        mainRecord.setOnClickListener(new  mainRecordListener());
        
        
        //读取程序设定部分
        iMoneyData dbHelper = new iMoneyData(main.this, "iMoneyData");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = db.query("settings", new String[] { "additionexpand", "id" },"id=?", new String[] { "0" }, null, null, null);
		while (cursor.moveToNext()) {
			additionExpand = cursor.getInt(cursor.getColumnIndex("additionexpand"));
		}

		cursor.close();
		db.close();
    }
    
    class mainExpandListener implements OnClickListener{
    	public void onClick(View v){
    		if (additionExpand == 1){
    			additionExpand=0;
    			main.this.onResume();
    			System.out.println("pull up");
    		}
    		else{
    			additionExpand=1;
    			main.this.onResume();
    			System.out.println("pull down");
    		}
    	}
    }
    
    class mainSeeListener implements OnClickListener{
    	public void onClick(View v){
    		Time tempTime = new Time();
    		tempTime.setToNow();
    		Intent intent = new Intent();
    		intent.putExtra("year", tempTime.year);
    		intent.putExtra("month", tempTime.month);
    		intent.putExtra("day", tempTime.monthDay);
    		System.out.println(tempTime.year+":"+tempTime.month+":"+tempTime.monthDay);
    		intent.putExtra("type", 1);
    		intent.setClass(main.this, showMoney.class);
    		main.this.startActivity(intent);
    	}
    	
    }
	class mainGroupBackListener implements OnClickListener{
		public void onClick(View v){
			Intent intent = new Intent();
			intent.setClass(main.this, groupManage.class);
			main.this.startActivity(intent);
		}
	}
    class mainRecordListener implements OnClickListener{
    	public void onClick(View v){
    		int getMoney=0;
    		String edittext = null;
    		String addition = "";
    		edittext=mainGetExpense.getText().toString();
    		if (edittext.equals("")==true){
    			Toast.makeText(main.this, R.string.noNumber,Toast.LENGTH_SHORT).show();
    		}
    		else if (currentTabGroupId==0 && currentSecondGroupId==0){
    			Toast.makeText(main.this, R.string.noExpense,Toast.LENGTH_SHORT).show();
    			
    		}
    		else{
    			Toast.makeText(main.this, R.string.hasBeenRecorded,Toast.LENGTH_SHORT).show();
    			uftools.insertExpenseDetails(main.this, currentTabGroupId, currentSecondGroupId, Double.parseDouble(edittext),  mainGetAddition.getText().toString());
    			mainGetExpense.setText("");
    			mainGetAddition.setText("");
    			mainLabelText.setText(R.string.expense); 
    			currentTabGroupId=0;currentSecondGroupId=0;
    			main.this.onResume();
    		}
    	}
    }

	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		middleBoxGenerate middlebox = new middleBoxGenerate();
		
		
		
		if (additionExpand == 1){
			additionGetBox.setVisibility(View.VISIBLE);
			mainExpand.setBackgroundResource(R.drawable.pullup);
		}
		else{
			mainExpand.setBackgroundResource(R.drawable.pulldown);
			additionGetBox.setVisibility(View.GONE);
		}
		if (currentTab == 0){
		    middlebox.normalGroupGenerate();
		    mainNormalGroup.setBackgroundResource(R.drawable.switchrightdown);
		    mainFavouriteGroup.setBackgroundResource(R.drawable.switchleft);
		}
		else if (currentTab ==1){
			mainFavouriteGroup.setBackgroundResource(R.drawable.switchleftdown);
			mainNormalGroup.setBackgroundResource(R.drawable.switchright);
			
		}
		else if (currentTab ==2){
			mainNormalGroup.setBackgroundResource(R.drawable.switchright);
			mainFavouriteGroup.setBackgroundResource(R.drawable.switchleft);
			middlebox.secondGroupGenerate();
		}
	
	}
	class mainNormalGroupListener implements OnClickListener{
		public void onClick(View v){
			currentTab = 0;
			currentTabGroupId =0;
			currentSecondGroupId = 0;
			mainLabelText.setText(R.string.expense);
			main.this.onResume();
		}
	}
	class mainFavouriteGroupListener implements OnClickListener{
		public void onClick(View v){
			currentTabGroupId =0;
			currentSecondGroupId = 0;
			currentTab = 1;
			mainLabelText.setText(R.string.expense);
			main.this.onResume();
		}
	}
    class middleBoxGenerate{
    	public void buttonGenerate(String buttonText[]){
    		RelativeLayout expenseGroupBox = (RelativeLayout) findViewById(R.id.expenseGroupBox);
            RelativeLayout.LayoutParams relativeLayoutParams = null;
            int rowCount = 4; // 行总数
            int colCount = 4; // 列总数
            int itemIndex = 0;
            Button itemButton = null;
            int itemId = 0;//id may have a problem
            expenseGroupBox.removeAllViews();
    		for (int i = 0; i < rowCount; i++) { // 把握行
            	
            	if (itemIndex<itemNum){
                itemButton = new Button(main.this);
                itemButton.setId(itemId+=10);
                
                relativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                
                if (0 == i) { //第一行第一列
                relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                relativeLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                relativeLayoutParams.setMargins(10, 10, 0, 0);
               
                
                } else {
                relativeLayoutParams.addRule(RelativeLayout.ALIGN_LEFT,itemId - 10);
                relativeLayoutParams.addRule(RelativeLayout.BELOW,itemId - 10);
                relativeLayoutParams.setMargins(0, itemBetween, 0, 0);
               
                
                }
                //buttonText = cursor2.getString(cursor2.getColumnIndex("name"));
                //cursor2.moveToNext();
                itemButton.setText(buttonText[itemIndex]);
                
                itemButton.setWidth(bottonWidth);
                itemButton.setLayoutParams(relativeLayoutParams);
                itemButton.setOnClickListener(new itemButtonListener());
                itemButton.setTextSize(12);
                expenseGroupBox.addView(itemButton);
                itemIndex++;
                if(itemIndex==itemNum){
                	break;
                }
                }
                // ******************
                for (int j = 1; j < colCount; j++) { // 把握列
                if (itemIndex<itemNum){	
                	itemButton = new Button(main.this);
                	itemButton.setId(itemId + j);
                	
                	//buttonText = cursor2.getString(cursor2.getColumnIndex("name"));
                    //cursor2.moveToNext();
                	itemButton.setText(buttonText[itemIndex]);
                	relativeLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
                	relativeLayoutParams.addRule(RelativeLayout.RIGHT_OF, itemId+ j - 1);
                	relativeLayoutParams.addRule(RelativeLayout.ALIGN_TOP, itemId+ j - 1);
                	//relativeLayoutParams.setMargins(10, 10, 10, 10);
                	relativeLayoutParams.setMargins(itemBetween, 0, 0, 0);
                	itemButton.setWidth(bottonWidth);
                	itemButton.setLayoutParams(relativeLayoutParams);
                	itemButton.setOnClickListener(new itemButtonListener());
                	itemButton.setTextSize(12);
                	expenseGroupBox.addView(itemButton);
                	itemIndex++;
                	if(itemIndex==itemNum){
                		break;
                	}
                }
                }
            }//for i
    	}
    	public void secondGroupGenerate(){
    		System.out.println("secondGroupGenerate");
            String buttonText []= new String[17];
        	iMoneyData dbHelper = new iMoneyData(main.this, "iMoneyData");
    		SQLiteDatabase db = dbHelper.getWritableDatabase();
    		Cursor cursor = db.query("expensegroup", new String[] { "secondgroupnumber", "id" },"id=?", new String[] { currentTabGroupId+"" }, null, null, null);
    		while (cursor.moveToNext()) {
    			itemNum = cursor.getInt(cursor.getColumnIndex("secondgroupnumber"));
    		}
    		cursor.close();
    		
    		Cursor cursor2 = db.query("secondgroup", new String[] { "groupid","inuse","name"},"groupid=? and inuse=?", new String[] { currentTabGroupId+"","1" }, null, null, null);		
    		int i=0;
    		while (cursor2.moveToNext()) {
    			buttonText[i] = cursor2.getString(cursor2.getColumnIndex("name"));
    			i++;
    		}
            buttonGenerate(buttonText);
            db.close();
            cursor2.close();
    	}
    	public void normalGroupGenerate(){
    		System.out.println("noarmalGroupGenerate");
            String buttonText []= new String[17];
        	iMoneyData dbHelper = new iMoneyData(main.this, "iMoneyData");
    		SQLiteDatabase db = dbHelper.getWritableDatabase();
    		Cursor cursor = db.query("settings", new String[] { "groupnumber", "id" },"id=?", new String[] { "0" }, null, null, null);
    		while (cursor.moveToNext()) {
    			itemNum = cursor.getInt(cursor.getColumnIndex("groupnumber"));
    		}
    		cursor.close();
    		Cursor cursor2 = db.query("expensegroup", new String[] { "id","inuse","name"},"inuse=?", new String[] { "1" }, null, null, null);		
    		int i=0;
    		while (cursor2.moveToNext()) {
    			buttonText[i] = cursor2.getString(cursor2.getColumnIndex("name"));
    			i++;
    		}
    		cursor2.close();
    		db.close();
            buttonGenerate(buttonText);
    	}
    }
	class itemButtonListener implements OnClickListener{
		public void onClick(View v){
			Button thisbutton = (Button) v;
			String buttonText;
			buttonText = (String) thisbutton.getText();
			Toast.makeText(main.this, buttonText, Toast.LENGTH_SHORT).show();
			//normal press button
			if (currentTab == 0){
				
				currentTab = 2;
				iMoneyData dbHelper = new iMoneyData(main.this, "iMoneyData");
	    		SQLiteDatabase db = dbHelper.getWritableDatabase();
	    		int secondgroupnumber = 0;
	    		Cursor cursor = db.query("expensegroup", new String[] { "name", "secondgroupnumber","id" },"name=?", new String[] { buttonText }, null, null, null);
	    		while (cursor.moveToNext()) {
	    			currentTabGroupId = cursor.getInt(cursor.getColumnIndex("id"));
	    			secondgroupnumber = cursor.getInt(cursor.getColumnIndex("secondgroupnumber"));
	    		}
	    		cursor.close();
				db.close();
				//直接生成
				if (secondgroupnumber == 0){
					mainLabelText.setText(buttonText);
					currentSecondGroupId = 0;
					currentTab = 0;
				}
				main.this.onResume();
				
			}
			else if (currentTab ==1){
				
			}
			else if (currentTab ==2){
				iMoneyData dbHelper = new iMoneyData(main.this, "iMoneyData");
	    		SQLiteDatabase db = dbHelper.getWritableDatabase();
	    		Cursor cursor = db.query("secondgroup", new String[] { "name", "secondgroupid"},"name=?", new String[] { buttonText }, null, null, null);
	    		while (cursor.moveToNext()) {
	    			currentSecondGroupId = cursor.getInt(cursor.getColumnIndex("secondgroupid"));
	    		}
	    		cursor.close();
				db.close();
				mainLabelText.setText(buttonText);
				currentTab = 0;
				main.this.onResume();
				
			}
		
		}
	}
    
}