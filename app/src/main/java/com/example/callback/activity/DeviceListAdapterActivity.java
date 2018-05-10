package com.example.callback.activity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.callback.adapter.LeDeviceListAdapter;
import com.example.callback.R;

import java.util.ArrayList;
import java.util.List;

public class DeviceListAdapterActivity extends ListActivity {

    private final String TAG = "DeviceListAdapter";
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private List<BluetoothDevice> mBluetoothDevices;
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        BluetoothDevice bluetoothDevice =(BluetoothDevice) getListView().getItemAtPosition(position);
    }

    private void init() {
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        mBluetoothDevices = new ArrayList<>();
            mLeDeviceListAdapter = new LeDeviceListAdapter(this, R.layout.bluetooth_device, mBluetoothDevices);
        mHandler = new Handler();
        // start scan
        scanLeDevice(true);
        setListAdapter(mLeDeviceListAdapter);
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    Log.d(TAG, "start le scan 10s");
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, int rssi,
                                     byte[] scanRecord) {
                    Log.d(TAG, "onLeScan result " + device.getName());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, "onLeScan result addDevice");
                            mLeDeviceListAdapter.addDevice(device);
                        }
                    });
                }
            };
}
