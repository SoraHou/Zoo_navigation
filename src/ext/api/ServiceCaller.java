package ext.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import android.sax.Element;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.util.Log;

import org.json.*;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class ServiceCaller {
	private static String host = "";
	private static String token = "";
	private static String xmlUrl = "";
	private static String xmlMember = "";

	
	private static HashMap<String, String> macAddress = null;


	// Create MD5 String
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);

		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}

		return hexValue.toString();
	}

	// Get Token
	public static String getToken() {
		return token;
	}
	
	// Get Event
	public static HashMap<String, String> getMACAddress() {
		return macAddress;
	}

	

	// Login
	public static boolean login(String username, String password)
			throws ClientProtocolException, IOException {
		String API = "getMember.ashx";
		String strRet = "";
		String media = "0";

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(host + API);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("id", username));
		nvps.add(new BasicNameValuePair("pd", MD5(password)));
		nvps.add(new BasicNameValuePair("media", media));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		entity = response.getEntity();

		Log.d("HTTP_DEBUG",
				"HTTP POST getSatusLINE: " + response.getStatusLine());
		Log.d("HTTP_DEBUG", strRet);

		strRet = EntityUtils.toString(entity);
		strRet = strRet.trim();

		if (entity != null) {
			entity.consumeContent();
		}

		Log.d("Entity", entity.toString());
		Log.d("Entity", strRet);

		try {
			JSONObject json = new JSONObject(strRet);

			if (json.getLong("status") == 0) {

				// Get Token, Url, Member
				token = json.getString("token");
				xmlUrl = json.getString("xmlUrl");
				xmlMember = json.getString("xmlMember");
				Log.d("test", xmlMember);
	

				return true;
			}

			else
				return false;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	// Initialize Event


	

	// Locate
	public static boolean locate(String UDID, String lon, String lat)
			throws ClientProtocolException, IOException {
		String API = "uploadFile.ashx";
		String strRet = "";

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(host + API);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("token", token));
		nvps.add(new BasicNameValuePair("UDID", UDID));
		nvps.add(new BasicNameValuePair("lon", lon));
		nvps.add(new BasicNameValuePair("lat", lat));
		nvps.add(new BasicNameValuePair("location", "1"));

		httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		entity = response.getEntity();

		Log.d("HTTP_DEBUG",
				"HTTP POST getSatusLINE: " + response.getStatusLine());
		Log.i("HTTP_DEBUG", strRet);

		strRet = EntityUtils.toString(entity);
		strRet = strRet.trim();

		if (entity != null) {
			entity.consumeContent();
		}

		Log.d("Entity", entity.toString());
		Log.d("Entity", strRet);

		try {
			JSONObject json = new JSONObject(strRet);

			return (json.getLong("status") == 0);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	// Upload Event
	public static boolean upload(String jsonString)
			throws ClientProtocolException, IOException {
		String API = "uploadFile.ashx";
		String strRet = "";

		DefaultHttpClient httpclient = new DefaultHttpClient();
		HttpPost httpost = new HttpPost(host + API);

		Charset encode = Charset.forName("utf-8");
		MultipartEntity reqEntity = new MultipartEntity();

		try {
			JSONObject json = new JSONObject(jsonString);

			String filename = json.getString("fileName");
			int index = filename.split("/").length - 1;
			filename = filename.split("/")[index];

			reqEntity.addPart("token", new StringBody(token));
			reqEntity.addPart("UDID", new StringBody(json.getString("UDID")));
			reqEntity.addPart("AccNo", new StringBody(json.getString("AccNo")));
			reqEntity.addPart("lon", new StringBody(json.getString("lon")));
			reqEntity.addPart("lat", new StringBody(json.getString("lat")));

			if (!json.getString("etype").equals("")) {
				reqEntity.addPart("CheName",
						new StringBody(json.getString("CheName"), encode));
				reqEntity.addPart("etype",
						new StringBody(json.getString("etype"), encode));
				reqEntity.addPart("evalue",
						new StringBody(json.getString("evalue")));
				reqEntity.addPart("eunit",
						new StringBody(json.getString("eunit"), encode));
			}

			if (!json.getString("fileName").equals("")) {
				reqEntity.addPart("fileName", new StringBody(filename, encode));
				reqEntity.addPart("File1",
						new FileBody(new File(json.getString("fileName"))));
			}

			reqEntity.addPart("location", new StringBody("0"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		httpost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httpost);
		HttpEntity entity = response.getEntity();
		entity = response.getEntity();

		Log.d("HTTP_DEBUG",
				"HTTP POST getSatusLINE: " + response.getStatusLine());
		Log.i("HTTP_DEBUG", strRet);

		strRet = EntityUtils.toString(entity);
		strRet = strRet.trim();

		if (entity != null) {
			entity.consumeContent();
		}

		Log.d("Entity", entity.toString());
		Log.d("Entity", strRet);

		try {
			JSONObject json = new JSONObject(strRet);

			return (json.getLong("status") == 0);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}
}