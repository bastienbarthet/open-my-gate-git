package com.baba.openmygate;

import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.provider.CallLog;
import android.widget.Toast;

public class BluetoothHeadsetConnectionService extends Service {
	
	private final String numTel = "+33689838373";
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	//The BroadcastReceiver that listens for bluetooth broadcasts
	private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
	        String action = intent.getAction();
	        if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
	        	Toast.makeText(getBaseContext(), "Bluetooth Headset Connected", Toast.LENGTH_LONG).show();
	        	addGateNumberOnFirstPlace();
	        }
	    }
	};
	
	private void addGateNumberOnFirstPlace() {
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
	
	@Override
	public void onCreate() {
		IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
	    this.registerReceiver(mReceiver, filter1);
	    super.onCreate();
	}
	
}
	 
