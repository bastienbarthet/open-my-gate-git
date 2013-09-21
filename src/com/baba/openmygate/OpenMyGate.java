package com.baba.openmygate;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ToggleButton;

public class OpenMyGate extends Activity {

	private ToggleButton toggleButtonHeadset;
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.activity_main);
  
        toggleButtonHeadset =  (ToggleButton) findViewById(R.id.toggleButtonHeadset);
        
        toggleButtonHeadset.setOnClickListener(
        		
        		new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (toggleButtonHeadset.isChecked()) {
							startService(new Intent(OpenMyGate.this, BluetoothHeadsetConnectionService.class));
						} else {
							stopService(new Intent(OpenMyGate.this, BluetoothHeadsetConnectionService.class));
						}
						
					}
        		});
        
       this.areServicesRunning();
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	this.areServicesRunning();
    };
    
    private void areServicesRunning() {
    	
    	this.toggleButtonHeadset.setChecked(false);
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (BluetoothHeadsetConnectionService.class.getName().equals(service.service.getClassName())) {
            	this.toggleButtonHeadset.setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    

}
