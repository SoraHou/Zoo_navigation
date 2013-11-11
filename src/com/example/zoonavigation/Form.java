package com.example.zoonavigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import ext.api.ServiceCaller;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class Form extends Activity implements LocationListener {

	private String longitude;
	private String latitude;
	
	// Define widget
	private EditText longitudeAndLatitude;

	private DBHelper dbHelper;
	
	private String accidentNo;
	private String accidentName;
	private String instrumentName;
	private String chemicalName;
	private String MAC;
	private String path;
	
	private Spinner event;
	private Spinner instrument;
	private Spinner chemicalNameSpinner;
	
	private EditText chemicalNameEditText;
	private EditText quantity;
	private EditText unit;

	private Button record;
	private Button upload;
	private Button skype;
	
	private ArrayAdapter<String> eventAdapter;
	private ArrayAdapter<String> instrumentAdapter;
	private ArrayAdapter<String> chemicalAdapter;

	private String bestProvider = LocationManager.GPS_PROVIDER;
	private boolean getService = false;

	private LocationManager lms;

	private HashMap<String, HashMap<String, String>> IChemical;
	private HashMap<String, String> accident;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//setContentView(R.layout.form);

		chemicalName = "";
		dbHelper = new DBHelper(this);

		// Initialize All Widgets
	

		// Initialize Location Manager
		LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));

		// Check GPS Status
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) && status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			getService = true;
			locationServiceInitial();
		} else {
			Intent i = new Intent();
			i.setClass(Form.this, Login.class);
			startActivity(i);
			finish();
			Toast.makeText(Form.this, "½Ð¶}±ÒGPS", Toast.LENGTH_LONG).show();
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
		}
		
		// Get MAC Address
		WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		MAC = wifiInf.getMacAddress().toUpperCase();
		Log.d("MAC Address", MAC);
	
		
		chemicalNameSpinner.setVisibility(Spinner.INVISIBLE);

		
		// Start Service
		Intent intent = new Intent();
		intent.setClass(Form.this, LocateService.class);
		startService(intent);
	}

	

		public void onNothingSelected(AdapterView<?> arg0) {

		}


	

	


	
	
	


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (getService) {
			lms.requestLocationUpdates(bestProvider, 1000, 1, this);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (getService) {
			lms.removeUpdates(this);
		}
	}

	private void locationServiceInitial() {
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		bestProvider = lms.getBestProvider(criteria, true);
		Location location = lms.getLastKnownLocation(bestProvider);
		getLocation(location);
	}

	private void getLocation(Location location) {
		if (location != null) {
			longitude = String.valueOf(location.getLongitude());
			latitude = String.valueOf(location.getLatitude());
			
			longitudeAndLatitude.setText(longitude + ", " + latitude);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// User Logout
		/*case R.id.menu_logout:
			Intent logoutIntent = new Intent();
			logoutIntent.setClass(Form.this, MainActivity.class);
			startActivity(logoutIntent);

			finish();
			break;*/
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		getLocation(location);
		Log.d("Form", "Change");
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

}
