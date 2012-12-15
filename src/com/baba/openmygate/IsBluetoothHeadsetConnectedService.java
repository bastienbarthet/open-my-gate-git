package com.baba.openmygate;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class IsBluetoothHeadsetConnectedService extends Service {
	
	private static final long ONE_MINUTE = 60*1000;
	private BluetoothAdapter bluetoothAdapter;
	private Handler mHandler = new Handler();
	
	private Runnable periodicTask = new Runnable() {
        public void run() {
            IsBluetoothHeadsetConnectedService.this.runTrackingServiceIfHeadsetConnected();
            mHandler.postDelayed(periodicTask, ONE_MINUTE);
        }
    };
    
	private void runTrackingServiceIfHeadsetConnected() {
		if (this.isBluetoothOn()) {
			final boolean isHeadsetConnected = this.bluetoothAdapter.getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET) != android.bluetooth.BluetoothProfile.STATE_DISCONNECTED;
			if (isHeadsetConnected) {
				startService(new Intent(IsBluetoothHeadsetConnectedService.this, TrackMeService.class));
			} else {
				stopService(new Intent(IsBluetoothHeadsetConnectedService.this, TrackMeService.class));
			}
		} else {
			Toast.makeText(getBaseContext(), "Bluetooth OFF, veuillez activer le Bluetooth", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onCreate() {
		if (this.isGpsOn()) {
			this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (this.isBluetoothOn()) {
				 this.mHandler.postDelayed(periodicTask, ONE_MINUTE);
				super.onCreate();
				Toast.makeText(getBaseContext(), "Service IsBluetoothOn :  Demarré", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getBaseContext(), "Bluetooth OFF, veuillez activer le Bluetooth", Toast.LENGTH_LONG).show();
			}
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
		Toast.makeText(getBaseContext(), "Service IsBluetoothOn : Arrêté", Toast.LENGTH_LONG).show();
		mHandler.removeCallbacks(periodicTask);
		stopService(new Intent(IsBluetoothHeadsetConnectedService.this, TrackMeService.class));
		super.onDestroy();
	 }
	
	//check if gps is on
	private boolean isGpsOn(){
		LocationManager locationMgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}
	
	//check if bluetooth is on
	private boolean isBluetoothOn(){
		return ( (this.bluetoothAdapter != null) && (this.bluetoothAdapter.isEnabled()) );
	}
	 
}
	 
