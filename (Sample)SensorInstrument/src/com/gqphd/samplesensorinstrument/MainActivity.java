package com.gqphd.samplesensorinstrument;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	LinearLayout m_linDbgValues;
	TextView[] m_arrTvDbgVals;
	
	private SensorManager m_mgrSensor;
	private Sensor m_sensorLinAcc;
	
	MediaPlayer m_mpTic; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//init dbg ui
		m_linDbgValues = (LinearLayout)findViewById(R.id.laylinAccval);
		m_arrTvDbgVals = new TextView[4];
		for(int i = 0 ; i <4; i++){
			m_arrTvDbgVals[i] = new TextView(getApplicationContext());
			m_arrTvDbgVals[i].setText(" VAL"+(i+1) + " ");
			m_arrTvDbgVals[i].setTextColor(Color.BLACK);
			m_linDbgValues.addView(m_arrTvDbgVals[i]);
		}
		
		//init sensor
		m_mgrSensor = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		m_sensorLinAcc = m_mgrSensor.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
		
		m_mpTic = MediaPlayer.create(getApplicationContext(), R.raw.macarastic);
		try {
			m_mpTic.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	SensorEventListener m_cbSensorListener = new SensorEventListener() {
		@Override
		public void onSensorChanged(SensorEvent event) {
			if(Sensor.TYPE_LINEAR_ACCELERATION == event.sensor.getType()){
				
				//display acceleration values
				for(int i = 0 ; i <3; i++){
					String str = String.format(" %.02f ", event.values[i]);
					m_arrTvDbgVals[i].setText(str);
				}			
				
				//check if shaking
				double pow = Math.sqrt(
						event.values[0]*event.values[0] +
						event.values[1]*event.values[1] +
						event.values[2]*event.values[2] 
						);
				
				double PowThreshold = 4.5;//heuristic
				m_arrTvDbgVals[3].setText("POW " + String.format("%.2f", pow));
				
				if(pow > PowThreshold){
					Log.i("gqphd","Shake detected - pow : " + String.format("%.2f", pow));
					m_arrTvDbgVals[3].setTextColor(Color.RED);
					
					//play sound
					m_mpTic.start();
					
				}else{
					m_arrTvDbgVals[3].setTextColor(Color.BLACK);
				}
				
			}
		}
		
		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		m_mgrSensor.registerListener(m_cbSensorListener, m_sensorLinAcc, SensorManager.SENSOR_DELAY_FASTEST);
	}
	
	@Override
	protected void onPause() {
		m_mgrSensor.unregisterListener(m_cbSensorListener);
		super.onPause();
	}
}
