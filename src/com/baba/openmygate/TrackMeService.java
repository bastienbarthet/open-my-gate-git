package com.baba.openmygate;

import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

public class TrackMeService extends Service {
	
	private LocationManager	locationMgr;
	private LocationListener onLocationChange = 
			
//		new GateLocationListener();
		
		new LocationListener() {
		
			private final double gateLatitude = 43.61278002558139;
			private final double gateLongitude = 1.392238611074346;
			private final double radius = 125; //radius detection in meters
			private final String numTel = "+33689838373";
			private double distance;
			private boolean gateOpened;
			private double latitude;
			private double longitude;
			private boolean gpsStatus;
			
			@Override
			public void onLocationChanged(Location location) {
				
				final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
				this.gpsStatus = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
				
				if (this.gpsStatus) {
					this.latitude = location.getLatitude();
					this.longitude = location.getLongitude();
					
					if (isNearToGate(longitude, latitude)) {
						if (!this.gateOpened) {
							this.gateOpened = true;;
							Toast.makeText(getBaseContext(),
									"Position : -latitude: " + this.latitude + " // longitude : " + this.longitude + "\n"
											+ "Distance: " + this.distance + "\n"
											+ "Portail ouvert ? " + this.gateOpened + "\n"
											+ "Status du GPS: " + this.gpsStatus, 
											Toast.LENGTH_LONG).show();
							openGate();
						}
					} else {
						this.gateOpened = false;
					}
					
					
				} else {
					Toast.makeText(getBaseContext(), "GPS OFF, veuillez activer le GPS", Toast.LENGTH_LONG).show();

				}
			}
	
			@Override
			public void onProviderDisabled(String provider) {
				Toast.makeText(getBaseContext(), "GPS OFF", Toast.LENGTH_LONG).show();
			}
	
			@Override
			public void onProviderEnabled(String provider) {
				Toast.makeText(getBaseContext(), "GPS ON", Toast.LENGTH_LONG).show();
			}
	
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				// TODO Auto-generated method stub
	
			}
			
		    private void openGate() {
		    	try {
		            Intent callIntent = new Intent(Intent.ACTION_CALL);
		            callIntent.setData(Uri.parse("tel:"+this.numTel));
		            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            startActivity(callIntent);
		        } catch (ActivityNotFoundException e) {
		            e.printStackTrace();
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
		
		 
	};
		
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		if (locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			locationMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, onLocationChange);
			super.onCreate();
			Toast.makeText(getBaseContext(), "Service OpenMyGate - TrackMe : Demarré", Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(getBaseContext(), "GPS OFF, veuillez activer le GPS", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(getBaseContext(), "Service OpenMyGate - TrackMe : Arrêté", Toast.LENGTH_LONG).show();
		super.onDestroy();
		locationMgr.removeUpdates(onLocationChange);
	 }
	 
}
	 
