package com.mingrisoft;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	BroadcastReceiver mReceiver = null;
	boolean mIsScanning = false;
	WifiManager mWifiManager;
	boolean mOrigWifiState;
	StringBuffer mScanResultsSB;
	IntentFilter mFilter;
	Handler mHandler;
	RabbitView rabbit;
	float X_SCREEN;
	float Y_SCREEN;
	float de;

	
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);
		DisplayMetrics dispayMetrics = new DisplayMetrics();
		dispayMetrics = getResources().getDisplayMetrics();
		
		
		
		X_SCREEN = dispayMetrics.widthPixels;
		Y_SCREEN = dispayMetrics.heightPixels;
		
		Toast.makeText(getApplicationContext(), X_SCREEN+":"+Y_SCREEN, Toast.LENGTH_LONG).show();

		
		FrameLayout frameLayout = (FrameLayout) findViewById(R.id.mylayout); // ��ȡ֡���ֹ�����
		rabbit = new RabbitView(MainActivity.this); // ������ʵ����RabbitView��
		// ΪС������Ӵ����¼�����
		rabbit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				rabbit.bitmapX = event.getX(); // ����С������ʾλ�õ�X����
				rabbit.bitmapY = event.getY(); // ����С������ʾλ�õ�Y����
				rabbit.invalidate(); // �ػ�rabbit���
				return true;
			}
		});
		frameLayout.addView(rabbit); // ��rabbit��ӵ����ֹ�������

		mWifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
		mOrigWifiState = mWifiManager.isWifiEnabled();
		mWifiManager.setWifiEnabled(true);
		mReceiver = new BroadcastReceiver(){
			@Override
			public void onReceive(Context context, Intent intent){//!
				if(mIsScanning){
					getLocfromServer();
					mWifiManager.startScan();
				}
			}
		};
		mFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		registerReceiver(mReceiver, mFilter);
		mScanResultsSB = new StringBuffer(256);
		
		
		mIsScanning = true;
		mWifiManager.startScan();
	}
	
	void getLocfromServer() {
		//ͨ��socket����������͡���Ϣ����Ȼ���ڽ��շ��������ص�λ��֮�󣬽�����ӳ���С����
		// change scanresults to msg, msg format is "(ap-ssid:ap-bssid,rssi)*" like 
		// "00:b0:0c:42:8f:68,-65,00:25:86:3c:0d:b2,-67"
		List<ScanResult> scanResults = mWifiManager.getScanResults();
		ScanResult scanResult;
		mScanResultsSB.setLength(0);
		for(int i=0; i<scanResults.size(); ++i){
			scanResult = scanResults.get(i);
			mScanResultsSB.append(scanResult.SSID.replaceAll(",", "#")+":"); //! change , in ssid to #
			mScanResultsSB.append(scanResult.BSSID+",");
			mScanResultsSB.append(scanResult.level+";");
		}
		//FIX: mScanResultsSB maybe null
		String msg = mScanResultsSB.toString();
		if (msg.endsWith(";")) msg = mScanResultsSB.deleteCharAt(mScanResultsSB.length()-1).toString();
		new LinkThread(MainActivity.this, msg).start();
		mHandler = new Handler(){
			public void handleMessage(Message msg) {
				if (msg.what == 0x101) {
					String location = msg.getData().getString("location");
					updateLocAtUI(location);
				}
				super.handleMessage(msg);
			}
		};
	}
	
	void updateLocAtUI(String locStr) {
		String locs[] = locStr.split(",");
		float x = Float.valueOf(locs[0]);
		float y = Float.valueOf(locs[1]);
		
		float X = (float) (((x+5.288)/36.61)*X_SCREEN); // TODO: map x to X need fucntion
		float Y = (float) (((y+6.965)/53.324)*Y_SCREEN); // TODO: map y to Y need function
		
		rabbit.bitmapX = X; // ����С������ʾλ�õ�X����
		rabbit.bitmapY = Y; // ����С������ʾλ�õ�Y����
		rabbit.invalidate(); // �ػ�rabbit���
	}
	
	@Override
	public void onDestroy(){
		mIsScanning = false;
		mWifiManager.setWifiEnabled(mOrigWifiState);
		super.onDestroy();
	}
}