package com.algonquincollege.yuan0037.ictlabschedules;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.algonquincollege.mad9132.ictlabschedules.R;

import util.ServiceHandler;

import domain.Lab;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * Display the list of ICT labs.
 *
 * Usage:
 * 1) Click one of the labs from the list to see its schedule.
 *
 * Notes:
 * 1) class ListLabsActivity extends from Android's class ListActivity.
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @Version 1.0
 */
public class ListLabsActivity extends ListActivity implements Constants {
	// JSON Node names
	private static final String TAG_ICT_LABS    = "ict-labs";
	private static final String TAG_ROOM        = "room";
	private static final String TAG_DESCRIPTION = "description";

	private ArrayAdapter<Lab> labsAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_labs);

		//TODO: List adapter
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
			return true;
		}
		return super.onOptionsItemSelected(item);
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

			labsAdapter.clear();
		}

		@Override
		protected List<Lab> doInBackground( String... params ) {
			List<Lab> labs = new ArrayList<Lab>();

			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();

			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall( params[0], ServiceHandler.GET ) ;

			Log.d( TAG + " Response: ", "> " + jsonStr );

			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject( jsonStr );

					// Getting JSON Array node
					JSONArray jsonLabs = jsonObj.getJSONArray( TAG_ICT_LABS );

					//labs.empty();
					// looping through each Lab, one at a time
					for (int i = 0; i < jsonLabs.length(); i++) {
						JSONObject jsonLab = jsonLabs.getJSONObject( i );

						String room = jsonLab.getString( TAG_ROOM );
						String description = jsonLab.getString( TAG_DESCRIPTION );

						// add this lab to the list of labs
						//TODO: labs.add( new Lab(room, description) );
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
			//TODO: labsAdapter.addAll( result );
		}
	}
}