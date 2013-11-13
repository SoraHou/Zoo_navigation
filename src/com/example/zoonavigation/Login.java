package com.example.zoonavigation;

import java.io.File;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import ext.api.*;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class Login extends Activity {

	// Define widget
	private Button login;
	private Button reg;
//	private Button clear;
	private EditText username;
	private EditText password;
	
	private DBHelper dbHelper;
	private Cursor dbCursor;
	
	private ProgressDialog loading = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		initWidget();
		
		dbHelper = new DBHelper(this);
		dbCursor = dbHelper.readAccount();
		
		// Set default behavior
//		clear.setOnClickListener(onClickClear);
		login.setOnClickListener(onClickLogin);
		reg.setOnClickListener(onClickReg);
		 GPSCheck();
		//dirCheck();

		
		loading = new ProgressDialog(Login.this);
		loading.setTitle("登入中");
		loading.setMessage("請稍候...");
		loading.setCancelable(false);

		if (dbCursor.getCount() > 0) {
			dbCursor.moveToFirst();
			
			username.setText(dbCursor.getString(dbCursor.getColumnIndex("user")));
			password.setText(dbCursor.getString(dbCursor.getColumnIndex("pass")));
		}
	}

	private void initWidget() {
		login = (Button) findViewById(R.id.button1);
		reg = (Button) findViewById(R.id.button2);
//		clear = (Button) findViewById(R.id.button2);
		password = (EditText) findViewById(R.id.editText1);
		username = (EditText) findViewById(R.id.editText2);
	}

	

	// Check and Create Directory
	/*private void dirCheck() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File SDCard = Environment.getExternalStorageDirectory();
			File dirPath = new File(SDCard.getPath() + "/ToxicApp");
			if (!dirPath.exists())
				dirPath.mkdirs();
		}
	}*/

	// Clear Username and Password
	/*private Button.OnClickListener onClickClear = new Button.OnClickListener() {

		@Override
		public void onClick(View v) {
			username.setText("");
			password.setText("");
			
			dbHelper.deleteAccount();
			dbCursor.requery();
		}

	};*/
	private void GPSCheck() {
		LocationManager status = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

		if (!status.isProviderEnabled(LocationManager.GPS_PROVIDER)
				|| !status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
			Toast.makeText(Login.this, "請開啟GPS", Toast.LENGTH_LONG)
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
	// Login
	private Handler loginHandler = new Handler() {
		
		public void handleMessage(Message msg) {
			
			loading.dismiss();
			boolean isLogin = msg.getData().getBoolean("isLogin");
			
			if (isLogin) {
				String name = username.getText().toString();
				String pass = password.getText().toString();
				
				dbHelper.insertAccount(name, pass);
				dbCursor.requery();
				
				Intent i = new Intent();
				i.setClass(Login.this, Form.class);
				startActivity(i);
				finish();
			}
			else {
				Toast.makeText(Login.this, "登入失敗", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	private Button.OnClickListener onClickLogin = new Button.OnClickListener() {

		public void onClick(View v) {
			
			final String name = username.getText().toString();
			final String pass = password.getText().toString();
			
			if (name.equals("") || pass.equals("")) {
				Toast.makeText(Login.this, "請輸入帳號密碼", Toast.LENGTH_LONG).show();
				return;
			}
			
			if(NetworkCheck()) {
			
				loading.show();
	
				new Thread(new Runnable() {
	
					@Override
					public void run() {
						// TODO Auto-generated method stub
	
						Message message = new Message();
						Bundle bundle = message.getData();
	
						try {
							boolean result = ServiceCaller.login(name, pass);
							bundle.putBoolean("isLogin", result);
							
							message.setData(bundle);
							loginHandler.sendMessage(message);
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
			else {
				Toast.makeText(Login.this, "請連接網路", Toast.LENGTH_LONG).show();
			}
		}

	};
	private Button.OnClickListener onClickReg = new Button.OnClickListener() {

		public void onClick(View v) {
			Intent intent = new Intent(); // intent實體化
			intent.setClass(Login.this, NavigationPage.class);
			startActivity(intent); // startActivity觸發換頁
			finish(); // 換頁後結束此頁
		}

	};


}
