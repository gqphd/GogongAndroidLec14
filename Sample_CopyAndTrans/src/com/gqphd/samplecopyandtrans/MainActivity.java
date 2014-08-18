package com.gqphd.samplecopyandtrans;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

public class MainActivity extends Activity {

	ToggleButton togBtn;
	SharedPreferences pref;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//title
		setTitle("Copy And Translate");
		
		//get stored setting
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		
		//ui
		togBtn = (ToggleButton)findViewById(R.id.togOnOff);
		togBtn.setChecked(pref.getBoolean("SERVICE_ENABLED", false));
		
		togBtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean true_or_false) {
				SharedPreferences.Editor editor = pref.edit();
	        	editor.putBoolean("SERVICE_ENABLED", true_or_false);
	        	editor.commit();
	        	
	        	if(true_or_false){
	        		Intent intent = new Intent(getApplicationContext(), ClipboardWatchService.class);
	        		startService(intent);
	        	}else{
	        		Intent intent = new Intent(getApplicationContext(), ClipboardWatchService.class);
	        		stopService(intent);
	        	}
			}
		});
	}
	
	@Override
	protected void onResume() {
		
		//get setting value
		pref = getSharedPreferences("pref", MODE_PRIVATE);
		pref.getBoolean("SERVICE_ENABLED", false);
		
		super.onResume();
	}
}
