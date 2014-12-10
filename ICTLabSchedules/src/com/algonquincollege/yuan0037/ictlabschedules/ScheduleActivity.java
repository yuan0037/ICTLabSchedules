package com.algonquincollege.yuan0037.ictlabschedules;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import util.ServiceHandler;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableRow.LayoutParams;

import com.algonquincollege.yuan0037.ictlabschedules.R;

import domain.Lab;
import domain.LabSchedule;

import com.algonquincollege.yuan0037.ictlabschedules.Constants;


public class ScheduleActivity extends Activity {
	private Lab currentLab;
	private List<LabSchedule> labSchedules;
	private TableLayout generalTable;
	private String labScheduleJSONString;
	private static final String[] days = 
		{"sunday", "monday", "tuesday", 
		"wednesday", "thursday", "friday", "saturday"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		
		labSchedules = new ArrayList<LabSchedule>();
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle b = getIntent().getExtras();
		currentLab = b.getParcelable("domain.Lab");
		
		
		//refreshDisplay();
		
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
		getMenuInflater().inflate(R.menu.schedule, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Toast.makeText(this, currentLab.getRoom(), Toast.LENGTH_SHORT).show();
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
			pDialog = new ProgressDialog( ScheduleActivity.this );
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
			labScheduleJSONString=jsonStr;
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
			generateTable();
		}
	}
	
	public void generateTable(){
		generalTable=(TableLayout) this.findViewById(R.id.scheduleTableLayout);
		generalTable.removeAllViews();
		if (generalTable!=null)
		{
			//loop from 8H00 to 17H00; 
			TableRow firstRow = new TableRow(this);
			LayoutParams lpFirstRow = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			firstRow.setLayoutParams(lpFirstRow);

			TextView tvColumnHourTitle = new TextView(this);
			tvColumnHourTitle.setText("Time");
			tvColumnHourTitle.setWidth(100);
			firstRow.addView(tvColumnHourTitle);

			for (int i=0; i<=6;i++)
			{
				TextView tvColumnTitle = new TextView(this);
				tvColumnTitle.setText(days[i].substring(0, 3));
				tvColumnTitle.setWidth(100);
				
				firstRow.addView(tvColumnTitle);
				
			}
			generalTable.addView(firstRow, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				
			for (int i=8; i<=17; i++)
			{
				 TableRow tr = new TableRow(this);
				 LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				 tr.setLayoutParams(lp);
				 
				 TextView tvHourTitle = new TextView(this);
				 tvHourTitle.setText(String.format("%2d", i));
				 tvHourTitle.setWidth(100);
				 tr.addView(tvHourTitle);
					
				for (int j=0; j<=6; j++)
				{
					Boolean tempFound=false;
					
					for(LabSchedule lS : labSchedules)
					{
						//Log.d(Constants.TAG, "looping now:"+ lS.getLabName()+days[lS.getScheduleDayOfWeek()]);
				        if ((lS.getScheduleStartHour().equals(i)) && (lS.getScheduleDayOfWeek().equals(j))){
				        	TextView cellTitle=new TextView(this);
							
							cellTitle.setText(lS.getLabName());
							cellTitle.setWidth(100);
							cellTitle.setBackgroundColor(lS.getScheduleColor());
							Log.d(Constants.TAG, "color = "+String.valueOf(lS.getScheduleColor()));
							tr.addView(cellTitle);
							tempFound=true;
							break;
				        }				          
				    }
					if (!tempFound)
					{
						TextView cellTitle=new TextView(this);
						
						cellTitle.setText("");
						cellTitle.setWidth(100);
						tr.addView(cellTitle);
					}
					
					
					
				}
				generalTable.addView(tr, new TableLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			}
			 	
			 

			 // generalTable.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		}
	}	
}



