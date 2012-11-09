package com.baba.openmygate;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class GateLocationListener extends Activity implements LocationListener {

	private final double gateLatitude = 43.61206;
	private final double gateLongitude = 1.39245;
	private final double radius = 50; //radius detection in meters
	private final String numTel = "+33689838373";
	private double distance;
	private boolean gateOpened;
	private double latitude;
	private double longitude;
	private boolean gpsStatus;
	
	public GateLocationListener() {
		super();
		this.gateOpened = false;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		 
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
		
		if (isNearToGate(longitude, latitude)) {
			if (!this.gateOpened) {
				this.gateOpened = true;;
				openGate();
			}
		} else {
			this.gateOpened = false;
		}
		
		
	 	Toast.makeText(getBaseContext(),
	 			"Position : " + this.latitude + " " + this.longitude + "\n"
	 			+ "Distance : " + this.distance + "\n"
	 			+ "Gate Opened ? : " + this.gateOpened + "\n"
	 			+ "GPS Status : " + this.gpsStatus, 
	 			Toast.LENGTH_LONG).show();
	}

	@Override
	public void onProviderDisabled(String provider) {
		this.gpsStatus = false;
	}

	@Override
	public void onProviderEnabled(String provider) {
		this.gpsStatus = true;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	
    private void openGate() {
    	try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+this.numTel));
            startActivity(callIntent);
        } catch (ActivityNotFoundException e) {
            //Log.e("dialing", "Call failed", e);
        }
    }
    
    private boolean isNearToGate(double longitude, double latitude) {
    	final double deltaLatitude = latitude-this.gateLatitude;
    	final double deltaLongitude = longitude-this.gateLongitude;
    	
    	this.distance = Math.sqrt(deltaLatitude*deltaLatitude + deltaLongitude*deltaLongitude)*111.11*1000;
    	
    	boolean result = false;
    	if (this.distance < this.radius) {
    		result = true;
    	}
    	return result;
    }

}
