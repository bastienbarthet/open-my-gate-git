package com.baba.openmygate;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class GateLocationListener implements LocationListener {


	private TextView latitudeField;
	private TextView longitudeField;
	
	public GateLocationListener(TextView latitudeField, TextView longitudeField) {
		this.latitudeField = latitudeField;
		this.longitudeField = longitudeField;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		this.latitudeField.setText(String.valueOf(location.getLatitude()));
		this.longitudeField.setText(String.valueOf(location.getLongitude()));

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
