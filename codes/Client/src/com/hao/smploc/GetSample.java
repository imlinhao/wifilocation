package com.hao.smploc;

import java.util.List;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

public class GetSample extends Thread {
	private GetSampleState getSampleState = GetSampleState.UNGETSAMPLE;
	private int x;
	private int y;
	private int z;
	boolean ap1Exist = false;
	boolean ap2Exist = false;
	int ap1Level;
	int ap2Level;
	Connect connect = null;
	String location = null;
	int interval = 100;//采样间隔
	int times = 100 ; //采样次数
	private WifiManager mWifiManager = null;
	private static final String AP1_BSSID = "00:b0:0c:42:77:70";
	//private static final String AP2_BSSID = "00:b0:0c:42:8f:68";
	private static final String AP2_BSSID = "36:ec:01:69:b9:0b";

	public GetSample(Context context, int x, int y, int z) {
		mWifiManager = (WifiManager) context
				.getSystemService(Context.WIFI_SERVICE);
		this.x = x;
		this.y = y;
		this.z = z;
		this.connect = new Connect();
	}

	public GetSampleState getGetSampleState() {
		return this.getSampleState;
	}

	public void setGetSampleState(GetSampleState getSampleState) {
		this.getSampleState = getSampleState;
	}

	private void getSignal() {
		int ap1ScannedTimes =0;
		int ap1SumLevel = 0;
		int ap2ScannedTimes =0;
		int ap2SumLevel = 0;
		//初始ap1,ap2都没有扫到信号
		ap1Level = 0;
		ap2Level = 0;
		for(int i=0;i<times;i++){
			mWifiManager.startScan();
			//隔interval个时间扫一次
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<ScanResult> srList = mWifiManager.getScanResults();
			if (srList != null) {
				for (ScanResult sr : srList) {
					if (sr.BSSID.equals(AP1_BSSID)) {
						ap1Exist = true;
						ap1Level = sr.level;
					} else if (sr.BSSID.equals(AP2_BSSID)) {
						ap2Exist = true;
						ap2Level = sr.level;
					}
				}
				// 已经扫到信号，但是没有扫到ap1或ap2的就把其信号强度置0
				if (!ap1Exist) {
					ap1Level = 0;
				}else{					
					ap1ScannedTimes ++;
					ap1SumLevel = ap1SumLevel + ap1Level;
				}
				if (!ap2Exist) {
					ap2Level = 0;
				}else{
					ap2ScannedTimes ++;
					ap2SumLevel = ap2SumLevel + ap2Level;
				}
			}
		}
		//计算扫到的平均信号
		if(ap1SumLevel!=0){
			ap1Level = ap1SumLevel/ap1ScannedTimes;
		}
		if(ap2SumLevel!=0){
			ap2Level = ap2SumLevel/ap2ScannedTimes;
		}
	}

	public void run() {
		while (true) {
			if (getSampleState.equals(GetSampleState.START_GETSAMPLE)) {
				getSignal();
				// 把采到的信号交给服务器
				connect.sendMsg(x + ";" + y + ";" + z + ";" + ap1Level
							+ ";" + ap2Level);
				Log.w("msg",ap1Level
						+ ";" + ap2Level);
				//样点交给服务器以后本次采样也就结束了
				getSampleState = GetSampleState.STOP_GETSAMPLE;
			} else if (getSampleState.equals(GetSampleState.STOP_GETSAMPLE)) {
				connect.sendMsg("exit");
				break;
			} else if (getSampleState.equals(GetSampleState.LOCATE)) {
				getSignal();
				connect.sendMsg("locate");
				connect.sendMsg(ap1Level+ ";" + ap2Level);
				location = connect.receiveMsg();
				break;
			}
		}
		//等待界面用location去更新数据，防止采样进程过早地被垃圾回收
		try {
			sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
