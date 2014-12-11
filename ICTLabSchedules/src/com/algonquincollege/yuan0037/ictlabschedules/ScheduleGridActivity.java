package com.algonquincollege.yuan0037.ictlabschedules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import util.ServiceHandler;
import domain.Lab;
import domain.LabSchedule;
import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import com.algonquincollege.yuan0037.ictlabschedules.R;

public class ScheduleGridActivity extends Activity {
	private Lab currentLab;
	private String currentLabScheduleJSONString;
	private List<LabSchedule> labSchedules;
	private GridLayout labGrid; 
	private GridLayout labHourGrid;
	private static final String[] days = 
		{"sunday", "monday", "tuesday", 
		"wednesday", "thursday", "friday", "saturday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_grid);
		
		labGrid=(GridLayout) this.findViewById(R.id.scheduleGridView);
		labHourGrid=(GridLayout) this.findViewById(R.id.scheduleGridViewForHourTitle);
		labSchedules = new ArrayList<LabSchedule>();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		Log.d(Constants.TAG, "detail activity created");
		currentLab = b.getParcelable("domain.Lab");
		currentLabScheduleJSONString=b.getString("scheduleJSONString");

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(Constants.TAG, "detail activity started");
		//Log.d(Constants.TAG, Constants.URL+currentLab.getRoom().toLowerCase());
		if (currentLabScheduleJSONString.equals(""))
		{
			new FetchLabSchedules().execute(Constants.URL+currentLab.getRoom().toLowerCase());
		}
		else
		{
			parseScheduleJSONString(currentLabScheduleJSONString);
			generateGrid();
		}
	}
	private void parseScheduleJSONString(String jSONString){
		try {
			currentLabScheduleJSONString=jSONString;
			JSONObject jsonObj = new JSONObject( jSONString );

			String tempStr = jsonObj.getString(currentLab.getRoom().toLowerCase());
			// Getting JSON Array node
			//JSONArray jsonLabSchedules = jsonObj.getJSONArray(currentLab.getRoom().toLowerCase());
			//;
			// looping through each schedule, one at a time
			//Log.d( Constants.TAG, "tempStr1= "+tempStr);
			
			JSONObject scheduleForWeekObj = new JSONObject(tempStr);
			for (int i=8; i<=17; i++)
			{
				String singleHourForOneWeekString = scheduleForWeekObj.getString("H"+String.format("%02d", i)+"00");
				JSONObject singleHourForOneWeekObj = new JSONObject(singleHourForOneWeekString);

				//return days[calendar.get(Calendar.DAY_OF_WEEK)-1];

				for (int j=0; j<=6; j++)
				{
					if (!singleHourForOneWeekObj.getString(days[j]).equals("")){
						LabSchedule lSchedule = new LabSchedule();
						lSchedule.setRoom(currentLab.getRoom());
						lSchedule.setLabName(singleHourForOneWeekObj.getString(days[j]));
						lSchedule.setScheduleStartHour(i);
						lSchedule.setScheduleEndHour(i+1);
						lSchedule.setScheduleDayOfWeek(j);
						//Log.d(Constants.TAG, "object added"+lSchedule.getRoom()+lSchedule.getLabName()+days[lSchedule.getScheduleDayOfWeek()]+lSchedule.getScheduleStartHour());
						labSchedules.add(lSchedule);
					}
				}
			}

		} catch ( JSONException e ) {
			//Log.d(Constants.TAG, e.getMessage().toString());
			e.printStackTrace();
		}	
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.schedule_grid, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (item.getItemId() == android.R.id.home) 
		{
			Intent returnIntent = new Intent();
			returnIntent.putExtra("scheduleJSONString",currentLabScheduleJSONString);
			setResult(RESULT_OK,returnIntent);
			finish();
		}	
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class FetchLabSchedules extends AsyncTask<String, Void, List<LabSchedule>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ScheduleGridActivity.this );
			pDialog.setMessage( "Please wait..." );
			pDialog.setCancelable( false );
			pDialog.show();

			labSchedules.clear();
		}

		@Override
		protected List<LabSchedule> doInBackground( String... params ) {
//			List<LabSchedule> schedules = new ArrayList<LabSchedule>();

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall( params[0], ServiceHandler.GET ) ;
//			labScheduleJSONString=jsonStr;
			Log.d( Constants.TAG + " Response: ", "> " + jsonStr );

			if (jsonStr != null) {
				parseScheduleJSONString(jsonStr);
			} else {
				Log.e( Constants.TAG + " ServiceHandler", "Couldn't get any data from the url" );
			}

			return null;
		}

		@Override
		protected void onPostExecute( List<LabSchedule> result ) {
			super.onPostExecute( result );
			// Dismiss the progress dialog
			if ( pDialog.isShowing() )
				pDialog.dismiss();

			//Log.d(Constants.TAG, String.valueOf(labSchedules.size()));
			generateGrid();
		}
	}

	public class CustomComparator implements Comparator<LabSchedule> {
		@Override
		public int compare(LabSchedule o1, LabSchedule o2) {
			return o1.getScheduleStartHour().compareTo(o2.getScheduleStartHour());
		}
	}

	public void generateGrid(){


		labGrid.removeAllViews();
		labHourGrid.removeAllViews();
		
		//set up the grid to have 11 rows and 8 columns;
		//11 rows: column header row, and 10 rows from 8H00 to 1700
		//8 columns: row header column, and 7 columns from sunday to saturday; 
		labGrid.setRowCount(11);
		labGrid.setColumnCount(8);
		labHourGrid.setRowCount(11);
		labHourGrid.setColumnCount(1);

		//generate the first cell of the first column (0, 0);
		TextView tvColumnHourTitle = new TextView(this);
		tvColumnHourTitle.setText("Time");
		tvColumnHourTitle.setHeight(50);
		tvColumnHourTitle.setBackgroundResource(R.drawable.border_style);
		tvColumnHourTitle.setWidth(Constants.COLUMN_WIDTH-10);
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.setGravity(Gravity.CENTER);
		params.rowSpec= GridLayout.spec(0);
		params.columnSpec=GridLayout.spec(0);
		tvColumnHourTitle.setLayoutParams(params);

		//labGrid.addView(tvColumnHourTitle);
		labHourGrid.addView(tvColumnHourTitle);
		//generate the first row's cells from Sunday to Saturday (sun, mon, tue, ...sat);
		//from index 1 to 7
		for (int i=0; i<=6;i++)
		{
			TextView tvColumnTitle = new TextView(this);
			tvColumnTitle.setText(days[i].substring(0, 3));
			tvColumnTitle.setWidth(Constants.COLUMN_WIDTH);
			tvColumnTitle.setHeight(50);
			tvColumnTitle.setBackgroundResource(R.drawable.border_style);
			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.WRAP_CONTENT;
			paramsForTitle.width = LayoutParams.WRAP_CONTENT;
			paramsForTitle.setGravity(Gravity.CENTER);
			paramsForTitle.rowSpec= GridLayout.spec(0);
			paramsForTitle.columnSpec=GridLayout.spec(i+1);
			tvColumnTitle.setLayoutParams(paramsForTitle);
			labGrid.addView(tvColumnTitle);
		}
		
		//generate the first column's cells, from 8H00 to 17:00;
		for (int i=8; i<=17; i++)
		{
			TextView tvHourTitle = new TextView(this);
			tvHourTitle.setText(String.format("%2d", i));
			tvHourTitle.setWidth(Constants.COLUMN_WIDTH-10);
			tvHourTitle.setHeight(Constants.ROW_HEIGHT);
	
			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.MATCH_PARENT;
			paramsForTitle.width = LayoutParams.MATCH_PARENT;

			paramsForTitle.rowSpec= GridLayout.spec(i-8+1);
			paramsForTitle.columnSpec=GridLayout.spec(0);
			paramsForTitle.setGravity(Gravity.CENTER);
			tvHourTitle.setLayoutParams(paramsForTitle);
			//labGrid.addView(tvHourTitle);
			labHourGrid.addView(tvHourTitle);
		}
		
		//generate the first column's cells, from 8H00 to 17:00;
		for (int i=8; i<=17; i++)
		{
			TextView tvHourTitle = new TextView(this);
			tvHourTitle.setText(String.format("%2d", i));
			//please note, have to set up the first column the same as the 
			//other grid, if I remove this part of code, this grid's certain views
			//will collapse to wrong positions; don't know why
			//in order to avoid duplicated "time" column, I set the width to 1; 
			tvHourTitle.setWidth(1);
			tvHourTitle.setHeight(Constants.ROW_HEIGHT);
	
			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.MATCH_PARENT;
			paramsForTitle.width = LayoutParams.MATCH_PARENT;

			paramsForTitle.rowSpec= GridLayout.spec(i-8+1);
			paramsForTitle.columnSpec=GridLayout.spec(0);
			paramsForTitle.setGravity(Gravity.CENTER);
			tvHourTitle.setLayoutParams(paramsForTitle);
			//labGrid.addView(tvHourTitle);
			labGrid.addView(tvHourTitle);
		}
		
		//process labSchedule arrays by each day (mon, tue, ... )
		//and get an array for each day's schedule
		//for each day's array, sort the labSchedule objects by starting hour; 
		//combine continuing labSchedule objects into one; 
		//then get each day's array back to the labSchedules array; 
		List<LabSchedule> newWeekScheduleList=new ArrayList<LabSchedule>();

		for (int k=0; k<=6; k++)
		{

			List<LabSchedule> dailyScheduleList = new ArrayList<LabSchedule>();
			for (LabSchedule lS: labSchedules)
			{
				if (lS.getScheduleDayOfWeek().equals(k))
				{
					dailyScheduleList.add(lS);
				}
			}
			//Log.d(Constants.TAG, days[k]+ " daily count before processing="+String.valueOf(dailyScheduleList.size()));
			Collections.sort(dailyScheduleList, new CustomComparator());
			for (int l=dailyScheduleList.size()-1; l>=1; l--)
			{

				if ((dailyScheduleList.get(l).getLabName().equals(dailyScheduleList.get(l-1).getLabName())) &&(dailyScheduleList.get(l).getScheduleStartHour().equals(dailyScheduleList.get(l-1).getScheduleEndHour())))
				{
					dailyScheduleList.get(l-1).setScheduleEndHour(dailyScheduleList.get(l).getScheduleEndHour());
					dailyScheduleList.remove(l);				
				}
			}
			newWeekScheduleList.addAll(dailyScheduleList);
		}

		labSchedules.clear();
		labSchedules.addAll(newWeekScheduleList);
		
		//highlight the current day's column 
		Calendar localCalendar = Calendar.getInstance(TimeZone.getDefault());
		TextView tvColumnHighlightCurrentDay = new TextView(this);
		tvColumnHighlightCurrentDay.setWidth(Constants.COLUMN_WIDTH);
		tvColumnHighlightCurrentDay.setBackgroundColor(Constants.TODAY_HIGHLIGHT_COLOR);
		tvColumnHighlightCurrentDay.getBackground().setAlpha(128);
		GridLayout.LayoutParams paramsForHighlightCurrentDay = new GridLayout.LayoutParams();
		paramsForHighlightCurrentDay.height = LayoutParams.MATCH_PARENT;
		paramsForHighlightCurrentDay.width = LayoutParams.MATCH_PARENT;
		paramsForHighlightCurrentDay.rowSpec= GridLayout.spec(0, 11);
		paramsForHighlightCurrentDay.columnSpec=GridLayout.spec(localCalendar.get(Calendar.DAY_OF_WEEK));
		paramsForHighlightCurrentDay.setGravity(Gravity.FILL);
		tvColumnHighlightCurrentDay.setLayoutParams(paramsForHighlightCurrentDay);
		tvColumnHighlightCurrentDay.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		labGrid.addView(tvColumnHighlightCurrentDay);
		

		//draw schedule for each labSchedule object;
		for(LabSchedule lS : labSchedules)
		{
			TextView tvSchedule = new TextView(this);
			tvSchedule.setText(lS.getLabName());
			tvSchedule.setWidth(Constants.COLUMN_WIDTH);
			tvSchedule.setHeight(Constants.ROW_HEIGHT*(lS.getScheduleEndHour()-lS.getScheduleStartHour()));

			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.MATCH_PARENT;
			paramsForTitle.width = LayoutParams.MATCH_PARENT;
			
			//Log.d(Constants.TAG, "drawing:"+days[lS.getScheduleDayOfWeek()]+lS.getLabName()+ String.valueOf(lS.getScheduleEndHour()-lS.getScheduleStartHour()));
			if (lS.getLabName().equals("MAD9111"))
			{
				Log.d(Constants.TAG, "day of week = "+days[lS.getScheduleDayOfWeek()]+" start hour = "+String.valueOf(lS.getScheduleStartHour())+"end hour = "+
			String.valueOf(lS.getScheduleEndHour()));
				
				
			}
			
			paramsForTitle.rowSpec= GridLayout.spec(lS.getScheduleStartHour()-8+1,lS.getScheduleEndHour()-lS.getScheduleStartHour());

			paramsForTitle.columnSpec=GridLayout.spec(lS.getScheduleDayOfWeek()+1);
			paramsForTitle.setGravity(Gravity.FILL);
			//|Gravity.CENTER_VERTICAL);
			tvSchedule.setLayoutParams(paramsForTitle);
			tvSchedule.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

			//tvSchedule.setBackgroundResource(R.drawable.border_style_schedule_cell);
			tvSchedule.setBackgroundColor(lS.getScheduleColor());			
			//tvSchedule.getBackground().setAlpha(128);
			
			//highlight the current hour's schedule when possible; 
			if (lS.getScheduleDayOfWeek().equals(localCalendar.get(Calendar.DAY_OF_WEEK)-1))
			{
				//if it is now the actual integer hour, like 9:00;
				//we only highlight the next schedule that starts sharply at 9:00
				//or we will highlight any schedule that cross 9:00, such as 8:00-10:00;
				//in other words, we do not highlight any schedule 
				//ends on 9:00 (current actual interger hour)
				if (localCalendar.get(Calendar.MINUTE)==0)
				//Log.d(Constants.TAG, "current minute = "+ String.valueOf(localCalendar.get(Calendar.MINUTE)));	
				{
					if ((lS.getScheduleStartHour()<=localCalendar.get(Calendar.HOUR_OF_DAY)) && 
							(lS.getScheduleEndHour()>localCalendar.get(Calendar.HOUR_OF_DAY)))
								tvSchedule.setBackgroundResource(R.drawable.border_style);
					
				}
				else
				{
					if ((lS.getScheduleStartHour()<=localCalendar.get(Calendar.HOUR_OF_DAY)) && 
							(lS.getScheduleEndHour()>localCalendar.get(Calendar.HOUR_OF_DAY)))
								tvSchedule.setBackgroundResource(R.drawable.border_style);
				}
					
			}
			labGrid.addView(tvSchedule);
		}
	}	

}
