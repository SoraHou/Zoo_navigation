package com.example.zoonavigation;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button login;
	private Button start;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mainactivity);
		initWidget();
		login.setOnClickListener(onClickLogin);
		start.setOnClickListener(onClickStart);
		GPSCheck();
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		login = (Button) findViewById(R.id.login);
		start = (Button) findViewById(R.id.start);

	}

	// Check GPS
	private void GPSCheck() {
		LocationManager status = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		if (!status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			Toast.makeText(MainActivity.this, "�ж}��GPS", Toast.LENGTH_LONG)
					.show();
		}

	}

	// Check Network
	private boolean NetworkCheck() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();

		return ni != null && ni.isConnected();
	}

	private Button.OnClickListener onClickStart = new Button.OnClickListener() {
		public void onClick(View v) {
			if (NetworkCheck()) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						
						Intent intent = new Intent(); // intent�����
						intent.setClass(MainActivity.this, NavigationPage.class);
						startActivity(intent); // startActivityĲ�o����
						finish(); // �����ᵲ������

					}

				}).start();

			} else {
				Toast.makeText(MainActivity.this, "�гs������", Toast.LENGTH_LONG)
						.show();
			}

		}
	};
	private Button.OnClickListener onClickLogin = new Button.OnClickListener() {
		public void onClick(View v) {

			Intent intent = new Intent(); // intent�����
			intent.setClass(MainActivity.this, Login.class);
			startActivity(intent); // startActivityĲ�o����
			finish(); // �����ᵲ������
		}
	};

}
