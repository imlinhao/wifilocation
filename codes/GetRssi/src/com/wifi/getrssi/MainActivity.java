package com.wifi.getrssi;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	//��ȡ���
	TextView textView1;
	Button button1,button2;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textView1=(TextView)findViewById(R.id.textView1);
		button1=(Button)findViewById(R.id.button1);
		button2=(Button)findViewById(R.id.button2);
		
		Criteria criteria=new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		
		//����gps��λ
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//����locationManage����
				LocationManager locManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
				//�ж�GPS�Ƿ���������
		        if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		            Toast.makeText(MainActivity.this, "�뿪��GPS����...", Toast.LENGTH_SHORT).show();
		            //���ؿ���GPS�������ý���
		            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);   
		            startActivityForResult(intent,0); 
		            return;
		        }
		     // ��GPS��ȡ���������Ķ�λ��Ϣ
				Location location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				updateView(location);
				// ����ÿ3���ȡһ��GPS�Ķ�λ��Ϣ
				locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, locationListener);
			}
		});
		
		//ִ���ռ�����ʱ
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), Second.class);
				startActivity(intent);
			}
		});
		
	}
	
	
	private final LocationListener locationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			//GPS״̬Ϊ�ɼ�ʱ
			switch (status) {
            case LocationProvider.AVAILABLE:
            	Toast.makeText(MainActivity.this, "��ǰGPS״̬Ϊ�ɼ�״̬", Toast.LENGTH_SHORT).show();
                break;
            //GPS״̬Ϊ��������ʱ
            case LocationProvider.OUT_OF_SERVICE:
            	Toast.makeText(MainActivity.this, "��ǰGPS״̬Ϊ��������״̬", Toast.LENGTH_SHORT).show();
                break;
            //GPS״̬Ϊ��ͣ����ʱ
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
            	Toast.makeText(MainActivity.this, "��ǰGPS״̬Ϊ��ͣ����״̬", Toast.LENGTH_SHORT).show();
                break;
        }
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			// ��GPS LocationProvider����ʱ������λ��
			//updateView(locManager.getLastKnownLocation(provider));
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			updateView(null);
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			// ��GPS��λ��Ϣ�����ı�ʱ������λ��
			updateView(location);
		}
	};
	
	
	// ����EditText����ʾ������
		public void updateView(Location newLocation)
		{
			if (newLocation != null)
			{
				float s = newLocation.getAccuracy();//��ȷ��
				String t=String.valueOf(s);
				Toast.makeText(MainActivity.this, "��ȷ����:"+t, Toast.LENGTH_SHORT).show();
				StringBuilder sb = new StringBuilder();
				sb.append("ʵʱ��λ����Ϣ��\n");
				sb.append("���ȣ�");
				sb.append(newLocation.getLongitude());
				sb.append("\nγ�ȣ�");
				sb.append(newLocation.getLatitude());
				textView1.setText(sb.toString());
			}
			else
			{
				// ��������Location����Ϊ�������EditText
				textView1.setText("");
			}
		}

}
