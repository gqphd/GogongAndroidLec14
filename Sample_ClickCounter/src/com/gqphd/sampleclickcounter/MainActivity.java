package com.gqphd.sampleclickcounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	int m_iClick = 0;
	Button m_btnUp;
	TextView m_tvCount;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		m_btnUp = (Button)findViewById(R.id.btnUp);
		m_tvCount = (TextView)findViewById(R.id.tvCount);
		
		m_btnUp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m_iClick++;
				m_tvCount.setText("x " + String.format("%02d", m_iClick));
			}
		});
	}
}
