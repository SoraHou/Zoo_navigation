package com.example.zoonavigation;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ext.api.ServiceCaller;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

@SuppressLint("DefaultLocale")
public class LocateService extends Service implements LocationListener {

	private String MAC;
	private String lon;
	private String lat;
	
	private LocationManager lms;
	private String bestProvider = LocationManager.GPS_PROVIDER;
	
	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		handler.postDelayed(showTime, 1000);
		super.onStart(intent, startId);
		
		LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
		if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) && status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationServiceInitial();
		}
		
		WifiManager wifiMan = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInf = wifiMan.getConnectionInfo();
		MAC = wifiInf.getMacAddress().toUpperCase();
	}

	@Override
	public void onDestroy() {
		handler.removeCallbacks(showTime);
		super.onDestroy();
	}

	private Runnable showTime = new Runnable() {
		
		public void run() {
			
			// Delay 5min
			handler.postDelayed(this, 300000);
			
			getLocation(lms.getLastKnownLocation(bestProvider));

			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					try {
						if (ServiceCaller.locate(MAC, lon, lat)) {
							Log.d("Locate", MAC + "-" + lon + ":" + lat);
						}
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}).start();
			
		}
		
	};
	
	private void locationServiceInitial() {
		lms = (LocationManager) getSystemService(LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		bestProvider = lms.getBestProvider(criteria, true);
		Location location = lms.getLastKnownLocation(bestProvider);
		getLocation(location);
	}

	private void getLocation(Location location) {
		if (location != null) {
			lon = String.valueOf(location.getLongitude());
			lat = String.valueOf(location.getLatitude());
		}
	}
	
	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		getLocation(arg0);
		Log.d("Locate", "Change");
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
}