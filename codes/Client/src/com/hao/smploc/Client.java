package com.hao.smploc;

import com.hao.smploc.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Client extends Activity {

	private TextView locationTv;
	private TextView xTv,yTv,zTv;
	private EditText xEt,yEt,zEt;
	private Button startGetSampleBtn;
	//private Button stopGetSampleBtn;
	private Button locateBtn;
	GetSample getSample = null;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		findviews();
		setonclick();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(getSample!=null){
			if(getSample.getGetSampleState().equals(GetSampleState.START_GETSAMPLE)){
				getSample.setGetSampleState(GetSampleState.STOP_GETSAMPLE);
			}			
		}	
		finish();
	}

	private void findviews() {
		xTv = (TextView) this.findViewById(R.id.xTv);
		xEt = (EditText) this.findViewById(R.id.xEt);
		yTv = (TextView) this.findViewById(R.id.yTv);
		yEt = (EditText) this.findViewById(R.id.yEt);
		zTv = (TextView) this.findViewById(R.id.zTv);
		zEt = (EditText) this.findViewById(R.id.zEt);
		locationTv = (TextView) this.findViewById(R.id.locationTv);
		startGetSampleBtn = (Button) this.findViewById(R.id.startGetSampleBtn);
		//stopGetSampleBtn = (Button) this.findViewById(R.id.stopGetSampleBtn);
		locateBtn = (Button) this.findViewById(R.id.locateBtn);
	}

	private void setonclick() {
		startGetSampleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int x = Integer.parseInt(xEt.getText().toString());
				int y = Integer.parseInt(yEt.getText().toString());
				int z = Integer.parseInt(zEt.getText().toString());
				getSample = new GetSample(Client.this,x,y,z);
				xEt.setText("");
				yEt.setText("");
				zEt.setText("");
				getSample.setGetSampleState(GetSampleState.START_GETSAMPLE);
				getSample.start();
			}
		});
		
		/*stopGetSampleBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getSample!=null){
					getSample.setGetSampleState(GetSampleState.STOP_GETSAMPLE);					
				}
			}
		});*/
		
		locateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(getSample!=null){
					getSample.setGetSampleState(GetSampleState.STOP_GETSAMPLE);	
					getSample = null;
				}
				getSample = new GetSample(Client.this,0,0,0);
				getSample.setGetSampleState(GetSampleState.LOCATE);
				getSample.start();
				while(getSample.location ==null){
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				locationTv.setText("ÄãµÄÎ»ÖÃÎª:" + getSample.location);
			}
		});
	}
}