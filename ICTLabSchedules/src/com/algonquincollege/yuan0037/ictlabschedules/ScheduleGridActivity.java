package com.algonquincollege.yuan0037.ictlabschedules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;

import util.ServiceHandler;
import domain.Lab;
import domain.LabSchedule;
import android.R.color;
import android.app.Activity;
import android.app.ProgressDialog;
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

public class ScheduleGridActivity extends Activity {
	private Lab currentLab;
	private List<LabSchedule> labSchedules;
	private GridLayout labGrid; 

	private static final String[] days = 
		{"sunday", "monday", "tuesday", 
		"wednesday", "thursday", "friday", "saturday"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_grid);
		labSchedules = new ArrayList<LabSchedule>();

		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		currentLab = b.getParcelable("domain.Lab");


	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.d(Constants.TAG, Constants.URL+currentLab.getRoom().toLowerCase());
		new FetchLabSchedules().execute(Constants.URL+currentLab.getRoom().toLowerCase());
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
		if (item.getItemId() == android.R.id.home) {
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
			List<LabSchedule> schedules = new ArrayList<LabSchedule>();

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall( params[0], ServiceHandler.GET ) ;

			Log.d( Constants.TAG + " Response: ", "> " + jsonStr );

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject( jsonStr );

					String tempStr = jsonObj.getString(currentLab.getRoom().toLowerCase());
					// Getting JSON Array node
					//JSONArray jsonLabSchedules = jsonObj.getJSONArray(currentLab.getRoom().toLowerCase());
					//;
					// looping through each schedule, one at a time
					Log.d( Constants.TAG, "tempStr1= "+tempStr);

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
								Log.d(Constants.TAG, "object added"+lSchedule.getRoom()+lSchedule.getLabName()+days[lSchedule.getScheduleDayOfWeek()]+lSchedule.getScheduleStartHour());
								labSchedules.add(lSchedule);
							}
						}
					}

					return null;
				} catch ( JSONException e ) {
					Log.d(Constants.TAG, e.getMessage().toString());
					e.printStackTrace();
				}
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

			Log.d(Constants.TAG, String.valueOf(labSchedules.size()));
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

		labGrid=(GridLayout) this.findViewById(R.id.scheduleGridView);
		labGrid.removeAllViews();
		labGrid.setRowCount(11);
		labGrid.setColumnCount(8);

		TextView tvColumnHourTitle = new TextView(this);
		tvColumnHourTitle.setText("Time");
		tvColumnHourTitle.setWidth(100);
		GridLayout.LayoutParams params = new GridLayout.LayoutParams();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		params.setGravity(Gravity.CENTER);
		params.rowSpec= GridLayout.spec(0);
		params.columnSpec=GridLayout.spec(0);
		tvColumnHourTitle.setLayoutParams(params);

		labGrid.addView(tvColumnHourTitle);

		for (int i=0; i<=6;i++)
		{
			TextView tvColumnTitle = new TextView(this);
			tvColumnTitle.setText(days[i].substring(0, 3));
			tvColumnTitle.setWidth(100);

			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.WRAP_CONTENT;
			paramsForTitle.width = LayoutParams.WRAP_CONTENT;
			paramsForTitle.setGravity(Gravity.CENTER);
			paramsForTitle.rowSpec= GridLayout.spec(0);
			paramsForTitle.columnSpec=GridLayout.spec(i+1);

			tvColumnTitle.setLayoutParams(paramsForTitle);
			labGrid.addView(tvColumnTitle);

		}
		
		for (int i=8; i<=17; i++)
		{
			TextView tvHourTitle = new TextView(this);
			tvHourTitle.setText(String.format("%2d", i));
			tvHourTitle.setWidth(100);
			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.MATCH_PARENT;
			paramsForTitle.width = LayoutParams.MATCH_PARENT;

			paramsForTitle.rowSpec= GridLayout.spec(i-8+1);
			paramsForTitle.columnSpec=GridLayout.spec(0);
			paramsForTitle.setGravity(Gravity.CENTER);
			tvHourTitle.setLayoutParams(paramsForTitle);
			labGrid.addView(tvHourTitle);
		}
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
			Log.d(Constants.TAG, days[k]+ " daily count before processing="+String.valueOf(dailyScheduleList.size()));
			Collections.sort(dailyScheduleList, new CustomComparator());
			for (int l=dailyScheduleList.size()-1; l>=1; l--)
			{

				if ((dailyScheduleList.get(l).getLabName().equals(dailyScheduleList.get(l-1).getLabName())) &&(dailyScheduleList.get(l).getScheduleStartHour().equals(dailyScheduleList.get(l-1).getScheduleEndHour())))
				{
					Log.d(Constants.TAG, "L ="+dailyScheduleList.get(l).getScheduleStartHour());
					Log.d(Constants.TAG, "L-1 ="+dailyScheduleList.get(l-1).getScheduleStartHour());
					dailyScheduleList.get(l-1).setScheduleEndHour(dailyScheduleList.get(l).getScheduleEndHour());
					dailyScheduleList.remove(l);
					Log.d(Constants.TAG, days[k]+" daily count after processing="+String.valueOf(dailyScheduleList.size()));
					
				}
			}
			newWeekScheduleList.addAll(dailyScheduleList);
		}

		labSchedules.clear();
		labSchedules.addAll(newWeekScheduleList);
		for(LabSchedule lS : labSchedules)
		{
			TextView tvSchedule = new TextView(this);
			tvSchedule.setText(lS.getLabName());
			tvSchedule.setWidth(100);
			

			GridLayout.LayoutParams paramsForTitle = new GridLayout.LayoutParams();
			paramsForTitle.height = LayoutParams.MATCH_PARENT;
			paramsForTitle.width = LayoutParams.MATCH_PARENT;
			
			Log.d(Constants.TAG, "drawing:"+days[lS.getScheduleDayOfWeek()]+lS.getLabName()+ String.valueOf(lS.getScheduleEndHour()-lS.getScheduleStartHour()));
			paramsForTitle.rowSpec= GridLayout.spec(lS.getScheduleStartHour()-8+1,lS.getScheduleEndHour()-lS.getScheduleStartHour());

			paramsForTitle.columnSpec=GridLayout.spec(lS.getScheduleDayOfWeek()+1);
			paramsForTitle.setGravity(Gravity.FILL);
			//|Gravity.CENTER_VERTICAL);
			tvSchedule.setLayoutParams(paramsForTitle);
			tvSchedule.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			tvSchedule.setBackgroundColor(lS.getScheduleColor());
			labGrid.addView(tvSchedule);
		}
	}	

}