package com.algonquincollege.yuan0037.ictlabschedules;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import util.ColoredArrayAdapter;
import util.ServiceHandler;

import domain.Lab;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import android.widget.Toast;

/**
 * Display the list of ICT labs.
 *
 * Usage:
 * 1) Click one of the labs from the list to see its schedule.
 *
 * Notes:
 * 1) class ListLabsActivity extends from Android's class ListActivity.
 *
 * @author yuan0037@algonquinlive.com
 * @Version 1.0
 */
public class ListLabsActivity extends ListActivity implements Constants {
	// JSON Node names
	private static final String TAG_ICT_LABS    = "ict-labs";
	private static final String TAG_ROOM        = "room";
	private static final String TAG_DESCRIPTION = "description";
	private static final String REMOTE_URL="http://faculty.edumedia.ca/hurdleg/ict/tt/";
	private Integer currentPositionID;
	private ArrayAdapter<Lab> labsAdapter;
	private List<Lab> labsList;
	private List<String> labScheduleJSONStringList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_labs);

		//TODO: List adapter
		labsList = new ArrayList<Lab>();
		labScheduleJSONStringList=new ArrayList<String>();
		labsAdapter = new ColoredArrayAdapter(this, android.R.layout.simple_list_item_1);

		//if (labsList.size()==0)
		new FetchLabs().execute(REMOTE_URL);


		//
		this.setListAdapter(labsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_author) {
			Toast.makeText(this, "Bo Yuan (yuan0037@algonquinlive.com)", Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode==1)
		{
			if (resultCode==RESULT_OK)
			{
				//Log.d(Constants.TAG, "now get data from detail activity: "+data.getStringExtra("scheduleJSONString"));
				if (labScheduleJSONStringList.get(currentPositionID).equals(""))
				{
					labScheduleJSONStringList.set(currentPositionID,String.valueOf(data.getStringExtra("scheduleJSONString")));
				}
			}
		}
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);

		Lab lab = labsAdapter.getItem(position); //names.get(position);
		currentPositionID=Integer.valueOf(position);
		Intent intent = new Intent(this, ScheduleGridActivity.class);
		intent.putExtra("domain.Lab", lab);
		//Log.d(Constants.TAG, "now sending data to detail activity: "+labScheduleJSONStringList.get(currentPositionID));
		intent.putExtra("scheduleJSONString", labScheduleJSONStringList.get(currentPositionID));

		startActivityForResult(intent, 1);

	}

	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class FetchLabs extends AsyncTask<String, Void, List<Lab>> {
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
			pDialog = new ProgressDialog( ListLabsActivity.this );
			pDialog.setMessage( "Please wait..." );
			pDialog.setCancelable( false );
			pDialog.show();

			labsList.clear();
		}

		@Override
		protected List<Lab> doInBackground( String... params ) {
			List<Lab> labs = new ArrayList<Lab>();

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall( params[0], ServiceHandler.GET ) ;

			//Log.d( TAG + " Response: ", "> " + jsonStr );

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject( jsonStr );

					// Getting JSON Array node
					JSONArray jsonLabs = jsonObj.getJSONArray( TAG_ICT_LABS );

					// looping through each Lab, one at a time
					for (int i = 0; i < jsonLabs.length(); i++) {
						JSONObject jsonLab = jsonLabs.getJSONObject( i );

						String room = jsonLab.getString( TAG_ROOM );
						String description = jsonLab.getString( TAG_DESCRIPTION );

						// add this lab to the list of labs
						Lab newLab=new Lab(room, description);
						labs.add(newLab);

					}

					return labs;
				} catch ( JSONException e ) {
					e.printStackTrace();
				}
			} else {
				Log.e( TAG + " ServiceHandler", "Couldn't get any data from the url" );
			}

			return null;
		}

		@Override
		protected void onPostExecute( List<Lab> result ) {
			super.onPostExecute( result );
			// Dismiss the progress dialog
			if ( pDialog.isShowing() )
				pDialog.dismiss();
			//Log.d("ICTLab", String.valueOf(result.size()));'
			//labsList.clear();
			labsList.addAll( result );
			for (int i=0; i<=labsList.size()-1;i++)
			{
				labScheduleJSONStringList.add(String.valueOf(""));
			}
			labsAdapter.clear();
			labsAdapter.addAll(labsList);
			labsAdapter.notifyDataSetChanged();
		}
	}
}