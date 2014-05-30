package com.mingrisoft;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class LinkThread extends Thread {
	String mLocation = null;
	String mMsg = null;
	MainActivity mMa;
	
	public LinkThread(MainActivity ma, String msg) {
		mMa = ma;
		mMsg = msg;
	}
	
	public void run() {
		Connect mConnect = new Connect();
		mConnect.sendMsg(mMsg);
		mLocation = mConnect.receiveMsg();
		//Log.d("client", mLocation);
		Message m = mMa.mHandler.obtainMessage();
		m.what = 0x101;
		Bundle bundle = new Bundle();
		bundle.putString("location", mLocation);
		m.setData(bundle);
		mMa.mHandler.sendMessage(m);
	}
}