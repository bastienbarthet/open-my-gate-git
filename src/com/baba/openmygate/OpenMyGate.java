package com.baba.openmygate;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class OpenMyGate extends Activity {

	private Button launchButton;
	private Button stopButton;
	private TextView serviceStatus;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.activity_main);
  
        launchButton = (Button) findViewById(R.id.button1);
        stopButton = (Button) findViewById(R.id.button2);
        serviceStatus = (TextView) findViewById(R.id.textServiceStatus);
        
        launchButton.setOnClickListener( 
        		
        	new OnClickListener(){
	        	 @Override
	        	 public void onClick(View actuelView) {
	        		 startService(new Intent(OpenMyGate.this, OpenMyGateService.class));
	        		 isMyServiceRunning();
	        	 }
        	});
        
        stopButton.setOnClickListener( 
        	new OnClickListener(){
	        	 @Override
	        	 public void onClick(View actuelView) {
	        		 stopService(new Intent(OpenMyGate.this, OpenMyGateService.class));
	        		 isMyServiceRunning();
	        	 }
        	});
        
        this.isMyServiceRunning();
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	this.isMyServiceRunning();
    	
    };
    
    private void isMyServiceRunning() {
    	
    	this.launchButton.setEnabled(true);
    	this.stopButton.setEnabled(false);
    	this.serviceStatus.setText("OFF");
    	
    	final LocationManager locManag = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
		if (!locManag.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Toast.makeText(getBaseContext(), "GPS OFF, veuillez d�marrer le GPS", Toast.LENGTH_LONG).show();
		}
    	
		
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (OpenMyGateService.class.getName().equals(service.service.getClassName())) {
            	this.launchButton.setEnabled(false);
            	this.stopButton.setEnabled(true);
            	this.serviceStatus.setText("ON");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    

}
