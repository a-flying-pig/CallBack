package com.example.callback.activity;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.callback.adapter.LeDeviceListAdapter;
import com.example.callback.R;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeviceListAdapterActivity extends ListActivity {

    private final String TAG = "DeviceListAdapter";
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private List<BluetoothDevice> mBluetoothDevices;
    private BluetoothLeScanner mBluetoothLeScanner;
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
        mBluetoothLeScanner = mBluetoothAdapter.getBluetoothLeScanner();
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
                    //mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    mBluetoothLeScanner.stopScan(mScanCallback);
                }
            }, SCAN_PERIOD);

            mScanning = true;
            //mBluetoothAdapter.startLeScan(mLeScanCallback);

            List<ScanFilter> filters = new ArrayList<>();

            ScanFilter filter = new ScanFilter.Builder()
                    .setServiceUuid( new ParcelUuid(UUID.fromString("CDB7950D-73F1-4D4D-8E47-C090502DBD63") ) )
                    .build();
            filters.add( filter );

            ScanSettings settings = new ScanSettings.Builder()
                    .setScanMode( ScanSettings.SCAN_MODE_LOW_LATENCY )
                    .build();

            mBluetoothLeScanner.startScan(filters, settings, mScanCallback);
        } else {
            mScanning = false;
            //mBluetoothAdapter.stopLeScan(mLeScanCallback);
            mBluetoothLeScanner.stopScan(mScanCallback);
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

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            Log.d(TAG, "onScanResult ");
            final BluetoothDevice device = result.getDevice();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG, "onScanResult addDevice");
                    mLeDeviceListAdapter.addDevice(device);
                }
            });
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            Log.d(TAG, "onScanFailed errorCode: " + errorCode);
        }
    };


}
