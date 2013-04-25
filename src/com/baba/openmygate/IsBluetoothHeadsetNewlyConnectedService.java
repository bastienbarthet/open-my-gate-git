package com.baba.openmygate;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.provider.CallLog;
import android.widget.Toast;

public class IsBluetoothHeadsetNewlyConnectedService extends Service {
	
	private static final long checkPeriod = 5*1000;
	private BluetoothAdapter bluetoothAdapter;
	private Handler mHandler = new Handler();
	private boolean headsetYetConnected;
	private final String numTel = "+33689838373";
	
	private Runnable periodicTask = new Runnable() {
        public void run() {
            IsBluetoothHeadsetNewlyConnectedService.this.setCallLogIfHeadsetNewlyConnected();
            mHandler.postDelayed(periodicTask, checkPeriod);
        }
    };
    
	private void setCallLogIfHeadsetNewlyConnected() {
		if (!this.headsetYetConnected) {
			if (this.isHeadSetConnected()) {
				ContentValues values = new ContentValues();
				values.put(CallLog.Calls.NUMBER, this.numTel);
				values.put(CallLog.Calls.DATE, System.currentTimeMillis());
				values.put(CallLog.Calls.DURATION, 0);
				values.put(CallLog.Calls.TYPE, CallLog.Calls.OUTGOING_TYPE);
				values.put(CallLog.Calls.NEW, 1);
				values.put(CallLog.Calls.CACHED_NAME, "");
				values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
				values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
				ContentResolver contentResolver = getContentResolver();
				contentResolver.insert(CallLog.Calls.CONTENT_URI, values);
			}
		}
		this.headsetYetConnected = this.isHeadSetConnected();
	}
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	// check is any bluetooth headset is connected
	private boolean isHeadSetConnected() {
		return (this.isBluetoothOn())&&(this.bluetoothAdapter.getProfileConnectionState(android.bluetooth.BluetoothProfile.HEADSET) != android.bluetooth.BluetoothProfile.STATE_DISCONNECTED);
	}
	
	@Override
	public void onCreate() {
		this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		this.headsetYetConnected = false;
		this.mHandler.postDelayed(periodicTask, checkPeriod);
		super.onCreate();
		Toast.makeText(getBaseContext(), "Service HeadsetNewlyConnected :  Demarré", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		 return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Toast.makeText(getBaseContext(), "Service HeadsetNewlyConnected : Arrêté", Toast.LENGTH_LONG).show();
		mHandler.removeCallbacks(periodicTask);
		stopService(new Intent(IsBluetoothHeadsetNewlyConnectedService.this, TrackMeService.class));
		super.onDestroy();
	 }
	
	//check if bluetooth is on
	private boolean isBluetoothOn(){
		return ( (this.bluetoothAdapter != null) && (this.bluetoothAdapter.isEnabled()) );
	}
	 
}
	 
