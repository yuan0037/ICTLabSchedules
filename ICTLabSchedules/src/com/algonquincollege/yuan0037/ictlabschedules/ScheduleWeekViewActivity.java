package com.algonquincollege.yuan0037.ictlabschedules;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import util.ServiceHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.algonquincollege.yuan0037.ictlabschedules.R;
import com.alamkanak.weekview.WeekView;
import com.alamkanak.weekview.WeekView.EventClickListener;
import com.alamkanak.weekview.WeekView.EventLongPressListener;
import com.alamkanak.weekview.WeekView.MonthChangeListener;
import com.alamkanak.weekview.WeekViewEvent;

import domain.Lab;
import domain.LabSchedule;


public class ScheduleWeekViewActivity extends Activity implements EventClickListener, MonthChangeListener, EventLongPressListener  {

	private static final int TYPE_DAY_VIEW = 1;
	private static final int TYPE_THREE_DAY_VIEW = 2;
	private static final int TYPE_WEEK_VIEW = 3;
	private int mWeekViewType = TYPE_THREE_DAY_VIEW;
	private WeekView mWeekView;

	private Lab currentLab;
	private String currentLabScheduleJSONString;
	private List<LabSchedule> labSchedules;
	private static final String[] days = 
		{"sunday", "monday", "tuesday", 
		"wednesday", "thursday", "friday", "saturday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_week_view);


		
		labSchedules = new ArrayList<LabSchedule>();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		//Log.d(Constants.TAG, "detail activity created");
		currentLab = b.getParcelable("domain.Lab");
		currentLabScheduleJSONString=b.getString("scheduleJSONString");


		//Get a reference for the week view in the layout.
		mWeekView = (WeekView) findViewById(R.id.weekView);
		
		// Show a toast message about the touched event.
		mWeekView.setOnEventClickListener(this);

		// The week view has infinite scrolling horizontally. We have to provide the events of a
		// month every time the month changes on the week view.
		mWeekView.setMonthChangeListener(this);

		// Set long press listener for events.
		mWeekView.setEventLongPressListener(this);

		Log.d(Constants.TAG, "weekview activity created");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		//Log.d(Constants.TAG, "detail activity started");
		//Log.d(Constants.TAG, Constants.URL+currentLab.getRoom().toLowerCase());
		if (currentLabScheduleJSONString.equals(""))
		{
			new FetchLabSchedules().execute(Constants.URL+currentLab.getRoom().toLowerCase());
		}
		else
		{
			parseScheduleJSONString(currentLabScheduleJSONString);
			generateEventPattern();
		}
	}
	
	private void parseScheduleJSONString(String jSONString){
		try {
			currentLabScheduleJSONString=jSONString;
			JSONObject jsonObj = new JSONObject( jSONString );
			String tempStr = jsonObj.getString(currentLab.getRoom().toLowerCase());
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
						lSchedule.setScheduleDayOfWeek(j+1);
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
		getMenuInflater().inflate(R.menu.schedule_week_view, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){
        case R.id.action_today:
        	
            mWeekView.goToToday();
            return true;
        case R.id.action_day_view:
            if (mWeekViewType != TYPE_DAY_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(1);
                mWeekView.setDayNameLength(com.alamkanak.weekview.WeekView.LENGTH_LONG);

                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            }
            return true;
        case R.id.action_three_day_view:
            if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_THREE_DAY_VIEW;
                mWeekView.setNumberOfVisibleDays(3);
                mWeekView.setDayNameLength(com.alamkanak.weekview.WeekView.LENGTH_LONG);
                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
            }
            return true;
        case R.id.action_week_view:
            if (mWeekViewType != TYPE_WEEK_VIEW) {
                item.setChecked(!item.isChecked());
                mWeekViewType = TYPE_WEEK_VIEW;
                mWeekView.setNumberOfVisibleDays(7);
                mWeekView.setDayNameLength(com.alamkanak.weekview.WeekView.LENGTH_SHORT);
               // mWeekView.setColumnGap(15);
                // Lets change some dimensions to best fit the view.
                mWeekView.setColumnGap((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                mWeekView.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 8, getResources().getDisplayMetrics()));
                mWeekView.setEventTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
            }
            return true;
		}
        return super.onOptionsItemSelected(item);
	}

	public WeekViewEvent labScheduleToWeekViewEvent(LabSchedule ls, Calendar onDate)
	{
		WeekViewEvent event = new WeekViewEvent();
		
		Calendar startTime = (Calendar) onDate.clone();
		
		startTime.set(Calendar.HOUR_OF_DAY, ls.getScheduleStartHour());
		startTime.set(Calendar.MINUTE, 0);
		event.setStartTime(startTime);
		
		Calendar endTime = (Calendar) onDate.clone();
		endTime.set(Calendar.HOUR_OF_DAY, ls.getScheduleEndHour());
		endTime.set(Calendar.MINUTE, 0);
		event.setEndTime(endTime);		
		
		event.setColor(ls.getScheduleColor());
		event.setName(ls.getLabName());
		Log.d(Constants.TAG,"start time = "+ event.getStartTime() +" end time = "+event.getEndTime());
		return event;
	}
	@Override
	public List<WeekViewEvent> onMonthChange(int newYear, int newMonth) {

		Toast.makeText(this, "month change", Toast.LENGTH_SHORT).show();
		// Populate the week view with some events.
		List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

		
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 3);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.MONTH, newMonth);
		c.set(Calendar.YEAR, newYear);
		c.set(Calendar.DAY_OF_MONTH, 1);
		Integer maxDayOfNewMonth = Integer.valueOf(c.getActualMaximum(Calendar.DAY_OF_MONTH));
		Log.d(Constants.TAG, "max day of month = "+String.valueOf(maxDayOfNewMonth));
		for (int i=1; i<=maxDayOfNewMonth; i++)
		{
			c.set(Calendar.DAY_OF_MONTH, i);
			for (int j=0; j<=labSchedules.size()-1; j++)
			{
				if (labSchedules.get(j).getScheduleDayOfWeek()==c.get(Calendar.DAY_OF_WEEK))
				{
					events.add(labScheduleToWeekViewEvent(labSchedules.get(j), c));
				}
			}
		}


		return events;
	}



	private String getEventTitle(Calendar time) {
		return String.format("Event of %02d:%02d %s/%d", time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), time.get(Calendar.MONTH)+1, time.get(Calendar.DAY_OF_MONTH));
	}

	@Override
	public void onEventClick(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(ScheduleWeekViewActivity.this, "Clicked " + event.getName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
		Toast.makeText(ScheduleWeekViewActivity.this, "Long pressed event: " + event.getName(), Toast.LENGTH_SHORT).show();
	}
	
	private class FetchLabSchedules extends AsyncTask<String, Void, List<LabSchedule>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ScheduleWeekViewActivity.this );
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
			//Log.d( Constants.TAG + " Response: ", "> " + jsonStr );

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
			generateEventPattern();
			
			mWeekView.notifyDatasetChanged();
		}
	}

	public class CustomComparator implements Comparator<LabSchedule> {
		@Override
		public int compare(LabSchedule o1, LabSchedule o2) {
			return o1.getScheduleStartHour().compareTo(o2.getScheduleStartHour());
		}
	}
	
	
	public void generateEventPattern(){

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
				if (lS.getScheduleDayOfWeek().equals(k+1))
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
		

	}	
}
