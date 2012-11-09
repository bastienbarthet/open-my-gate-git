package com.baba.openmygate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class OpenMyGate extends Activity implements LocationListener{

	private TextView latitudeField;
	private TextView longitudeField;
	private TextView openGateField;
	private TextView distanceField;
	private TextView gpsStatus;
	private Button launchButton;
	private LocationManager locationManager;
	private String provider;
	private final double gateLatitude = 43.61206;
	private final double gateLongitude = 1.39245;
	private final double radius = 50; //radius detection in meters
	private final String numTel = "+33689838373";
	  
	 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        
        latitudeField = (TextView) findViewById(R.id.textLatitude);
        longitudeField = (TextView) findViewById(R.id.textLongitude);
        openGateField = (TextView) findViewById(R.id.textOpenGate);
        distanceField = (TextView) findViewById(R.id.textDistance);
        gpsStatus = (TextView) findViewById(R.id.textGpsStatus);
        launchButton = (Button) findViewById(R.id.button1);
        
        launchButton.setOnClickListener( 
        		
        	new OnClickListener(){

	        	 @Override
	        	 public void onClick(View actuelView) {
	        		 startService(new Intent(OpenMyGate.this, OpenMyGateService.class));
	        	 }
	        	 
        	}
        );
        
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
          System.out.println("Provider " + provider + " has been selected.");
          onLocationChanged(location);
          gpsStatus.setText("ON");
        } else {
          latitudeField.setText("Location not available");
          longitudeField.setText("Location not available");
          gpsStatus.setText("OFF");
        }
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    private void openGate() {
    	try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+this.numTel));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            //Log.e("helloandroid dialing example", "Call failed", e);
        }
    }
    
    private boolean isNearToGate(double longitude, double latitude) {
    	final double deltaLatitude = latitude-this.gateLatitude;
    	final double deltaLongitude = longitude-this.gateLongitude;
    	
    	final double distance = Math.sqrt(deltaLatitude*deltaLatitude + deltaLongitude*deltaLongitude)*111.11;
    	this.distanceField.setText(String.valueOf(distance*1000));
    	
    	boolean result = false;
    	if (distance*1000 < radius) {
    		result = true;
    	}
    	return result;
    }

    /* Request updates at startup */
    @Override
    protected void onResume() {
      super.onResume();
      locationManager.requestLocationUpdates(provider, 1*1000, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
      super.onPause();
      //locationManager.removeUpdates(this);
    }

    
	@Override
	public void onLocationChanged(Location location) {
		
		this.onResume();
		
		final double latitude = location.getLatitude();
		final double longitude = location.getLongitude();
		
		if (isNearToGate(longitude, latitude)) {
			if (!this.openGateField.getText().equals("YES")) {
				this.openGateField.setText("YES");
				openGate();
			}
		} else {
			this.openGateField.setText("NO");
		}
		
		this.latitudeField.setText(String.valueOf(latitude));
		this.longitudeField.setText(String.valueOf(longitude));
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		this.gpsStatus.setText("OFF");
	}

	@Override
	public void onProviderEnabled(String provider) {
		this.gpsStatus.setText("ON");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
}
